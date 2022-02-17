package JustWho.controllers;

import JustWho.dto.api.SearchResponseDTO;
import JustWho.services.SearchService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Controller("/search")
public class SearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    @Inject
    SearchService searchService;

    @Get(produces = MediaType.APPLICATION_JSON)
    public CompletableFuture<SearchResponseDTO> index() throws IOException {


        return searchService.search();
    }
}