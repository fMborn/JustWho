package JustWho.dto.search.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class SuggestResultDTO {

    @JsonProperty
    String title;
    @JsonProperty
    int year;
    @JsonProperty
    String posterPath;

}
