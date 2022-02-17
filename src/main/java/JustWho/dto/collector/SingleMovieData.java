package JustWho.dto.collector;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.util.Date;
import java.util.List;

@Introspected
public class SingleMovieData {

    @JsonProperty("adult")
    boolean adult;
    @JsonProperty("genre_ids")
    List<Integer> genreIds;
    @JsonProperty("title")
    String title;
    @JsonProperty("overview")
    String overview;
    @JsonProperty("vote_average")
    double voteAverage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("release_date")
    Date realeaseDate;

    public boolean isAdult() {
        return adult;
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

    public Date getRealeaseDate() {
        return realeaseDate;
    }
}
