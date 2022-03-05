package JustWho.dto.index;

import JustWho.dto.collector.SingleMovieData;
import JustWho.dto.search.SearchResultDTO;
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
    Date releaseDate;
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
    @JsonProperty
    List<String> castNames;
    @JsonProperty
    String directorName;

    private SearchDTO(
        String id,
        String title,
        List<String> genres,
        Date releaseDate,
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
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
    }

    private SearchDTO(
            String id,
            String title,
            List<String> genres,
            Date releaseDate,
            double voteAverage,
            String overview,
            String posterPath,
            String backdropPath,
            String originalTitle,
            String originalLanguage,
            List<String> castNames,
            String directorName
    ) {
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.castNames = castNames;
        this.directorName = directorName;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return title;
    }

    // TODO Revert quick fix. Did not have time to actually find out what this is and why its done like that. Changed it to make actors index work.
    public int getYear() {
        Optional<Date> dateOpt = Optional.ofNullable(releaseDate);
        return dateOpt.map(date -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.YEAR);
        }).orElse(0);
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
                movieData.getReleaseDate(),
                movieData.getVoteAverage(),
                movieData.getOverview(),
                movieData.getPosterPath(),
                movieData.getBackdropPath(),
                movieData.getOriginalTitle(),
                movieData.getOriginalLanguage()
        );
    }

    public static SearchDTO ofResult(final SearchResultDTO resultDTO, String movieId, List<String> names, String director) {
        Optional<Long> dateOpt = Optional.ofNullable(resultDTO.getReleaseDate());

        return new SearchDTO(
            movieId,
            resultDTO.getTitle(),
            resultDTO.getGenres(),
            dateOpt.map(Date::new).orElse(null),
            resultDTO.getVoteAverage(),
            resultDTO.getOverview(),
            resultDTO.getPosterPath(),
            resultDTO.getBackdropPath(),
            resultDTO.getOriginalTitle(),
            resultDTO.getOriginalLanguage(),
            names,
            director
        );
    }
}
