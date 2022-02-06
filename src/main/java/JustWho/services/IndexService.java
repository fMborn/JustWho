package JustWho.services;

import JustWho.dto.index.Indexable;
import JustWho.dto.index.SearchDTO;
import JustWho.dto.index.SuggestDTO;
import JustWho.util.Constants;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.transport.ElasticsearchTransport;
import jakarta.inject.Inject;
import org.elasticsearch.search.suggest.Suggest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class IndexService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);

    @Inject
    ElasticsearchTransport elasticsearchTransport;

    @Inject
    ElasticsearchAsyncClient elasticsearchAsyncClient;

    public CompletableFuture<String> fillIndex() {
        final String name = "Kung Pow: Enter the Fist";
        final String year = "2002-02-02";
        final SearchDTO searchDTO = new SearchDTO(name, List.of(11, 12, 31), year, 9.9 );
        final CompletableFuture<IndexResponse> searchIndexResponse = sendToIndex(Constants.SEARCH_INDEX, searchDTO);

        final SuggestDTO suggestDTO = new SuggestDTO(name, 2002);
        final CompletableFuture<IndexResponse> suggestIndexResponse = sendToIndex(Constants.SUGGEST_INDEX, suggestDTO);

        return searchIndexResponse
                .thenCombine(suggestIndexResponse, (a, b) -> a.result().toString() + " " + b.result().toString());
    }

    public CompletableFuture<IndexResponse> sendToIndex(final String indexName, final Indexable dto) {
        try {
            return elasticsearchAsyncClient.index(
                    indexRequest -> indexRequest.index(indexName).id(dto.getId()).document(dto));

        } catch (Exception e) {
            LOGGER.error("An exception occurred during indexing: " + dto.toString() + "Stacktrace: " + Arrays.toString(e.getStackTrace()));
            return CompletableFuture.failedFuture(e);
        }
    }

}
