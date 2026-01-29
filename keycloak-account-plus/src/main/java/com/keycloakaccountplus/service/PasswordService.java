package com.keycloakaccountplus.service;

import com.keycloakaccountplus.dto.request.ChangePasswordRequest;
import com.keycloakaccountplus.exception.ValidationException;
import com.keycloakaccountplus.security.SecurityContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private static final Logger log = LoggerFactory.getLogger(PasswordService.class);

    private final KeycloakAdminService keycloakAdmin;
    private final SecurityContextService securityContext;

    public PasswordService(KeycloakAdminService keycloakAdmin, SecurityContextService securityContext) {
        this.keycloakAdmin = keycloakAdmin;
        this.securityContext = securityContext;
    }

    public void changePassword(ChangePasswordRequest request) {
        String userId = securityContext.getCurrentUserId();
        log.debug("Changing password for user: {}", userId);

        // Validate passwords match
        if (!request.passwordsMatch()) {
            throw new ValidationException("New password and confirmation do not match");
        }

        // Note: In a real scenario, you might want to verify the current password first.
        // However, the Keycloak Admin API doesn't provide a direct way to verify passwords.
        // The user is already authenticated, so we proceed with the password reset.
        // For additional security, you could implement a custom password verification
        // using Keycloak's authentication API.

        keycloakAdmin.resetPassword(userId, request.newPassword(), false);
        log.info("Password changed for user: {}", userId);
    }
}
