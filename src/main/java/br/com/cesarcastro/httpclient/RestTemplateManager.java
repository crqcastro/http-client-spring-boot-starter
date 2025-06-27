package br.com.cesarcastro.httpclient;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RestTemplateManager {
    private static final Logger log = LoggerFactory.getLogger(RestTemplateManager.class);

    private final RestTemplate restTemplate;

    public RestTemplate getInstance(String alias) {
        return this.getInstance();
    }

    public RestTemplate getInstance() {
        if (restTemplate != null) {
            return restTemplate;
        }

        log.debug("Instanciou RestTemplate (sem SSL) para o alias.");

        return restTemplate;
    }
}
