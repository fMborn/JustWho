package JustWho.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SearchDTO {

    @JsonProperty
    final String name;
    @JsonProperty
    final List<String> genres;
    @JsonProperty
    final int year;
    @JsonProperty
    final int ranking;

    public SearchDTO(String name, List<String> genres, int year, int ranking) {
        this.name = name;
        this.genres = genres;
        this.year = year;
        this.ranking = ranking;
    }
}
