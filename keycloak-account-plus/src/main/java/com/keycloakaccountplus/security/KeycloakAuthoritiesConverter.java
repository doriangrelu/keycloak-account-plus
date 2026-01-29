package com.keycloakaccountplus.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class KeycloakAuthoritiesConverter {

    private static final Logger log = LoggerFactory.getLogger(KeycloakAuthoritiesConverter.class);

    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Map<String, Object> claims) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Extract realm_access.roles
        Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
        if (realmAccess != null) {
            List<String> roles = (List<String>) realmAccess.get("roles");
            if (roles != null) {
                roles.forEach(role -> {
                    String authority = "ROLE_" + role.toUpperCase();
                    authorities.add(new SimpleGrantedAuthority(authority));
                    log.debug("Added realm role: {}", authority);
                });
            }
        }

        // Extract resource_access.{client}.roles (client roles)
        Map<String, Object> resourceAccess = (Map<String, Object>) claims.get("resource_access");
        if (resourceAccess != null) {
            resourceAccess.forEach((clientId, clientAccess) -> {
                if (clientAccess instanceof Map) {
                    Map<String, Object> access = (Map<String, Object>) clientAccess;
                    List<String> clientRoles = (List<String>) access.get("roles");
                    if (clientRoles != null) {
                        clientRoles.forEach(role -> {
                            String authority = "ROLE_" + clientId.toUpperCase() + "_" + role.toUpperCase();
                            authorities.add(new SimpleGrantedAuthority(authority));
                            log.debug("Added client role: {}", authority);
                        });
                    }
                }
            });
        }

        // Add scope-based authorities
        String scope = (String) claims.get("scope");
        if (scope != null) {
            Arrays.stream(scope.split(" "))
                    .filter(s -> !s.isBlank())
                    .forEach(s -> {
                        String authority = "SCOPE_" + s;
                        authorities.add(new SimpleGrantedAuthority(authority));
                        log.debug("Added scope: {}", authority);
                    });
        }

        return authorities;
    }
}
