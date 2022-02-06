package JustWho.services;

import JustWho.dto.tmdb.ResultContainer;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;

import static io.micronaut.http.HttpHeaders.ACCEPT;
import static io.micronaut.http.HttpHeaders.USER_AGENT;

@Client("https://api.themoviedb.org/3/discover")
@Header(name = USER_AGENT, value = "Micronaut HTTP Client")
@Header(name = ACCEPT, value = "application/json")
public interface ChuckNorrisService {
    @Get("/movie?api_key=4713a112c9a269b1a83e49e1367b2661&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_watch_monetization_types=flatrate")
    Publisher<ResultContainer> getRandomJoke();
}
