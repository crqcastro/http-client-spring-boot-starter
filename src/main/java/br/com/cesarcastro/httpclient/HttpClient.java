package br.com.cesarcastro.httpclient;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public interface HttpClient {

    String delete(String url, HttpHeaders httpHeaders, String certificateName);

    String get(String url, HttpHeaders httpHeaders, String certificateName);

    String post(String url, HttpHeaders httpHeaders, String body, String certificateName);

    String post(String url, HttpHeaders httpHeaders, HttpEntity<?> entity, String certificateName);

    String put(String url, HttpHeaders httpHeaders, String body, String certificateName);

}
