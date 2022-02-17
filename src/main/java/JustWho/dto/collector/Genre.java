package JustWho.dto.collector;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class Genre {

    @JsonProperty("id")
    int id;
    @JsonProperty("name")
    String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
