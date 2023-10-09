package dev.maxuz.vas3ksanta.vas3k;

import dev.maxuz.vas3ksanta.vas3k.dto.Vas3kUser;
import dev.maxuz.vas3ksanta.vas3k.dto.Vas3kUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
public class Vas3kApiService {
    private static final Logger log = LoggerFactory.getLogger(Vas3kApiService.class);

    private static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.of(10, ChronoUnit.SECONDS);
    private final RestTemplate restTemplate;
    private final String userInfoUrl;

    public Vas3kApiService(RestTemplateBuilder templateBuilder, @Value("${app.userinfo.url}") String userInfoUrl) {
        this.restTemplate = templateBuilder
            .setConnectTimeout(DEFAULT_REQUEST_TIMEOUT)
            .setReadTimeout(DEFAULT_REQUEST_TIMEOUT)
            .build();
        this.userInfoUrl = userInfoUrl;
    }

    public Vas3kUser getUser(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Vas3kUserResponse> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, requestEntity, Vas3kUserResponse.class);

        Vas3kUserResponse body = response.getBody();
        log.info("Response: {}", body);
        return body == null ? null : body.getUser();
    }

}
