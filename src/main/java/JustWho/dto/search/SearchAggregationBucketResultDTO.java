package JustWho.dto.search;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchAggregationBucketResultDTO {

    @JsonProperty("name")
    final String name;
    @JsonProperty("value")
    final long value;

    public SearchAggregationBucketResultDTO(String name, long value) {
        this.name = name;
        this.value = value;
    }

}

