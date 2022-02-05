package JustWho;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import io.micronaut.context.annotation.Factory;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.elasticsearch.DefaultElasticsearchConfigurationProperties;
import jakarta.inject.Inject;
import jakarta.inject.Qualifier;
import jakarta.inject.Singleton;
import org.apache.http.HttpHost;
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


            RestClient restClient = RestClient.builder(elasticConfig.getHttpHosts()).build();

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
