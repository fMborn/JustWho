package JustWho.services;

import JustWho.dto.index.Indexable;
import JustWho.dto.index.SearchDTO;
import JustWho.util.Constants;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.transport.ElasticsearchTransport;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class IndexService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);

    @Inject
    ElasticsearchTransport elasticsearchTransport;
    @Inject
    ElasticsearchAsyncClient elasticsearchAsyncClient;

    public CompletableFuture<String> fillIndex(final Flux<SearchDTO> searchDTOS) throws Exception{

        final CompletableFuture<BulkResponse> searchIndexResponse = sendToIndex(Constants.SEARCH_INDEX, searchDTOS);

//        final List<SuggestDTO> suggestDTOs = searchDTOS.stream()
//                .map(movie -> new SuggestDTO(movie.getName(), movie.getYear()))
//                .collect(Collectors.toList());
//        final CompletableFuture<BulkResponse> suggestIndexResponse = sendToIndex(Constants.SUGGEST_INDEX, suggestDTOs);


        return searchIndexResponse.thenApply(Object::toString);
    }

    /**
     * Creates a bulk request and sends multiple instances to the given index
     * @param indexName
     * @param indexables
     * @return
     * @throws IOException
     */
    private CompletableFuture<BulkResponse> sendToIndex(final String indexName, final Flux<? extends Indexable> indexables) throws IOException {
        final Flux<BulkOperation> bulkOperations = indexables
                .map(indexable -> BulkOperation.of(b -> b.index(i -> i.id(indexable.getId()).document(indexable))));
        try {
            return elasticsearchAsyncClient.bulk(b -> b.index(indexName).operations(bulkOperations.collectList().block()));

        } catch (Exception e) {
            LOGGER.error("An exception occurred during indexing. Stacktrace: " + Arrays.toString(e.getStackTrace()));
            throw e;
        }

    }

}
