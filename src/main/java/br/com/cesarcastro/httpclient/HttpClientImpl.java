package br.com.cesarcastro.httpclient;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;

@Component
@RequiredArgsConstructor
public class HttpClientImpl implements HttpClient {
    private static final Logger log = LoggerFactory.getLogger(HttpClientImpl.class);
    public static final String ERRO_AO_ACESSAR_RECURSO = "Erro ao acessar recurso: {}";

    private final RestTemplateManager restTemplateManager;

    @Override
    public String delete(String url, HttpHeaders httpHeaders, String certificateName) {
        var entity = new HttpEntity<>(httpHeaders);
        return buildRequisition(url, entity, HttpMethod.DELETE, certificateName);
    }

    @Override
    public String get(String url, HttpHeaders httpHeaders, String certificateName) {
        var entity = new HttpEntity<>(httpHeaders);
        return buildRequisition(url, entity, HttpMethod.GET, certificateName);
    }

    @Override
    public String post(String url, HttpHeaders httpHeaders, String body, String certificateName) {
        var entity = new HttpEntity<>(body, httpHeaders);
        return buildRequisition(url, entity, HttpMethod.POST, certificateName);
    }

    @Override
    public String post(String url, HttpHeaders httpHeaders, HttpEntity<?> entity, String certificateName) {
        return buildRequisition(url, entity, HttpMethod.POST, certificateName);
    }

    @Override
    public String put(String url, HttpHeaders httpHeaders, String body, String certificateName) {
        var entity = new HttpEntity<>(body, httpHeaders);
        return buildRequisition(url, entity, HttpMethod.PUT, certificateName);
    }

    private String buildRequisition(String url, HttpEntity<?> entity, HttpMethod httpMethod, String certificateName) {
        try {
            var restTemplate = restTemplateManager.getInstance(certificateName);
            log.debug("Requisicao HTTP: {} - URL: {} - Certificate: {}", httpMethod, url, certificateName);

            var responseEntity = restTemplate.exchange(url, httpMethod, entity, String.class);
            log.debug("Resposta HTTP: {} - URL: {} - Certificate: {}", responseEntity.getStatusCode(), url, certificateName);

            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error(ERRO_AO_ACESSAR_RECURSO, e.getMessage(), e);
            throw e;
        } catch (ResourceAccessException e) {
            log.error(ERRO_AO_ACESSAR_RECURSO, e.getMessage(), e);
            throw new HttpClientErrorException(REQUEST_TIMEOUT, "Erro ao acessar recurso: " + e.getMessage());
        } catch (Exception e) {
            log.error(ERRO_AO_ACESSAR_RECURSO, e.getMessage(), e);
            throw new HttpServerErrorException(INTERNAL_SERVER_ERROR, "Erro ao acessar recurso: " + e.getMessage());
        }
    }
}
