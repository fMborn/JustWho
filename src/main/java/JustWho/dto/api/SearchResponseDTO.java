package JustWho.dto.api;

import JustWho.dto.search.response.SearchAggregationResultDTO;
import JustWho.dto.search.response.SearchResultDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class SearchResponseDTO {

    @JsonProperty
    long hits;

    @JsonProperty("searchResults")
    List<SearchResultDTO> searchResultDTOS = Collections.emptyList();

    @JsonProperty("aggregations")
    List<SearchAggregationResultDTO> searchAggregationResultDTOS = Collections.emptyList();

    public SearchResponseDTO(long hits, List<SearchResultDTO> searchResultDTOS, List<SearchAggregationResultDTO> searchAggregationResultDTOS) {
        this.hits = hits;
        this.searchResultDTOS = searchResultDTOS;
        this.searchAggregationResultDTOS = searchAggregationResultDTOS;
    }

    public SearchResponseDTO() {

    }

}
