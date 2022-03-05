package JustWho.services;

import JustWho.dto.collector.CreditsContainer;
import JustWho.dto.index.Indexable;
import JustWho.dto.index.SearchDTO;
import JustWho.dto.index.SuggestDTO;
import JustWho.dto.search.response.SearchResultDTO;
import JustWho.util.Constants;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import io.netty.util.internal.StringUtil;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

public class IndexService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);

    @Inject
    ElasticsearchAsyncClient elasticsearchAsyncClient;
    @Inject
    MovieCollectorService movieCollectorService;

    public CompletableFuture<List<String>> fillIndex(final List<SearchDTO> searchDTOS) {

        final CompletableFuture<BulkResponse> searchIndexResponseFuture = sendToIndex(Constants.SEARCH_INDEX, searchDTOS);

        final List<SuggestDTO> suggestDTOs = searchDTOS.stream()
                                                       .map(movie -> new SuggestDTO(movie.getTitle(), movie.getYear(), movie.getPosterPath()))
                                                       .collect(toList());
        final CompletableFuture<BulkResponse> suggestIndexResponseFuture = sendToIndex(Constants.SUGGEST_INDEX, suggestDTOs);

        return searchIndexResponseFuture.thenCombine(suggestIndexResponseFuture, (searchIndexResponse, suggestIndexResponse) -> {
            if (searchIndexResponse.errors() || suggestIndexResponse.errors()) {
                // TODO rp: maybe map over errors and give details to cause
                LOGGER.error("Bulk indexing failed. Errors: SearchIndex: " + searchIndexResponse.errors() + "SuggestIndex: " + suggestIndexResponse.errors());
                return List.of();
            } else {
                List<String> idList = searchIndexResponse.items().stream().map(BulkResponseItem::id).collect(toList());
                LOGGER.info("Completed indexing successfully for ids: " + StringUtil.join(", ", idList));
                return idList;
            }
        });

    }

    /**
     * Creates a bulk request and sends multiple instances to the given index.
     * @param indexName Name of the index data will be indexed into.
     * @param indexables Flux of DTOs getting indexed.
     * @return Returns the bulk response from elasticasyncclient
     */
    private CompletableFuture<BulkResponse> sendToIndex(final String indexName, final List<? extends Indexable> indexables) {
        final List<BulkOperation> bulkOperations = indexables
            .stream().map(indexable -> BulkOperation.of(b -> b.index(i -> i.id(indexable.getId()).document(indexable))))
            .collect(toList());

        return elasticsearchAsyncClient.bulk(b -> b.index(indexName).operations(bulkOperations));

    }

    // TODO scroll
    // to test functionality first function returns the default 10 search results
    public Flux<List<String>> indexCastAndCrew() {
        final Flux<Hit<SearchResultDTO>> searchResult = scrollSearchResults();
        final Flux<CreditsContainer> creditsResult = searchResult.flatMap(x -> movieCollectorService.fetchCredits(x));

        // TODO think about intellij warning, can it really be null?
        final Flux<SearchDTO> zippedResults = Flux.zip(searchResult, creditsResult, (fromIndex, credits) -> SearchDTO.ofResult(fromIndex.source(), fromIndex.id(), credits.getTopCastName(), credits.getDirectorName()))
                                                  .log();

        return zippedResults.buffer(100)
                            .flatMap(x -> Mono.fromFuture(fillIndex(x)))
                            .doFinally(c -> LOGGER.info("completed"));
    }

    private Flux<Hit<SearchResultDTO>> scrollSearchResults() {
        final SearchRequest searchRequest = new SearchRequest.Builder()
            .query(q -> q.matchAll(m -> m))
            .index(Constants.SEARCH_INDEX)
            .build();

        final CompletableFuture<SearchResponse<SearchResultDTO>> responseFuture = elasticsearchAsyncClient.search(searchRequest, SearchResultDTO.class);

        return Mono.fromFuture(responseFuture)
                   .flatMapMany(response -> Flux.fromIterable(response.hits().hits()));
    }
}
