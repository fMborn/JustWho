package JustWho.services;

import JustWho.dto.api.SearchResponseDTO;
import JustWho.dto.search.*;
import JustWho.util.Constants;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import co.elastic.clients.elasticsearch.core.search.Suggestion;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class SearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);
    @Inject
    ElasticsearchAsyncClient client;

    public CompletableFuture<SearchResponseDTO> search() {
        /*
        maybe interesting for many data points
                “aggs”: {
          "SAMPLE": {
            "sampler": {
              "shard_size": 100
            },
            "aggs": {...}
          }
        }
         */

       final SearchRequest searchRequest = new SearchRequest.Builder()
                .query(q -> q.matchAll(m -> m))
                .index(Constants.SEARCH_INDEX)
                .size(0)
                .aggregations(createAggregations())
                .explain(false)
                .build();

       final CompletableFuture<SearchResponse<SearchResultDTO>> searchResponseFuture = client.search(searchRequest, SearchResultDTO.class);

        return searchResponseFuture
                .thenApply(this::parseResponse);
    }


    private Map<String, Aggregation> createAggregations() {
        final Map<String, Aggregation> aggregationMap = new HashMap<>();
        // genre aggregations
        aggregationMap.put("genres_aggs", Aggregation.of(a -> a.terms(t -> t.field("genres.keyword"))));

        // year aggregations
        final List<AggregationRange> aggregationRanges = List.of(
                AggregationRange.of(ag -> ag.from("1900").to("1950")),
                AggregationRange.of(ag -> ag.from("1950").to("1970")),
                AggregationRange.of(ag -> ag.from("1970").to("1990")),
                AggregationRange.of(ag -> ag.from("1990").to("2020"))
        );
        aggregationMap.put("year_aggs", Aggregation.of(a -> a.range(t -> t.field("year").ranges(aggregationRanges))));

        return aggregationMap;
    }

    public SearchResponseDTO parseResponse(final SearchResponse<SearchResultDTO> searchResponse) {

        final List<SearchResultDTO> searchResults = searchResponse.hits().hits().stream().map(Hit::source).collect(Collectors.toList());

        final List<SearchAggregationResultDTO> aggregationResults = searchResponse
                .aggregations()
                .entrySet()
                .stream().map(entry -> new SearchAggregationResultDTO(entry.getKey(), parseAggregations(entry.getValue())))
                .collect(Collectors.toList());

        return new SearchResponseDTO(searchResults, aggregationResults);
    }
    public List<SearchAggregationBucketResultDTO> parseAggregations(Aggregate aggregate) {
        if (aggregate.isSterms()){
            return aggregate.sterms()
                    .buckets().array()
                    .stream().map(bucket -> new SearchAggregationBucketResultDTO(bucket.key(), bucket.docCount()))
                    .collect(Collectors.toList());
        } else if (aggregate.isRange()) {
            return aggregate.range()
                    .buckets().array()
                    .stream().map(bucket -> new SearchAggregationBucketResultDTO(bucket.key(), bucket.docCount()))
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }

    }

    public CompletableFuture<List<SuggestResultDTO>> suggest(final String input) {
        final String suggestKey = "name_suggestions";
        final Suggester suggester = Suggester.of(builder -> builder.text(input)
                .suggesters(suggestKey, s -> s.completion(c -> c.field("suggest_name").skipDuplicates(true))));

        final SearchRequest searchRequest = new SearchRequest.Builder()
                .suggest(suggester)
                .source(sb -> sb.filter(sf -> sf.excludes("suggest_name")))
                .size(7)
                .index(Constants.SUGGEST_INDEX)
                .explain(false)
                .build();

        final CompletableFuture<SearchResponse<SuggestResultDTO>> responseFuture = client.search(searchRequest, SuggestResultDTO.class);

        return responseFuture.thenApply(searchResponse -> getSuggestions(searchResponse, suggestKey));
    }

    private <TDocument> List<TDocument> getSuggestions(SearchResponse<TDocument> searchResponse, final String suggestKey) {
        return resolveList(searchResponse.suggest().get(suggestKey));
    }

    private <TDocument> List<TDocument> resolveList(List<Suggestion<TDocument>> suggestionList) {
        return suggestionList.stream().flatMap(b -> b.completion().options().stream().map(CompletionSuggestOption::source)).collect(Collectors.toList());
    }
}
