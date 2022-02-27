package JustWho.util;

import io.micronaut.context.annotation.ConfigurationProperties;


@ConfigurationProperties("collector")
public class MovieCollectorConfiguration {

    private String url;
    private String apiKey;

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
