package JustWho.dto.collector;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotNull;
import java.util.List;

@Introspected
public class MovieDataContainer {

    @JsonProperty("page")
    int page;
    @JsonProperty("results")
    List<SingleMovieData> movies;
    @JsonProperty("total_results")
    int totalResults;
    @JsonProperty("total_pages")
    int totalPages;

    public List<SingleMovieData> getMovies() {
        return movies;
    }

}
