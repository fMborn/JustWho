package JustWho.controllers;

import JustWho.dto.search.SearchResultDTO;
import JustWho.services.IndexService;
import JustWho.services.MovieCollectorService;
import JustWho.util.MovieCollectorConfiguration;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/index")
public class IndexController {

    @Inject
    MovieCollectorConfiguration movieCollectorConfiguration;
    @Inject
    MovieCollectorService movieCollectorService;
    @Inject
    IndexService indexService;

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
//    @Post("/fill")
//    public CompletableFuture<String> index(@Body @Valid List<IndexRequestDTO> movies) throws Exception{
//
//        final List<SearchDTO> searchDTOS = movies.stream().map(movie -> new SearchDTO(movie.getName(), movie.getGenres(), movie.getYear(), movie.getRanking())).collect(Collectors.toList());
//        return indexService.fillIndex(searchDTOS);
//    }

    @Get(value = "/importYears", produces = MediaType.APPLICATION_JSON_STREAM)
    public Flux<List<String>> bla(final int startingYear, final int stoppingYear) {

        //final int startingYear = movieCollectorConfiguration.getStartingYear();
        //final int stoppingYear = movieCollectorConfiguration.getStoppingYear();
        LOGGER.info("Starting import job for years " + startingYear + " - " + stoppingYear);
        // load all movies for config
        Flux<List<String>> searchDTOFlux = movieCollectorService
                .fetchAllMoviesForRange(startingYear,  stoppingYear)
                .buffer(150)
                .flatMap(searchDTOs -> Mono.fromFuture(indexService.fillIndex(searchDTOs)))
                .doFinally(c -> LOGGER.info("completed"));

        // subscription starts flux
        return searchDTOFlux;


    }

    @Get(value = "/test", produces = MediaType.APPLICATION_JSON)
    public Flux<List<String>> indexCredits() {
        LOGGER.info("Starting import for credits.");

        return indexService.indexCastAndCrew();
    }
}