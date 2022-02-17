package JustWho.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class JokeDTO {
  @JsonProperty
  String joke;

  public JokeDTO(String joke) {
    this.joke = joke;
  }
}
