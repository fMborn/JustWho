package JustWho.controllers;

import JustWho.dto.ChuckNorrisJokeDTO;
import JustWho.services.ChuckNorrisService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;

@Controller("/chuck")
public class ChuckController {

    @Inject
    ChuckNorrisService chuckNorrisService;

    @Get(uri = "/random", produces = MediaType.APPLICATION_JSON_STREAM)
    Publisher<ChuckNorrisJokeDTO> fetchReleases() {
        return chuckNorrisService.getRandomJoke();
    }
}