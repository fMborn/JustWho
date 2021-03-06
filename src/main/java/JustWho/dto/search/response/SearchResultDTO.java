package JustWho.dto.search.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.DecimalMin;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
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
    @JsonProperty("cast")
    private List<String> cast;
    @JsonProperty("castNames")
    private List<String> castNames;
    @JsonProperty("director")
    private String director;
    @JsonProperty("directorName")
    private String directorNames;

    public String getTitle() {return title;}

    public List<String> getGenres() {return genres;}

    public Long getReleaseDate() {return releaseDate;}

    public String getOverview() {return overview;}

    public Double getVoteAverage() {return voteAverage;}

    public String getPosterPath() {return posterPath;}

    public String getBackdropPath() {return backdropPath;}

    public String getOriginalTitle() {return originalTitle;}

    public String getOriginalLanguage() {return originalLanguage;}

    public Integer getYear() {return year;}

    public List<String> getCastNames() {return castNames;}

    public String getDirectorNames() {return directorNames;}

}

