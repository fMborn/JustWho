package JustWho.controllers;

import JustWho.dto.api.IndexRequestDTO;
import JustWho.dto.collector.SingleMovieData;
import JustWho.dto.index.SearchDTO;
import JustWho.services.IndexService;
import JustWho.services.MovieCollectorService;
import JustWho.util.MovieCollectorConfiguration;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
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

    @Post("/fill")
    public CompletableFuture<String> index(@Body @Valid List<IndexRequestDTO> movies) throws Exception{

        final List<SearchDTO> searchDTOS = movies.stream().map(movie -> new SearchDTO(movie.getName(), movie.getGenres(), movie.getYear(), movie.getRanking())).collect(Collectors.toList());
        return indexService.fillIndex(searchDTOS);
    }

    @Get(value = "/bla", produces = MediaType.APPLICATION_JSON_STREAM)
    public Flux<SearchDTO> bla(@QueryValue String id) throws Exception {
        return movieCollectorService
               .fetchAllMoviesForRange(movieCollectorConfiguration.getStartingYear(),  movieCollectorConfiguration.getStoppingYear());
    }
}