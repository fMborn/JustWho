package JustWho.services;

import JustWho.dto.ChuckNorrisJokeDTO;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;

import static io.micronaut.http.HttpHeaders.ACCEPT;
import static io.micronaut.http.HttpHeaders.USER_AGENT;

@Client("https://api.chucknorris.io")
@Header(name = USER_AGENT, value = "Micronaut HTTP Client")
@Header(name = ACCEPT, value = "application/json")
public interface ChuckNorrisService {
    @Get("/jokes/random")
    Publisher<ChuckNorrisJokeDTO> getRandomJoke();
}
