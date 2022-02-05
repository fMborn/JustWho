package JustWho.services;

import JustWho.util.Constants;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class IndexService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);

    @Inject
    ElasticsearchAsyncClient elasticsearchAsyncClient;

    public String fillIndex() {
        final Map<String, Object> searchIndexMap = new HashMap<>();
        final String name = "Kung Pow: Enter the Fist";
        searchIndexMap.put("name", name);
        searchIndexMap.put("genres", List.of("Action, Abenteuer, Comedy"));
        searchIndexMap.put("year", 2002);
        searchIndexMap.put("ranking", 1);

        try {
            CompletableFuture<IndexResponse> indexResponseFuture = elasticsearchAsyncClient.index(
                    indexRequest -> indexRequest.index(Constants.SEARCH_INDEX).document(searchIndexMap)
            );
        } catch(Exception e) {
            LOGGER.error("Doof!", e);
        }

/*        final Map<String, Object> suggestIndexMap = new HashMap<>();
        suggestIndexMap.put("name", name);
        suggestIndexMap.put("year", 2021);
        suggestIndexMap.put("suggest_name", name);

        IndexRequest autosuggestRequest = new IndexRequest(Constants.SUGGEST_INDEX).source(suggestIndexMap);
        IndexResponse suggestIndexResponse =  client.index(autosuggestRequest, RequestOptions.DEFAULT);

        System.out.println(searchIndexResponse.status());
        System.out.println(suggestIndexResponse.status());*/

        return "";
    }


}
