package JustWho.services;

import JustWho.client.ChuckClient;
import JustWho.dto.ChuckJokeDTO;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

public class ChuckService {
  @Inject
  ChuckClient chuckClient;

  public Mono<String> bla() {
    return Mono.from(chuckClient.fetchJoke())
               .map(ChuckJokeDTO::getValue);
  }
}
