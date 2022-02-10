package JustWho.dto.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class SuggestResultDTO {

    @JsonProperty
    String name;
    @JsonProperty
    int year;
    @JsonProperty("suggest_name")
    List<String> suggestName;

}
