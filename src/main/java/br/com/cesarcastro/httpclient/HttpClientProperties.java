package br.com.cesarcastro.httpclient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "cc.http.client")
public record HttpClientProperties(
        @DefaultValue("10000") Integer connectionRequestTimeout,  // tempo de espera para pegar conexão do pool
        @DefaultValue("10000") Integer connectTimeout,            // tempo para conexão TCP
        @DefaultValue("10000") Integer socketTimeout              // tempo de leitura do socket
) {
}
