package JustWho.client;

import JustWho.dto.collector.CreditsContainer;
import JustWho.dto.collector.GenreContainer;
import JustWho.dto.collector.MovieDataContainer;
import JustWho.dto.collector.SingleMovieData;
import JustWho.util.MovieCollectorConfiguration;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import static io.micronaut.http.HttpHeaders.ACCEPT;
import static io.micronaut.http.HttpHeaders.USER_AGENT;

@Singleton
public class MovieCollectorClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieCollectorClient.class);
    private final HttpClient httpClient;
    private final MovieCollectorConfiguration configuration;

    public MovieCollectorClient(@Client("${collector.url}") HttpClient httpClient,
                                MovieCollectorConfiguration configuration) {
        this.configuration = configuration;
        this.httpClient = httpClient;
    }

    public Mono<SingleMovieData> fetchSingleMovie(String id) {

        final URI uri = UriBuilder.of("/3/movie")
                .path(id)
                .queryParam("api_key", configuration.getApiKey())
                .build();
        HttpRequest<?> req = HttpRequest.GET(uri)
                .header(USER_AGENT, "Micronaut HTTP Client")
                .header(ACCEPT, "application/json");
        return Mono.from(httpClient.retrieve(req, Argument.of(SingleMovieData.class)));
    }

    public Flux<MovieDataContainer> fetchAllMoviesPerYear(int year) {

        // TODO: language?, how to get > 500 pages

        return Flux.range(1, 500)
                .map(page -> buildRequest(page, year))
                .map(req -> httpClient.retrieve(req, Argument.of(MovieDataContainer.class)))    // call api
                .flatMap(Flux::from)
                .doOnError(e -> LOGGER.error(e.getMessage()))   // log error
                .onErrorResume(e -> Flux.empty());
    }

    public Mono<GenreContainer> fetchGenreMapping() {
        LOGGER.info("Fetching genre mapping");
        final URI uri = UriBuilder.of("/3/genre/movie/list")
                .queryParam("api_key", configuration.getApiKey())
                .build();
        HttpRequest<?> req = HttpRequest.GET(uri)
                .header(USER_AGENT, "Micronaut HTTP Client")
                .header(ACCEPT, "application/json");
        return Mono.from(httpClient.retrieve(req, Argument.of(GenreContainer.class)));
    }

    private MutableHttpRequest<Object> buildRequest(final int page, final int year) {
        final URI uri = UriBuilder.of("/3/discover/movie")
                .queryParam("primary_release_year", year)
                .queryParam("api_key", configuration.getApiKey())
                .queryParam("page", page)
                .build();
        LOGGER.debug("Calling url with parameters page: " + page + "year: " + year);
        return HttpRequest.GET(uri).header(USER_AGENT, "Micronaut HTTP Client").header(ACCEPT, "application/json");
    }

    public Mono<CreditsContainer> fetchCredits(String id) {
        final URI uri = UriBuilder.of("/3/movie")
                                  .path(id + "/credits")
                                  .queryParam("api_key", configuration.getApiKey())
                                  .build();
        HttpRequest<?> req = HttpRequest.GET(uri)
                                        .header(USER_AGENT, "Micronaut HTTP Client")
                                        .header(ACCEPT, "application/json");
        return Mono.from(httpClient.retrieve(req, Argument.of(CreditsContainer.class)));
    }
}

