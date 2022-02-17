package JustWho.jobs;

import JustWho.dto.index.SearchDTO;
import JustWho.services.MovieCollectorService;
import JustWho.util.MovieCollectorConfiguration;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class MovieCollectorJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieCollectorJob.class);

    @Inject
    MovieCollectorConfiguration movieCollectorConfiguration;

    @Inject
    MovieCollectorService movieCollectorService;

    @Scheduled(fixedDelay = "1d", initialDelay = "5s")
    void executeEveryDay() {

        //Flux<SearchDTO> d = movieCollectorService
         //       .fetchAllMoviesForRange(movieCollectorConfiguration.getStartingYear(),  movieCollectorConfiguration.getStoppingYear());

        //d.subscribeOn()
        //LOGGER.info(d.getName().toString());
    }

}
