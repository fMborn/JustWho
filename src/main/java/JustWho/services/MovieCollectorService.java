package JustWho.services;

import JustWho.client.MovieCollectorClient;
import JustWho.dto.collector.GenreContainer;
import JustWho.dto.collector.MovieDataContainer;
import JustWho.dto.collector.SingleMovieData;
import JustWho.dto.index.SearchDTO;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

public class MovieCollectorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieCollectorService.class);
    @Inject
    MovieCollectorClient movieCollectorClient;


    public Mono<SingleMovieData> doSomething(final String id) {

        final Mono<SingleMovieData> data = movieCollectorClient
                .fetchSingleMovie(id)
                .doOnError(e -> LOGGER.error("Fetching movie with " + id + " failed: " + e.getMessage()));
        return data;
    }

    public Flux<SearchDTO> fetchAllMoviesForRange(final int startingYear, final int stoppingYear) {

        final Mono<Map<Integer, String>> genreMappingMono = movieCollectorClient
                .fetchGenreMapping().map(GenreContainer::getGenreMapping);

        final int count = stoppingYear - startingYear > 0 ? stoppingYear - startingYear : 1;

        Flux<SearchDTO> bla = genreMappingMono.flatMapMany(genreMapping -> Flux.range(startingYear, count)
                .flatMap(movieCollectorClient::fetchAllMoviesPerYear).log()
                .map(MovieDataContainer::getMovies)
                .delayElements(Duration.ofSeconds(1))
                .flatMapIterable(movies -> movies.stream().map(movie -> SearchDTO.of(movie, genreMapping)).collect(Collectors.toList()))
        );
        return bla;
    }



}
