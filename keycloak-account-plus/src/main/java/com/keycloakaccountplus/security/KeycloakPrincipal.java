package com.keycloakaccountplus.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Collection;
import java.util.Map;

public class KeycloakPrincipal implements OAuth2AuthenticatedPrincipal {

    private final String subject;
    private final Map<String, Object> attributes;
    private final Collection<GrantedAuthority> authorities;

    public KeycloakPrincipal(String subject, Map<String, Object> attributes,
                            Collection<GrantedAuthority> authorities) {
        this.subject = subject;
        this.attributes = attributes;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return subject;
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String name) {
        return (T) attributes.get(name);
    }

    public String getSubject() {
        return subject;
    }

    public String getPreferredUsername() {
        return getAttribute("preferred_username");
    }

    public String getEmail() {
        return getAttribute("email");
    }

    public Boolean getEmailVerified() {
        return getAttribute("email_verified");
    }

    public String getGivenName() {
        return getAttribute("given_name");
    }

    public String getFamilyName() {
        return getAttribute("family_name");
    }
}
