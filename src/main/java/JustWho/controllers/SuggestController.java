package JustWho.controllers;

import JustWho.dto.search.SuggestResultDTO;
import JustWho.services.SearchService;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

import java.util.concurrent.CompletableFuture;

@Controller("/suggest")
public class SuggestController {

    @Inject
    SearchService searchService;

    @Get(produces = MediaType.TEXT_PLAIN)
    public CompletableFuture<String> index() throws Exception{

        final CompletableFuture<SearchResponse<SuggestResultDTO>> resultDTO = searchService.suggest("Fis");
        return resultDTO.thenApply(res -> res.suggest().toString());

    }
}