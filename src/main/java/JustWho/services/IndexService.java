package JustWho.services;

import JustWho.dto.index.Indexable;
import JustWho.dto.index.SearchDTO;
import JustWho.dto.index.SuggestDTO;
import JustWho.util.Constants;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.transport.ElasticsearchTransport;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class IndexService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);

    @Inject
    ElasticsearchTransport elasticsearchTransport;
    @Inject
    ElasticsearchAsyncClient elasticsearchAsyncClient;

    public CompletableFuture<String> fillIndex() throws Exception{
        final String name = "Kung Pow: Enter the Fist";
        final int year = 2002;
        final SearchDTO searchDTO = new SearchDTO(name, List.of("Action", "Abenteuer", "Comedy"), year, 44 );
        final CompletableFuture<IndexResponse> searchIndexResponse = sendToIndex(Constants.SEARCH_INDEX, searchDTO);

        final SuggestDTO suggestDTO = new SuggestDTO(name, year);
        final CompletableFuture<IndexResponse> suggestIndexResponse = sendToIndex(Constants.SUGGEST_INDEX, suggestDTO);

        return searchIndexResponse
                .thenCombine(suggestIndexResponse, (a, b) -> a.result().toString() + " " + b.result().toString());
    }

    private CompletableFuture<IndexResponse> sendToIndex(final String indexName, final Indexable dto) throws IOException {
        try {
            return elasticsearchAsyncClient.index(
                    indexRequest -> indexRequest.index(indexName).id(dto.getId()).document(dto));

        } catch (Exception e) {
            LOGGER.error("An exception occurred during indexing: " + dto.toString() + "Stacktrace: " + Arrays.toString(e.getStackTrace()));
            throw e;
        }


    }

}
