package JustWho.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChuckJokeDTO {

  private final List<Object> categories;
  private final String iconUrl;
  private final String id;
  private final String createdAt;
  private final String updatedAt;
  private final String url;
  private final String value;

  @JsonCreator
  public ChuckJokeDTO(
      @JsonProperty("categories") List<Object> categories,
      @JsonProperty("created_at") String createdAt,
      @JsonProperty("icon_url") String iconUrl,
      @JsonProperty("id") String id,
      @JsonProperty("updated_at") String updatedAt,
      @JsonProperty("url") String url,
      @JsonProperty("value") String value
                     ) {
    this.categories = categories;
    this.createdAt = createdAt;
    this.iconUrl = iconUrl;
    this.id = id;
    this.updatedAt = updatedAt;
    this.url = url;
    this.value = value;
  }

  public List<Object> getCategories() {
    return categories;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getIconUrl() {
    return iconUrl;
  }

  public String getId() {
    return id;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public String getUrl() {
    return url;
  }

  public String getValue() {
    System.out.println(value);
    return value;
  }
}