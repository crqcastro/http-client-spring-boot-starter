package br.com.cesarcastro.httpclient;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.ContextConsumer;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpClientTest {
    private static final Logger log = LoggerFactory.getLogger(HttpClientTest.class);
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    RestClientAutoConfiguration.class,
                    HttpClientServiceConfiguration.class
            ));

    private void runWithContext(ContextConsumer<ApplicationContext> assertions) {
        contextRunner.run(assertions);
    }

    @Test
    void shouldContainBeans() {
        runWithContext(context -> {
            log.info("Beans: {}", context.getBeanDefinitionNames());
            assertTrue(context.containsBean("restTemplate"));
            assertTrue(context.containsBean("httpClient"));
            assertTrue(context.containsBean("cc.http.client-br.com.cesarcastro.httpclient.HttpClientProperties"));
        });
    }

    @Test
    void shouldContainHttpClientProperties() {
        runWithContext(context -> {
            HttpClientProperties properties = context.getBean(HttpClientProperties.class);
            assertTrue(properties.connectionRequestTimeout() > 0);
            assertTrue(properties.connectTimeout() > 0);
            assertTrue(properties.socketTimeout() > 0);
        });
    }

    @Test
    void shouldHttpClientWithoutSSLContext() {
        runWithContext((context) -> {
            HttpClient httpClient = context.getBean(HttpClient.class);
            String response = httpClient.get("https://jsonplaceholder.typicode.com/todos", null, null);
            assertNotNull(response);
        });
    }
}
