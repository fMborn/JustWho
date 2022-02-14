package JustWho.controllers;

import JustWho.dto.api.IndexRequestDTO;
import JustWho.dto.index.SearchDTO;
import JustWho.services.ChuckService;
import JustWho.services.IndexService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller("/chuck")
public class ChuckController {
    @Inject
    IndexService indexService;
    @Inject
    ChuckService chuckService;

    @Get("/joke")
    Mono<String> joke() {
        return chuckService.bla();
    }
}