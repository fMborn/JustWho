package JustWho.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChuckNorrisJokeDTO {
    @JsonProperty("icon_url")
    private String iconUrl;

    @JsonProperty("id")
    private String id;

    @JsonProperty("url")
    private String url;

    @JsonProperty("value")
    private String value;

    public ChuckNorrisJokeDTO() {
    }

    public ChuckNorrisJokeDTO(String iconUrl, String id, String url, String value) {
        this.iconUrl = iconUrl;
        this.id = id;
        this.url = url;
        this.value = value;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getValue() {
        return value;
    }
}
