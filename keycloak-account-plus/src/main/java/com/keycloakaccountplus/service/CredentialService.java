package com.keycloakaccountplus.service;

import com.keycloakaccountplus.dto.response.CredentialResponse;
import com.keycloakaccountplus.exception.ResourceNotFoundException;
import com.keycloakaccountplus.security.SecurityContextService;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CredentialService {

    private static final Logger log = LoggerFactory.getLogger(CredentialService.class);

    private final KeycloakAdminService keycloakAdmin;
    private final SecurityContextService securityContext;

    public CredentialService(KeycloakAdminService keycloakAdmin, SecurityContextService securityContext) {
        this.keycloakAdmin = keycloakAdmin;
        this.securityContext = securityContext;
    }

    public List<CredentialResponse> getCurrentUserCredentials() {
        String userId = securityContext.getCurrentUserId();
        log.debug("Getting credentials for user: {}", userId);

        List<CredentialRepresentation> credentials = keycloakAdmin.getCredentials(userId);

        return credentials.stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CredentialResponse> getCurrentUserTotpCredentials() {
        return getCurrentUserCredentials().stream()
                .filter(c -> "otp".equals(c.type()))
                .toList();
    }

    public void removeTotp(String credentialId) {
        String userId = securityContext.getCurrentUserId();
        log.debug("Removing TOTP credential {} for user {}", credentialId, userId);

        // Verify credential is TOTP and belongs to user
        List<CredentialRepresentation> credentials = keycloakAdmin.getCredentials(userId);

        CredentialRepresentation totp = credentials.stream()
                .filter(c -> c.getId().equals(credentialId))
                .filter(c -> "otp".equals(c.getType()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("TOTP credential not found: " + credentialId));

        keycloakAdmin.removeCredential(userId, credentialId);
        log.info("Removed TOTP credential {} for user {}", credentialId, userId);
    }

    private CredentialResponse toResponse(CredentialRepresentation credential) {
        Instant createdAt = null;
        if (credential.getCreatedDate() != null) {
            createdAt = Instant.ofEpochMilli(credential.getCreatedDate());
        }

        return new CredentialResponse(
                credential.getId(),
                credential.getType(),
                credential.getUserLabel(),
                createdAt
        );
    }
}
