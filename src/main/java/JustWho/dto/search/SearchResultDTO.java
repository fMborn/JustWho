package JustWho.dto.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

//@JsonDeserialize(using = SearchResultDTODeserializer.class)
public class SearchResultDTO {

    @JsonIgnore
    String id = "";
    @JsonProperty("name")
    String name = "";
    @JsonProperty("genres")
     List<String> genres = List.of("");
    @JsonProperty("year")
    int year = 0;
    @JsonProperty("ranking")
    int ranking = 0;

}

