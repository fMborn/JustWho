package JustWho.dto.index;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.util.List;

@Introspected
public class SearchDTO implements Indexable {

    @JsonIgnore
    String id;
    @JsonProperty
    String name;
    @JsonProperty
    List<String> genres;
    @JsonProperty
    int year;
    @JsonProperty
    final int ranking;

    public SearchDTO(String name, List<String> genres, int year, int ranking) {
        this.id = name+year;
        this.name = name;
        this.genres = genres;
        this.year = year;
        this.ranking = ranking;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }
}