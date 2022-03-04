package JustWho.services;

import JustWho.dto.index.Indexable;
import JustWho.dto.index.SearchDTO;
import JustWho.dto.index.SuggestDTO;
import JustWho.dto.search.SearchResultDTO;
import JustWho.util.Constants;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.transport.ElasticsearchTransport;
import io.netty.util.internal.StringUtil;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class IndexService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);

    @Inject
    ElasticsearchTransport elasticsearchTransport;
    @Inject
    ElasticsearchAsyncClient elasticsearchAsyncClient;

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

    public CompletableFuture<Stream<String>> getAllIds() {
        final SearchRequest searchRequest = new SearchRequest.Builder()
            .query(q -> q.matchAll(m -> m))
            .index(Constants.SEARCH_INDEX)
            .build();

        CompletableFuture<SearchResponse<SearchResultDTO>> responseFuture = elasticsearchAsyncClient.search(searchRequest, SearchResultDTO.class);

        return responseFuture.thenApply(x -> x.hits()
                                   .hits()
                                   .stream()
                                   .map(Hit::id)
                            );
    }
}
