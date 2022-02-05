package JustWho.services;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import jakarta.inject.Inject;

public class SearchService {

    @Inject
    ElasticsearchAsyncClient client;


    public String search() {
        return "";
    }


    public String suggest() {
        return "";
    }
}
