package JustWho.dto.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(using = SearchResultDTODeserializer.class)
public class SuggestResultDTO {

    @JsonProperty
    final String name;
    @JsonProperty
    final int year;
    @JsonProperty("suggest_name")
    final List<String> suggestName;

    public SuggestResultDTO(String name, int year, List<String> suggestName) {
        this.name = name;
        this.year = year;
        this.suggestName = suggestName;
    }
}
