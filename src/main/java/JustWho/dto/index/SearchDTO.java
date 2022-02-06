package JustWho.dto.index;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SearchDTO implements Indexable {

    @JsonIgnore
    final String id;
    @JsonProperty
    final String name;
    @JsonProperty
    final List<String> genres;
    @JsonProperty
    final int year;
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
}
