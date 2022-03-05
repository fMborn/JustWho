package JustWho.services;

import JustWho.client.MovieCollectorClient;
import JustWho.dto.collector.CreditsContainer;
import JustWho.dto.collector.GenreContainer;
import JustWho.dto.collector.MovieDataContainer;
import JustWho.dto.collector.SingleMovieData;
import JustWho.dto.index.SearchDTO;
import JustWho.dto.search.response.SearchResultDTO;
import co.elastic.clients.elasticsearch.core.search.Hit;
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
                .flatMapIterable(movies -> movies.stream().map(movie -> SearchDTO.of(movie, genreMapping)).collect(Collectors.toList()))
                .delayElements(Duration.ofMillis(300))
        );
        return bla;
    }

    public Mono<CreditsContainer> fetchCredits(final Hit<SearchResultDTO> searchResult) {
        return movieCollectorClient.fetchCredits(searchResult.id());
    }

}
