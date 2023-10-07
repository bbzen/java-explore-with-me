package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StatClient {
    private static final String API_POST_PREFIX = "/hit";
    private final RestTemplate rest;

    @Autowired
    public StatClient(@Value("${ewm-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void postHit(EndpointHitDto dto) {
        ResponseEntity<Object> response =  makeAndSendRequest(HttpMethod.POST, API_POST_PREFIX, null, dto);
        log.debug("Client post method response is: " + response.getStatusCode());
    }

    public ResponseEntity<Object> getStats(Map<String, Object> parameters) {
        String getPrefix = "/stats?start={start}&end={end}&unique={unique}";
        if (parameters.containsKey("uris")) {
            getPrefix = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        }
        ResponseEntity<Object> response = makeAndSendRequest(HttpMethod.GET, getPrefix, parameters, null);
        log.debug("Client post method response is: " + response.getStatusCode());
        return response;
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> ewmServerResponse;
        try {
            if (parameters != null) {
                ewmServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                ewmServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareClientResponse(ewmServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareClientResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
