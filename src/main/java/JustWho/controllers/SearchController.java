package JustWho.controllers;

import JustWho.dto.search.SearchResultDTO;
import JustWho.services.SearchService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller("/search")
public class SearchController {

    @Inject
    SearchService searchService;

    @Get(produces = MediaType.APPLICATION_JSON)
    public CompletableFuture<List<SearchResultDTO>> index() throws IOException {


        return searchService.search();
    }
}