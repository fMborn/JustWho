package JustWho.services;

import JustWho.dto.api.SearchResponseDTO;
import JustWho.dto.search.response.SearchAggregationBucketResultDTO;
import JustWho.dto.search.response.SearchAggregationResultDTO;
import JustWho.dto.search.response.SearchResultDTO;
import JustWho.dto.search.response.SuggestResultDTO;
import JustWho.util.Constants;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import co.elastic.clients.elasticsearch.core.search.Suggestion;

import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Inject;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class SearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);
    @Inject
    ElasticsearchAsyncClient client;

    public CompletableFuture<SearchResponseDTO> search(
            String input,
            List<String> activeGenres,
            @Nullable String startingYear,
            @Nullable String stoppingYear,
            @Nullable String minScore,
            @Nullable String maxScore
    ) {
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

        final SearchRequest.Builder searchRequest = new SearchRequest.Builder()
                .query(buildBoolQuery(input, startingYear, stoppingYear))
                .index(Constants.SEARCH_INDEX)
                .size(20)
                .aggregations(createAggregations())
                .explain(false);


        final var postFilters = buildPostFilters(activeGenres, minScore, maxScore);
        if (!postFilters.isEmpty()) {
            searchRequest.postFilter(f -> f.bool(b -> b.filter(postFilters)));
        }

        final long startTime = System.currentTimeMillis();
        final CompletableFuture<SearchResponse<SearchResultDTO>> searchResponseFuture = client.search(searchRequest.build(), SearchResultDTO.class);
        return searchResponseFuture
                .thenApply(resp -> {
                    LOGGER.info("Elastic took: " + (System.currentTimeMillis() - startTime) + "ms server time: " + resp.took() + "ms");
                    return parseResponse(resp);
                });
    }

    private Query buildBoolQuery(String input, String startingYear, String stoppingYear) {

        final var boolBuilder = new BoolQuery.Builder();

        final var multiMatch = MultiMatchQuery.of(m -> m.query(input).fields(List.of("genres", "overview", "originalTitle", "title")));
        boolBuilder.must(m -> m.multiMatch(multiMatch));

        if (startingYear != null || stoppingYear != null) {
            boolBuilder.filter(f -> f.range(RangeQuery.of(t -> t.field("year").from(startingYear).to(stoppingYear))));
        }

        return new Query.Builder().bool(boolBuilder.build()).build();
    }

    private List<Query> buildPostFilters(List<String> activeGenres, String minScore, String maxScore) {
        final List<Query> queries = new ArrayList<>();
        final var genreFieldValues = activeGenres.stream().map(FieldValue::of).collect(Collectors.toList());

        // vote average post filter
        if (minScore != null || maxScore != null) {
            queries.add(new Query.Builder().range(r -> r.field("voteAverage").from("3").to("9")).build());
        }

        // active genres post filter
        if (!activeGenres.isEmpty()) {
            queries.add( new Query.Builder().terms(t -> t.field("genres").terms(v -> v.value(genreFieldValues))).build());
        }

        return queries;

    }



    private Map<String, Aggregation> createAggregations() {
        final Map<String, Aggregation> aggregationMap = new HashMap<>();
        // genre aggregations
        aggregationMap.put("Genres", Aggregation.of(a -> a.terms(t -> t.field("genres"))));

        // year aggregations
//        final List<AggregationRange> aggregationRanges = List.of(
//                AggregationRange.of(ag -> ag.from("1900").to("1950")),
//                AggregationRange.of(ag -> ag.from("1950").to("1970")),
//                AggregationRange.of(ag -> ag.from("1970").to("1990")),
//                AggregationRange.of(ag -> ag.from("1990").to("2020"))
//        );
//        aggregationMap.put("year_aggs", Aggregation.of(a -> a.range(t -> t.field("year").ranges(aggregationRanges))));

        aggregationMap.put("Years", Aggregation.of(a -> a.variableWidthHistogram(v -> v.field("year").buckets(6))));

        return aggregationMap;
    }

    public SearchResponseDTO parseResponse(final SearchResponse<SearchResultDTO> searchResponse) {

        final List<SearchResultDTO> searchResults = searchResponse.hits().hits().stream().map(Hit::source).collect(Collectors.toList());

        final List<SearchAggregationResultDTO> aggregationResults = searchResponse
                .aggregations()
                .entrySet()
                .stream().map(entry -> new SearchAggregationResultDTO(entry.getKey(), parseAggregations(entry.getValue())))
                .collect(Collectors.toList());

        final long hits = searchResponse.hits().total() != null ? searchResponse.hits().total().value() : 0;
        return new SearchResponseDTO(hits, searchResults, aggregationResults);
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
        } else if (aggregate.isVariableWidthHistogram()) {
            return aggregate.variableWidthHistogram()
                    .buckets().array()
                    .stream().map(bucket -> new SearchAggregationBucketResultDTO(bucket.min(), bucket.max(), bucket.docCount()))
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }

    }

    public CompletableFuture<List<SuggestResultDTO>> suggest(final String input) {
        final String suggestKey = "name_suggestions";
        final Suggester suggester = Suggester.of(builder -> builder.text(input)
                .suggesters(suggestKey, s -> s.completion(c -> c.field("suggestTitle").skipDuplicates(true))));

        final SearchRequest searchRequest = new SearchRequest.Builder()
                .suggest(suggester)
                .source(sb -> sb.filter(sf -> sf.excludes("suggestTitle")))
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
