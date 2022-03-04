package JustWho.dto.collector;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Introspected
public class CreditsContainer {

    @JsonProperty("genres")
    List<Genre> genres;

    public List<Genre> getGenres() {
        return genres;
    }

    public Map<Integer, String> getGenreMapping() {
        return genres.stream().collect(Collectors.toMap(Genre::getId, Genre::getName));
    }
}
