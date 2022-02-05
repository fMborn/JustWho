package JustWho.controllers;

import JustWho.services.IndexService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

@Controller("/index")
public class IndexController {

    @Inject
    IndexService indexService;

    @Get("/fill")
    public String index() throws  Exception{
        final String res = indexService.fillIndex();
        return "Hello World";
    }
}