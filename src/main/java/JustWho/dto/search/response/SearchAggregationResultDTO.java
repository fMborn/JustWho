package JustWho.dto.search.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SearchAggregationResultDTO {

    @JsonProperty("aggregationName")
    final String aggregationName;

    @JsonProperty("aggregationValues")
    final List<SearchAggregationBucketResultDTO> aggregationBucketResultDTOS;

    public SearchAggregationResultDTO(String aggregationName, List<SearchAggregationBucketResultDTO> aggregationBucketResultDTOS) {
        this.aggregationName = aggregationName;
        this.aggregationBucketResultDTOS = aggregationBucketResultDTOS;
    }

}

