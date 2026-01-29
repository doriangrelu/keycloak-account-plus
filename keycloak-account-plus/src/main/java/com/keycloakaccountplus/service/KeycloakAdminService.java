package com.keycloakaccountplus.service;

import com.keycloakaccountplus.exception.KeycloakApiException;
import com.keycloakaccountplus.exception.ResourceNotFoundException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.UserSessionRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class KeycloakAdminService {

    private static final Logger log = LoggerFactory.getLogger(KeycloakAdminService.class);

    private final RealmResource realmResource;

    public KeycloakAdminService(RealmResource realmResource) {
        this.realmResource = realmResource;
    }

    public UserResource getUserResource(String userId) {
        try {
            return realmResource.users().get(userId);
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException("User not found: " + userId);
        }
    }

    public UserRepresentation getUser(String userId) {
        try {
            return getUserResource(userId).toRepresentation();
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException("User not found: " + userId);
        } catch (Exception e) {
            log.error("Failed to get user {}", userId, e);
            throw new KeycloakApiException("Failed to get user: " + e.getMessage(), e);
        }
    }

    public void updateUser(String userId, UserRepresentation user) {
        try {
            getUserResource(userId).update(user);
            log.info("Updated user: {}", userId);
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException("User not found: " + userId);
        } catch (Exception e) {
            log.error("Failed to update user {}", userId, e);
            throw new KeycloakApiException("Failed to update user: " + e.getMessage(), e);
        }
    }

    public List<UserSessionRepresentation> getUserSessions(String userId) {
        try {
            return getUserResource(userId).getUserSessions();
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException("User not found: " + userId);
        } catch (Exception e) {
            log.error("Failed to get sessions for user {}", userId, e);
            throw new KeycloakApiException("Failed to get user sessions: " + e.getMessage(), e);
        }
    }

    public void removeSession(String sessionId) {
        try {
            realmResource.deleteSession(sessionId);
            log.info("Removed session: {}", sessionId);
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException("Session not found: " + sessionId);
        } catch (Exception e) {
            log.error("Failed to remove session {}", sessionId, e);
            throw new KeycloakApiException("Failed to remove session: " + e.getMessage(), e);
        }
    }

    public List<CredentialRepresentation> getCredentials(String userId) {
        try {
            return getUserResource(userId).credentials();
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException("User not found: " + userId);
        } catch (Exception e) {
            log.error("Failed to get credentials for user {}", userId, e);
            throw new KeycloakApiException("Failed to get credentials: " + e.getMessage(), e);
        }
    }

    public void removeCredential(String userId, String credentialId) {
        try {
            getUserResource(userId).removeCredential(credentialId);
            log.info("Removed credential {} for user {}", credentialId, userId);
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException("Credential not found: " + credentialId);
        } catch (Exception e) {
            log.error("Failed to remove credential {} for user {}", credentialId, userId, e);
            throw new KeycloakApiException("Failed to remove credential: " + e.getMessage(), e);
        }
    }

    public void resetPassword(String userId, String newPassword, boolean temporary) {
        try {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(newPassword);
            credential.setTemporary(temporary);

            getUserResource(userId).resetPassword(credential);
            log.info("Reset password for user: {}", userId);
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException("User not found: " + userId);
        } catch (Exception e) {
            log.error("Failed to reset password for user {}", userId, e);
            throw new KeycloakApiException("Failed to reset password: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getConsents(String userId) {
        try {
            return getUserResource(userId).getConsents();
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException("User not found: " + userId);
        } catch (Exception e) {
            log.error("Failed to get consents for user {}", userId, e);
            throw new KeycloakApiException("Failed to get consents: " + e.getMessage(), e);
        }
    }

    public void revokeConsent(String userId, String clientId) {
        try {
            getUserResource(userId).revokeConsent(clientId);
            log.info("Revoked consent for client {} for user {}", clientId, userId);
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException("Consent not found for client: " + clientId);
        } catch (Exception e) {
            log.error("Failed to revoke consent for client {} for user {}", clientId, userId, e);
            throw new KeycloakApiException("Failed to revoke consent: " + e.getMessage(), e);
        }
    }
}
