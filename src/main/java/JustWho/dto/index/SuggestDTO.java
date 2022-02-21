package JustWho.dto.index;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SuggestDTO implements Indexable {

    @JsonIgnore
    final String id;
    @JsonProperty
    final String title;
    @JsonProperty
    final int year;
    @JsonProperty
    final List<String> suggestTitle;
    @JsonProperty
    String posterPath;

    public SuggestDTO(String title, int year, String posterPath) {
        this.id = title + year;
        this.title = title;
        this.year = year;
        this.suggestTitle = createSuggestTitle(title);
        this.posterPath = posterPath;
    }

    private List<String> createSuggestTitle(final String name) {
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

    @Override
    public String getId() {
        return this.id;
    }
}
