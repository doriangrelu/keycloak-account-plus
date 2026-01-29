package com.keycloakaccountplus.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class KeycloakOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private static final Logger log = LoggerFactory.getLogger(KeycloakOpaqueTokenIntrospector.class);

    private final RestTemplate restTemplate;
    private final String introspectionUri;
    private final String clientId;
    private final String clientSecret;
    private final KeycloakAuthoritiesConverter authoritiesConverter;

    public KeycloakOpaqueTokenIntrospector(
            @Value("${spring.security.oauth2.resourceserver.opaquetoken.introspection-uri}") String introspectionUri,
            @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}") String clientId,
            @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}") String clientSecret,
            KeycloakAuthoritiesConverter authoritiesConverter) {
        this.restTemplate = new RestTemplate();
        this.introspectionUri = introspectionUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authoritiesConverter = authoritiesConverter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        log.debug("Introspecting token at: {}", introspectionUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(introspectionUri, request, Map.class);
            Map<String, Object> claims = response.getBody();

            if (claims == null || !Boolean.TRUE.equals(claims.get("active"))) {
                log.debug("Token is not active");
                throw new BadOpaqueTokenException("Token is not active");
            }

            log.debug("Token introspection successful for subject: {}", claims.get("sub"));

            var authorities = authoritiesConverter.convert(claims);

            return new KeycloakPrincipal(
                    (String) claims.get("sub"),
                    claims,
                    authorities
            );
        } catch (BadOpaqueTokenException e) {
            throw e;
        } catch (Exception e) {
            log.error("Token introspection failed", e);
            throw new BadOpaqueTokenException("Token introspection failed: " + e.getMessage());
        }
    }
}
