package JustWho.controllers;

import JustWho.dto.api.JokeDTO;
import JustWho.services.ChuckService;
import JustWho.services.IndexService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

@Controller("/chuck")
public class ChuckController {
    @Inject
    IndexService indexService;
    @Inject
    ChuckService chuckService;

    @Get("/joke")
    Mono<JokeDTO> joke() {
        return chuckService.getJoke();
    }
}