package JustWho.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SuggestDTO {

    @JsonProperty
    final String name;
    @JsonProperty
    final int year;
    @JsonProperty("suggest_name")
    final List<String> suggestName;

    public SuggestDTO(String name, int year) {
        this.name = name;
        this.year = year;
        this.suggestName = createSuggestName(name);
    }

    private List<String> createSuggestName(final String name) {
        final List<String> list = Arrays.asList(name.split(" "));
        Collections.reverse(list);
        return list.stream()
                .reduce(new ArrayList<String>(), (res, element) -> concatLists(res, concatElements(res, element)), this::concatLists);
    }

    private ArrayList<String> concatLists(final ArrayList<String> a, final ArrayList<String> b) {
        return new ArrayList(Stream.concat(a.stream(), b.stream()).collect(Collectors.toList()));
    }

    private ArrayList<String> concatElements(final List<String> list, final String elm) {
        final String lastElement = list.size() > 0 ? list.get(list.size() - 1) : "";
        return new ArrayList(List.of(elm + " " + lastElement));
    }
}
