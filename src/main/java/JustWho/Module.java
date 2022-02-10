package JustWho;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.elasticsearch.DefaultElasticsearchConfigurationProperties;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class Module {
    @Factory
    public static class ElasticClientFactory {

        @Inject
        DefaultElasticsearchConfigurationProperties elasticConfig;

        @Replaces(ElasticsearchClient.class)
        @Singleton
        ElasticsearchClient createClient () {


            RestClient restClient = RestClient.builder(elasticConfig.getHttpHosts()).build();

            ElasticsearchTransport transport = new RestClientTransport(
                    restClient, new JacksonJsonpMapper());
            return new ElasticsearchClient(transport);
        }

        @Replaces(ElasticsearchAsyncClient.class)
        @Singleton
        ElasticsearchAsyncClient createAsyncClient () {
            final CredentialsProvider credentialsProvider =
                    new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials("elastic", "f1c8863ac69cc550695b4b9ee4c9496aff24e687f00bc14d36d3a63b0d36f24b299df89238a9b51fe24763287e31ef5fd46b6f391cfe7d4abef00526de90715"));

            RestClient restClient = RestClient.builder(elasticConfig.getHttpHosts()).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                @Override
                public HttpAsyncClientBuilder customizeHttpClient(
                        HttpAsyncClientBuilder httpClientBuilder) {
                    return httpClientBuilder
                            .setDefaultCredentialsProvider(credentialsProvider);
                }
            }).build();

            ElasticsearchTransport transport = new RestClientTransport(
                    restClient, new JacksonJsonpMapper());
            return new ElasticsearchAsyncClient(transport);
        }

        @Replaces(RestHighLevelClient.class)
        @Singleton
        RestHighLevelClient createOldClient () {

            RestClientBuilder restClientBuilder = RestClient.builder(elasticConfig.getHttpHosts());

            return new RestHighLevelClient(restClientBuilder);
        }
    }
}
