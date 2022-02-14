package JustWho.services;

import JustWho.dto.index.Indexable;
import JustWho.dto.index.SearchDTO;
import JustWho.dto.index.SuggestDTO;
import JustWho.util.Constants;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.transport.ElasticsearchTransport;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class IndexService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);

    @Inject
    ElasticsearchTransport elasticsearchTransport;
    @Inject
    ElasticsearchAsyncClient elasticsearchAsyncClient;

    public CompletableFuture<String> fillIndex(final List<SearchDTO> searchDTOS) {

        final CompletableFuture<BulkResponse> searchIndexResponse = sendToIndex(Constants.SEARCH_INDEX, searchDTOS);

        final List<SuggestDTO> suggestDTOs = searchDTOS.stream()
                .map(movie -> new SuggestDTO(movie.getName(), movie.getYear()))
                .collect(Collectors.toList());
        final CompletableFuture<BulkResponse> suggestIndexResponse = sendToIndex(Constants.SUGGEST_INDEX, suggestDTOs);


        return searchIndexResponse
                .thenCombine(suggestIndexResponse, (a, b) -> a.errors() + " " + b.errors());
    }

    private CompletableFuture<IndexResponse> sendToIndex(final String indexName, final Indexable dto) {
        try {
            return elasticsearchAsyncClient.index(
                    indexRequest -> indexRequest.index(indexName).id(dto.getId()).document(dto));

        } catch (Exception e) {
            LOGGER.error("An exception occurred during indexing: " + dto.toString() + "Stacktrace: " + Arrays.toString(e.getStackTrace()));
            throw e;
        }

    }

    private CompletableFuture<BulkResponse> sendToIndex(final String indexName, final List<? extends Indexable> indexables) {
        final List<BulkOperation> bulkOperations = indexables
                .stream().map(indexable -> BulkOperation.of(b -> b.index(i -> i.id(indexable.getId()).document(indexable))))
                .collect(Collectors.toList());
        try {
            return elasticsearchAsyncClient.bulk(b -> b.index(indexName).operations(bulkOperations));

        } catch (Exception e) {
            LOGGER.error("An exception occurred during indexing. Stacktrace: " + Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }


}
