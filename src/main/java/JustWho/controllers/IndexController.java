package JustWho.controllers;

import JustWho.dto.index.SearchDTO;
import JustWho.services.IndexService;
import JustWho.services.MovieCollectorService;
import JustWho.util.MovieCollectorConfiguration;
import co.elastic.clients.elasticsearch.security.get_token.AuthenticationProvider;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    public Flux<List<String>> bla(final String startingYear, final String stoppingYear) {

        //final int startingYear = movieCollectorConfiguration.getStartingYear();
        //final int stoppingYear = movieCollectorConfiguration.getStoppingYear();
        LOGGER.info("Starting import job for years " + startingYear + " - " + stoppingYear);
        // load all movies for config
        Flux<List<String>> searchDTOFlux = movieCollectorService
                .fetchAllMoviesForRange(movieCollectorConfiguration.getStartingYear(),  movieCollectorConfiguration.getStoppingYear())
                .buffer(150)
                .flatMap(searchDTOs -> Mono.fromFuture(indexService.fillIndex(searchDTOs)))
                .doFinally(c -> LOGGER.info("completed"));;

        // subscription starts flux
        return searchDTOFlux;


    }
}