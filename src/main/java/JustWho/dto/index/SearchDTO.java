package JustWho.dto.index;

import JustWho.dto.collector.SingleMovieData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.util.*;
import java.util.stream.Collectors;

@Introspected
public class SearchDTO implements Indexable {

    @JsonIgnore
    String id;
    @JsonProperty
    String title;
    @JsonProperty
    List<String> genres;
    @JsonProperty
    Date realeaseDate;
    @JsonProperty
    String overview;
    @JsonProperty
    double voteAverage;
    @JsonProperty
    String posterPath;
    @JsonProperty
    String backdropPath;
    @JsonProperty
    String originalTitle;
    @JsonProperty
    String originalLanguage;

    public SearchDTO(
            String id,
            String title,
            List<String> genres,
            Date realeaseDate,
            double voteAverage,
            String overview,
            String posterPath,
            String backdropPath,
            String originalTitle,
            String originalLanguage
    ) {
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.realeaseDate = realeaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(realeaseDate);
        return calendar.get(Calendar.YEAR);
    }

    public String getPosterPath() {
        return posterPath;
    }

    public static SearchDTO of(final SingleMovieData movieData, final Map<Integer, String> genreMapping) {
        final List<String> genres = movieData
                .getGenres().stream()
                .map(genreId -> Optional.ofNullable(genreMapping.get(genreId)))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        return new SearchDTO(
                movieData.getMovieId(),
                movieData.getTitle(),
                genres,
                movieData.getRealeaseDate(),
                movieData.getVoteAverage(),
                movieData.getOverview(),
                movieData.getPosterPath(),
                movieData.getBackdropPath(),
                movieData.getOriginalTitle(),
                movieData.getOriginalLanguage()
        );
    }
}
