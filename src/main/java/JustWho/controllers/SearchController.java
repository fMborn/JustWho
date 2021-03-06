package JustWho.controllers;

import JustWho.dto.api.SearchResponseDTO;
import JustWho.services.SearchService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller("/search")
public class SearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    @Inject
    SearchService searchService;

    @Get(produces = MediaType.APPLICATION_JSON)
    public CompletableFuture<SearchResponseDTO> search(
            String input,
            @Nullable @QueryValue List<String> activeGenres,
            @Nullable @QueryValue String startingYear,
            @Nullable @QueryValue String stoppingYear,
            @Nullable @QueryValue String minScore,
            @Nullable @QueryValue String maxScore
    ) {

        List<String> sanitizedActiveGenres = activeGenres != null ? activeGenres: Collections.emptyList();
        if (input.length() >= 2) {
            LOGGER.info("Searching with parameters: "
                    + "query: " + input
                    + ", active genres: "+ activeGenres
                    + ", startingYear: " + startingYear
                    + ", stoppingYear: "+ stoppingYear
                    + ", minScore: " + minScore
                    + ", maxScore: " + maxScore);
            return searchService.search(
                    input,
                    sanitizedActiveGenres,
                    startingYear,
                    stoppingYear,
                    minScore,
                    maxScore
                    );
        } else {
            return CompletableFuture.completedFuture(new SearchResponseDTO());
        }

    }
}