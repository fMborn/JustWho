package JustWho.dto.collector;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Introspected
public class CreditsContainer {

    @JsonProperty("id")
    int id;

    @JsonProperty("cast")
    List<Person> cast;

    @JsonProperty("crew")
    List<Person> crew;

    public int getId() {return id;}

    public List<Person> getCast() {return cast;}

    public List<String> getTopCastName() {
        return cast.stream()
                   .limit(10)
                   .map(Person::getName)
                   .collect(toList());
    }

    public List<Person> getCrew() {return crew;}

    public String getDirectorName() {
        return crew.stream()
                   .filter(x -> Objects.equals(x.getJob().toLowerCase(), "director"))
                   .findFirst()
                   .map(Person::getName)
                   .orElse("");
    }
}
