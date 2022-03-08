package JustWho.controllers;

import JustWho.dto.search.response.SuggestResultDTO;
import JustWho.services.SearchService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import jakarta.inject.Inject;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller("/suggest")
public class SuggestController {

    @Inject
    SearchService searchService;

    @Get(produces = MediaType.APPLICATION_JSON)
    public CompletableFuture<List<SuggestResultDTO>> suggest( @QueryValue String input) {

        if (!input.isEmpty()) {
            return searchService.suggest(input);
        }  else {
            return CompletableFuture.completedFuture(List.of());
        }


    }
}