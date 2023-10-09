package dev.maxuz.vas3ksanta.rest;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class SpringSecurityService {
    private final OAuth2AuthorizedClientService clientService;

    public SpringSecurityService(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    public String getAccessToken(OAuth2AuthenticationToken authentication) {
        if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
            String clientRegistrationId = authentication.getAuthorizedClientRegistrationId();
            OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(clientRegistrationId, authentication.getName());
            return client.getAccessToken().getTokenValue();
        }

        throw new IllegalStateException("Can't get access token");
    }
}
