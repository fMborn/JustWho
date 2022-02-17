package JustWho.dto.api;

import JustWho.dto.index.Indexable;
import JustWho.dto.search.SearchAggregationResultDTO;
import JustWho.dto.search.SearchResultDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.util.List;

public class SearchResponseDTO {

    @JsonProperty("search_results")
    List<SearchResultDTO> searchResultDTOS;

    @JsonProperty("aggregations")
    List<SearchAggregationResultDTO> searchAggregationResultDTOS;

    public SearchResponseDTO(List<SearchResultDTO> searchResultDTOS, List<SearchAggregationResultDTO> searchAggregationResultDTOS) {
        this.searchResultDTOS = searchResultDTOS;
        this.searchAggregationResultDTOS = searchAggregationResultDTOS;
    }

}
