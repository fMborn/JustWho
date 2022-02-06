package JustWho.controllers;

import JustWho.services.IndexService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

import java.util.concurrent.CompletableFuture;

@Controller("/index")
public class IndexController {

    @Inject
    IndexService indexService;

    @Get("/fill")
    public CompletableFuture<String> index() throws Exception{
        return indexService.fillIndex();
    }
}