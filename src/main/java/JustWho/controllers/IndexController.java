package JustWho.controllers;

import JustWho.dto.api.IndexRequestDTO;
import JustWho.dto.index.SearchDTO;
import JustWho.services.ChuckService;
import JustWho.services.IndexService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller("/index")
public class IndexController {
    @Inject
    IndexService indexService;
    @Inject
    ChuckService chuckService;

    @Post("/fill")
    public CompletableFuture<String> index(@Body @Valid List<IndexRequestDTO> movies) throws Exception{

        final List<SearchDTO> searchDTOS = movies.stream().map(movie -> new SearchDTO(movie.getName(), movie.getGenres(), movie.getYear(), movie.getRanking())).collect(Collectors.toList());
        return indexService.fillIndex(searchDTOS);
    }
}