package JustWho.controllers;

import JustWho.dto.api.IndexRequestDTO;
import JustWho.dto.collector.SingleMovieData;
import JustWho.dto.index.SearchDTO;
import JustWho.services.IndexService;
import JustWho.services.MovieCollectorService;
import JustWho.util.MovieCollectorConfiguration;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.netty.util.internal.StringUtil;
import jakarta.inject.Inject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller("/index")
public class IndexController {

    @Inject
    IndexService indexService;
    @Inject
    MovieCollectorService movieCollectorService;
    @Inject
    MovieCollectorConfiguration movieCollectorConfiguration;

//    @Post("/fill")
//    public CompletableFuture<String> index(@Body @Valid List<IndexRequestDTO> movies) throws Exception{
//
//        final List<SearchDTO> searchDTOS = movies.stream().map(movie -> new SearchDTO(movie.getName(), movie.getGenres(), movie.getYear(), movie.getRanking())).collect(Collectors.toList());
//        return indexService.fillIndex(searchDTOS);
//    }

    @Get(value = "/bla", produces = MediaType.APPLICATION_JSON_STREAM)
    public CompletableFuture<String> bla(@QueryValue String id) throws Exception {

        Flux.just("1", "2", "3", "4").buffer(2).subscribe(s -> System.out.println(s));


        Flux<SearchDTO> bla = movieCollectorService
            .fetchAllMoviesForRange(movieCollectorConfiguration.getStartingYear(),  movieCollectorConfiguration.getStoppingYear());

        //indexService.
//
//        bla.subscribe(s -> s.)
//        return indexService.fillIndex(bla);
        return Mono.just("").toFuture();
    }
}