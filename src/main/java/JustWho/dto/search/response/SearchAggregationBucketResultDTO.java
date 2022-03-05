package JustWho.dto.search.response;

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

    public SearchAggregationBucketResultDTO(double min, double max, long value) {
        if (min == max) {
            this.name = Double.toString(min);
        } else {
            this.name = min + "-" + max;
        }
        this.value = value;
    }

}

