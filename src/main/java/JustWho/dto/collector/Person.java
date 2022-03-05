package JustWho.dto.collector;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {
  @JsonProperty("adult")
  private Boolean adult;
  @JsonProperty("gender")
  private Integer gender;
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("known_for_department")
  private String knownForDepartment;
  @JsonProperty("name")
  private String name;
  @JsonProperty("original_name")
  private String originalName;
  @JsonProperty("popularity")
  private Double popularity;
  @JsonProperty("profile_path")
  private String profilePath;
  @JsonProperty("cast_id")
  private Integer castId;
  @JsonProperty("character")
  private String character;
  @JsonProperty("credit_id")
  private String creditId;
  @JsonProperty("order")
  private Integer order;
  @JsonProperty("department")
  private String department;
  @JsonProperty("job")
  private String job;

  public Boolean getAdult() {return adult;}

  public Integer getGender() {return gender;}

  public Integer getId() {return id;}

  public String getKnownForDepartment() {return knownForDepartment;}

  public String getName() {return name;}

  public String getOriginalName() {return originalName;}

  public Double getPopularity() {return popularity;}

  public String getProfilePath() {return profilePath;}

  public Integer getCastId() {return castId;}

  public String getCharacter() {return character;}

  public String getCreditId() {return creditId;}

  public Integer getOrder() {return order;}

  public String getDepartment() {return department;}

  public String getJob() {return job;}
}
