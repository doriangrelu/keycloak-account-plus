package com.keycloakaccountplus.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SecurityContextService {

    public String getCurrentUserId() {
        KeycloakPrincipal principal = getCurrentPrincipal();
        return principal.getSubject();
    }

    public String getCurrentUsername() {
        KeycloakPrincipal principal = getCurrentPrincipal();
        return principal.getPreferredUsername();
    }

    public String getCurrentEmail() {
        KeycloakPrincipal principal = getCurrentPrincipal();
        return principal.getEmail();
    }

    public Set<String> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .map(auth -> auth.substring(5))
                .collect(Collectors.toSet());
    }

    public boolean hasRole(String role) {
        return getCurrentUserRoles().contains(role.toUpperCase());
    }

    public boolean hasAnyRole(String... roles) {
        Set<String> userRoles = getCurrentUserRoles();
        for (String role : roles) {
            if (userRoles.contains(role.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    public KeycloakPrincipal getCurrentPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof KeycloakPrincipal)) {
            throw new IllegalStateException("No authenticated user found");
        }
        return (KeycloakPrincipal) authentication.getPrincipal();
    }
}
