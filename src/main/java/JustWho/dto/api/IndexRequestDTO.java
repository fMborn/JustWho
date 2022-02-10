package JustWho.dto.api;

import JustWho.dto.index.Indexable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.util.List;


@Introspected
public class IndexRequestDTO {

    @JsonProperty
    String name;
    @JsonProperty
    List<String> genres;
    @JsonProperty
    int year;
    @JsonProperty
    int ranking;

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public List<String> getGenres() {
        return genres;
    }

    public int getRanking() {
        return ranking;
    }
}
