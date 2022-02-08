package JustWho.services;

import JustWho.dto.search.SearchResultDTO;
import JustWho.dto.search.SuggestResultDTO;
import JustWho.util.Constants;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggester;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class SearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);
    @Inject
    ElasticsearchAsyncClient client;


    public CompletableFuture<List<SearchResultDTO>> search() throws IOException {
        final SearchRequest searchRequest = new SearchRequest.Builder()
                .query(q -> q.matchAll(m -> m))
                .index(Constants.SEARCH_INDEX)
                .explain(false)
                .build();

        return client.search(searchRequest, SearchResultDTO.class)
                .thenApply(response -> response.hits().hits())
                .thenApply(hits -> hits.stream().map(h -> h.source()).collect(Collectors.toList()));

    }


    public CompletableFuture<SearchResponse<SuggestResultDTO>> suggest(final String input) throws IOException {
        final CompletionSuggester suggester = new CompletionSuggester.Builder()
                .field("suggest_name")
                .prefix(input)
                .skipDuplicates(true)
                .build();

        final Suggester suggester1 = new Suggester.Builder().suggesters("", s -> s.completion(suggester)).build();
        final SearchRequest searchRequest = new SearchRequest.Builder()
                // s -> s.suggesters("", FieldSuggester.of(fs -> fs.completion(c -> c.field("suggest_name").prefix(input).skipDuplicates(true)))).build()
                .suggest(suggester1)
                .index(Constants.SUGGEST_INDEX)
                .explain(false)
                .build();

        return client.search(searchRequest, SuggestResultDTO.class);

    }
}
