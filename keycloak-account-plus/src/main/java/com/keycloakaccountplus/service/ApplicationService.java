package com.keycloakaccountplus.service;

import com.keycloakaccountplus.dto.response.ApplicationResponse;
import com.keycloakaccountplus.security.SecurityContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationService.class);

    private final KeycloakAdminService keycloakAdmin;
    private final SecurityContextService securityContext;

    public ApplicationService(KeycloakAdminService keycloakAdmin, SecurityContextService securityContext) {
        this.keycloakAdmin = keycloakAdmin;
        this.securityContext = securityContext;
    }

    @SuppressWarnings("unchecked")
    public List<ApplicationResponse> getConsentedApplications() {
        String userId = securityContext.getCurrentUserId();
        log.debug("Getting consented applications for user: {}", userId);

        List<Map<String, Object>> consents = keycloakAdmin.getConsents(userId);

        return consents.stream()
                .map(this::toResponse)
                .toList();
    }

    public void revokeConsent(String clientId) {
        String userId = securityContext.getCurrentUserId();
        log.debug("Revoking consent for client {} for user {}", clientId, userId);

        keycloakAdmin.revokeConsent(userId, clientId);
        log.info("Revoked consent for client {} for user {}", clientId, userId);
    }

    @SuppressWarnings("unchecked")
    private ApplicationResponse toResponse(Map<String, Object> consent) {
        String clientId = (String) consent.get("clientId");

        // Client info might be nested
        Map<String, Object> client = (Map<String, Object>) consent.get("client");
        String clientName = client != null ? (String) client.get("clientId") : clientId;
        String description = client != null ? (String) client.get("description") : null;

        // Get granted scopes
        List<String> grantedScopes = null;
        List<Map<String, Object>> grantedClientScopes =
                (List<Map<String, Object>>) consent.get("grantedClientScopes");
        if (grantedClientScopes != null) {
            grantedScopes = grantedClientScopes.stream()
                    .map(scope -> (String) scope.get("name"))
                    .toList();
        }

        // Consent date
        Instant consentDate = null;
        Long createdDate = (Long) consent.get("createdDate");
        if (createdDate != null) {
            consentDate = Instant.ofEpochMilli(createdDate);
        }

        return new ApplicationResponse(
                clientId,
                clientName,
                description,
                grantedScopes,
                consentDate
        );
    }
}
