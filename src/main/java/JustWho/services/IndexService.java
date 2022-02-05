package JustWho.services;

import JustWho.util.Constants;
import jakarta.inject.Inject;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IndexService {

    @Inject
    RestHighLevelClient client;

    public String fillIndex() throws Exception {

        final Map<String, Object> searchIndexMap = new HashMap<>();
        final String name = "Spider-Man: No Way Home";
        searchIndexMap.put("name", name);
        searchIndexMap.put("genres", List.of("Action, Abenteuer, Science Fiction"));
        searchIndexMap.put("year", 2021);
        searchIndexMap.put("ranking", 68);

        IndexRequest request = new IndexRequest(Constants.SEARCH_INDEX).source(searchIndexMap);
        IndexResponse searchIndexResponse =  client.index(request, RequestOptions.DEFAULT);

        final Map<String, Object> suggestIndexMap = new HashMap<>();
        suggestIndexMap.put("name", name);
        suggestIndexMap.put("year", 2021);
        suggestIndexMap.put("suggest_name", name);

        IndexRequest autosuggestRequest = new IndexRequest(Constants.SUGGEST_INDEX).source(suggestIndexMap);
        IndexResponse suggestIndexResponse =  client.index(autosuggestRequest, RequestOptions.DEFAULT);

        System.out.println(searchIndexResponse.status());
        System.out.println(suggestIndexResponse.status());

        return "";
    }


}
