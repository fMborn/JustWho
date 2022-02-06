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
    final List<Integer> genres;
    @JsonProperty
    final String year;
    @JsonProperty
    final double ranking;

    public SearchDTO(String name, List<Integer> genres, String year, double ranking) {
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

    public static SearchDTO valueOf(String name, List<Integer> genres, String year, double ranking) {
        return new SearchDTO(name, genres, year, ranking);
    }
}
