package JustWho.dto.search;

import JustWho.dto.index.Indexable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.util.List;

public class SearchResponseDTO {

    @JsonIgnore
    List<SearchResultDTO> searchResultDTOS;
    @JsonIgnore
    List<SearchAggregationResultDTO> searchAggregationResultDTOS;
}
