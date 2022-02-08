package JustWho.dto.search;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuggestResultDTODeserializer extends StdDeserializer<SuggestResultDTO> {

    public SuggestResultDTODeserializer() {
        this(null);
    }

    public SuggestResultDTODeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public SuggestResultDTO deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        JsonNode node = ctxt.readTree(jp);
        final List<String> genres = new ArrayList<>();
        if (node.get("genres") != null && node.get("genres").isArray()) {
            for (JsonNode bla: node.get("genres")) {
                genres.add(bla.asText());
            }
        }
        int year = node.get("year").asInt();
        String name = node.get("name").asText();
        int ranking = node.get("ranking").asInt();
        return new SuggestResultDTO(name, year, List.of());
    }
}
