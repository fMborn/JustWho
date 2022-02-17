package JustWho.dto.index;

import JustWho.dto.collector.SingleMovieData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    final double ranking;

    public SearchDTO(String name, List<String> genres, int year, double ranking) {
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

    public static SearchDTO of(final SingleMovieData movieData, final Map<Integer, String> genreMapping) {
        final List<String> genres = movieData
                .getGenres().stream()
                .map(genreId -> Optional.ofNullable(genreMapping.get(genreId)))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(movieData.getRealeaseDate());

        return new SearchDTO(movieData.getTitle(), genres, calendar.get(Calendar.YEAR), movieData.getVoteAverage());
    }
}
