package ru.practicum.stat.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StatClient {
    private static final String API_POST_PREFIX = "/hit";
    private final RestTemplate rest;

    @Autowired
    public StatClient(@Value("${stat.server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void postHit(EndpointHitDto dto) {
        ResponseEntity<List<ViewStatsDto>> response =  makeAndSendRequest(HttpMethod.POST, API_POST_PREFIX, null, dto);
        log.debug("Client post method response is: " + response.getStatusCode());
    }

    public List<ViewStatsDto> getStats(Map<String, Object> parameters) {
        String getPrefix = "/stats?start={start}&end={end}&unique={unique}";
        if (parameters.containsKey("uris")) {
            getPrefix = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        }
        ResponseEntity<List<ViewStatsDto>> response = makeAndSendRequest(HttpMethod.GET, getPrefix, parameters, null);
        log.debug("Client post method response is: " + response.getStatusCode());
        return response.getBody();
    }

    private <T> ResponseEntity<List<ViewStatsDto>> makeAndSendRequest(HttpMethod method, String path, Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<List<ViewStatsDto>> ewmServerResponse;
        try {
            if (parameters != null) {
                ewmServerResponse = rest.exchange(path, method, requestEntity, new ParameterizedTypeReference<List<ViewStatsDto>>() {}, parameters);
            } else {
                ewmServerResponse = rest.exchange(path, method, requestEntity, new ParameterizedTypeReference<List<ViewStatsDto>>() {});
            }
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return prepareClientResponse(ewmServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<List<ViewStatsDto>> prepareClientResponse(ResponseEntity<List<ViewStatsDto>> response) {
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
