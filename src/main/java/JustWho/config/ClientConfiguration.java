package JustWho.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.http.client.HttpClientConfiguration;
import io.micronaut.runtime.ApplicationConfiguration;

import java.net.URI;

@ConfigurationProperties(ClientConfiguration.PREFIX)
public class ClientConfiguration extends HttpClientConfiguration {
  public static final String PREFIX = "hallo";

  private final ClientConnectionPoolConfiguration connectionPoolConfiguration;


  private URI uri;
  private String placeholder;
  private String path;

  public URI getUri() {
    return uri;
  }

  public void setUri(URI uri) {
    this.uri = uri;
  }

  public String getPlaceholder() {
    return placeholder;
  }

  public void setPlaceholder(String placeholder) {
    this.placeholder = placeholder;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public ClientConfiguration(ApplicationConfiguration applicationConfiguration, ClientConnectionPoolConfiguration connectionPoolConfiguration) {
    super(applicationConfiguration);
    this.connectionPoolConfiguration = connectionPoolConfiguration;
  }

  @Override
  public ConnectionPoolConfiguration getConnectionPoolConfiguration() {
    return connectionPoolConfiguration;
  }

  @ConfigurationProperties(ConnectionPoolConfiguration.PREFIX)
  public static class ClientConnectionPoolConfiguration extends ConnectionPoolConfiguration {
  }
}
