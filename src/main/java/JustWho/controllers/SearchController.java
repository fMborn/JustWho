package JustWho.controllers;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.core.InfoResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Controller("/search")
public class SearchController {

    @Inject
    ElasticsearchAsyncClient client;

    @Get(produces = MediaType.TEXT_PLAIN)
    public CompletableFuture<String> index() throws IOException {

        CompletableFuture<InfoResponse> info = client.info();


        return info.thenApply(InfoResponse::tagline);
    }
}