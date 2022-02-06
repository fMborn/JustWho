package JustWho.controllers;

import JustWho.dto.index.SearchDTO;
import JustWho.dto.tmdb.ResultContainer;
import JustWho.services.ChuckNorrisService;
import JustWho.services.IndexService;
import JustWho.util.Constants;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.CompletableFuture;

@Controller("/chuck")
public class ChuckController {

    @Inject
    ChuckNorrisService chuckNorrisService;

    @Inject
    IndexService indexerService;

    @Get(uri = "/random", produces = MediaType.APPLICATION_JSON_STREAM)
    boolean fetchReleases() {
        Publisher<ResultContainer> punisher = chuckNorrisService.getRandomJoke();
        CompletableFuture<ResultContainer> resultContainerFuture = new CompletableFuture<>();
        punisher.subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscription.request(1);
                System.out.println("ok lets go");
            }

            @Override
            public void onNext(ResultContainer resultContainer) {
                resultContainerFuture.complete(resultContainer);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                System.out.println("ja fett");
            }
        });
        resultContainerFuture.thenApply(resultContainer -> {
                             resultContainer.getResults().forEach(result -> {
                                 final SearchDTO searchDTO = SearchDTO.valueOf(result.getTitle(), result.getGenreIds(), result.getReleaseDate(), result.getVoteAverage());
                                 indexerService.sendToIndex(Constants.SEARCH_INDEX, searchDTO);
                             });
                             return resultContainer;
                         }
                        );

        return resultContainerFuture.isDone();
    }
}