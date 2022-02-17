package JustWho.services;

import JustWho.dto.collector.GenreContainer;
import JustWho.dto.collector.MovieDataContainer;
import JustWho.dto.collector.SingleMovieData;
import JustWho.util.MovieCollectorConfiguration;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

import static io.micronaut.http.HttpHeaders.ACCEPT;
import static io.micronaut.http.HttpHeaders.USER_AGENT;

@Singleton
public class MovieCollectorClient {

    private final HttpClient httpClient;
    private final MovieCollectorConfiguration configuration;

    public MovieCollectorClient(@Client("${collector.url}") HttpClient httpClient,
                                MovieCollectorConfiguration configuration) {
        this.configuration = configuration;
        this.httpClient = httpClient;
    }

    Mono<SingleMovieData> fetchSingleMovie(String id) {

        final URI uri = UriBuilder.of("/3/movie")
                .path(id)
                .queryParam("api_key", configuration.getApiKey())
                .build();
        HttpRequest<?> req = HttpRequest.GET(uri)
                .header(USER_AGENT, "Micronaut HTTP Client")
                .header(ACCEPT, "application/json");
        return Mono.from(httpClient.retrieve(req, Argument.of(SingleMovieData.class)));
    }

    Flux<MovieDataContainer> fetchAllMoviesPerYear(int year) {

        final URI uri1 = buildUri(1, year);
        final HttpRequest request = HttpRequest.GET(uri1).header(USER_AGENT, "Micronaut HTTP Client").header(ACCEPT, "application/json");
        final MovieDataContainer movieDataContainerMono = httpClient.toBlocking().retrieve(request, Argument.of(MovieDataContainer.class));

        final int a = 1;
        // TODO: language?, how to get > 500 pages
        Flux<MovieDataContainer> bla = Flux.range(1, 2)
                .map(page -> buildUri(page, year))
                .map(uri -> HttpRequest.GET(uri).header(USER_AGENT, "Micronaut HTTP Client").header(ACCEPT, "application/json"))
                .map(req -> httpClient.retrieve(req, Argument.of(MovieDataContainer.class)))
                .flatMap(f -> Flux.from(f));

        return bla;
    }

    Mono<GenreContainer> fetchGenreMapping() {

        final URI uri = UriBuilder.of("/3/genre/movie/list")
                .queryParam("api_key", configuration.getApiKey())
                .build();
        HttpRequest<?> req = HttpRequest.GET(uri)
                .header(USER_AGENT, "Micronaut HTTP Client")
                .header(ACCEPT, "application/json");
        return Mono.from(httpClient.retrieve(req, Argument.of(GenreContainer.class)));
    }

    private URI buildUri(final int page, final int year) {
        return UriBuilder.of("/3/discover/movie")
                .queryParam("primary_release_year", year)
                .queryParam("api_key", configuration.getApiKey())
                .queryParam("page", page)
                .build();
    }
}

