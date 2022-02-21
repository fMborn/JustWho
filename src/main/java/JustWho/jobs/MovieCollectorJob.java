package JustWho.jobs;

import JustWho.dto.index.SearchDTO;
import JustWho.services.IndexService;
import JustWho.services.MovieCollectorService;
import JustWho.util.MovieCollectorConfiguration;
import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.scheduling.annotation.Schedules;
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
    @Inject
    IndexService indexService;

    //@Scheduled(fixedDelay = "1d", initialDelay = "5h")
    void executeEveryDay() {
        final int startingYear = movieCollectorConfiguration.getStartingYear();
        final int stoppingYear = movieCollectorConfiguration.getStoppingYear();
        LOGGER.info("Starting import job for years " + startingYear + " - " + stoppingYear);
        // load all movies for config
        Flux<SearchDTO> searchDTOFlux = movieCollectorService
                .fetchAllMoviesForRange(movieCollectorConfiguration.getStartingYear(),  movieCollectorConfiguration.getStoppingYear());

        // subscription starts flux
        searchDTOFlux.buffer(20).subscribe(searchDTOs -> indexService.fillIndex(searchDTOs));

        LOGGER.info("Finished import job");
    }

}
