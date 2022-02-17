package JustWho.services;

import JustWho.client.ChuckClient;
import JustWho.dto.ChuckJokeDTO;
import JustWho.dto.api.JokeDTO;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

public class ChuckService {
  @Inject
  ChuckClient chuckClient;

  public Mono<JokeDTO> getJoke() {
    return chuckClient.fetchJoke().map(chuckJokeDTO -> new JokeDTO(chuckJokeDTO.getValue()));
  }
}
