package JustWho;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;

@ConfigurationProperties(TmdbConfiguration.PREFIX)
@Requires(property = TmdbConfiguration.PREFIX)
public class TmdbConfiguration {

  public static final String PREFIX = "tmdb";
  public static final String TMDB_API_URL = "https://api.themoviedb.org/3/";

  public String getPath() {
    return "genre/movie/list?api_key=4713a112c9a269b1a83e49e1367b2661&language=en-US";
  }
}
