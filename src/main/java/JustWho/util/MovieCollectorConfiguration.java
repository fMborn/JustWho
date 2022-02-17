package JustWho.util;

import io.micronaut.context.annotation.ConfigurationProperties;


@ConfigurationProperties("collector")
public class MovieCollectorConfiguration {


    private String url;
    private String apiKey;
    private int startingYear;
    private int stoppingYear;


    public void setStartingYear(int startingYear) {
        this.startingYear = startingYear;
    }

    public void setStoppingYear(int stoppingYear) {
        this.stoppingYear = stoppingYear;
    }

    public int getStartingYear() {
        return startingYear;
    }

    public int getStoppingYear() {
        return stoppingYear;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
