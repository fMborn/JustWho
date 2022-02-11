package JustWho.dto.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SearchAggregationResultDTO {

    @JsonProperty("aggregation_name")
    final String aggregationName;

    @JsonProperty("aggregation_values")
    final List<SearchAggregationBucketResultDTO> aggregationBucketResultDTOS;

    public SearchAggregationResultDTO(String aggregationName, List<SearchAggregationBucketResultDTO> aggregationBucketResultDTOS) {
        this.aggregationName = aggregationName;
        this.aggregationBucketResultDTOS = aggregationBucketResultDTOS;
    }

}

