package br.com.cesarcastro.httpclient;

import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@AutoConfiguration
@EnableConfigurationProperties(HttpClientProperties.class)
@RequiredArgsConstructor
public class HttpClientServiceConfiguration {
    private final HttpClientProperties httpClientProperties;
    private final List<RestTemplateCustomizer> customizers;

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(httpClientProperties.connectionRequestTimeout()))
                .setResponseTimeout(Timeout.ofMilliseconds(httpClientProperties.socketTimeout()))
                .build();

        CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(config)
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);

        RestTemplateBuilder builder = new RestTemplateBuilder()
                .requestFactory(() -> factory);

        if (customizers != null) {
            builder = builder.customizers(customizers.toArray(new RestTemplateCustomizer[0]));
        }

        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpClient httpClient(RestTemplateManager restTemplateManager) {
        return new HttpClientImpl(restTemplateManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplateManager restTemplateManager() {
        return new RestTemplateManager(restTemplate());

    }
}
