package JustWho.dto.collector;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.util.Date;
import java.util.List;

@Introspected
public class SingleMovieData {

    @JsonProperty("genre_ids")
    List<Integer> genreIds;
    @JsonProperty("title")
    String title;
    @JsonProperty("original_title")
    String originalTitle;
    @JsonProperty("original_language")
    String originalLanguage;
    @JsonProperty("overview")
    String overview;
    @JsonProperty("vote_average")
    double voteAverage;
    @JsonProperty("id")
    String movieId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("release_date")
    Date releaseDate;
    @JsonProperty("backdrop_path")
    String backdropPath;
    @JsonProperty("poster_path")
    String posterPath;
    public String getBackdropPath() {
        return backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getMovieId() {
        return movieId;
    }

    public List<Integer> getGenres() {
        return genreIds;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }
}
