package JustWho.client;

import JustWho.config.ClientConfiguration;
import JustWho.dto.ChuckJokeDTO;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;

@Client(
    value = "${hallo.uri}",
    configuration = ClientConfiguration.class)
public interface ChuckClient {
  @Get("${hallo.path}")
  Publisher<ChuckJokeDTO> fetchJoke();
}
