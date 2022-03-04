package JustWho.dto.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.DecimalMin;
import java.util.List;

public class SearchResultDTO {

    @JsonProperty("title")
    private String title;
    @JsonProperty("genres")
    private List<String> genres = null;
    @JsonProperty("releaseDate")
    @DecimalMin("9223372036854775807")
    private Long releaseDate;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("voteAverage")
    private Double voteAverage;
    @JsonProperty("posterPath")
    private String posterPath;
    @JsonProperty("backdropPath")
    private String backdropPath;
    @JsonProperty("originalTitle")
    private String originalTitle;
    @JsonProperty("originalLanguage")
    private String originalLanguage;
    @JsonProperty("year")
    private Integer year;
}

