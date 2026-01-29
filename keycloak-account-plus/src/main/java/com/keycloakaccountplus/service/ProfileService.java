package com.keycloakaccountplus.service;

import com.keycloakaccountplus.dto.request.UpdateProfileRequest;
import com.keycloakaccountplus.dto.response.UserProfileResponse;
import com.keycloakaccountplus.security.SecurityContextService;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProfileService {

    private static final Logger log = LoggerFactory.getLogger(ProfileService.class);

    private final KeycloakAdminService keycloakAdmin;
    private final SecurityContextService securityContext;

    public ProfileService(KeycloakAdminService keycloakAdmin, SecurityContextService securityContext) {
        this.keycloakAdmin = keycloakAdmin;
        this.securityContext = securityContext;
    }

    public UserProfileResponse getCurrentUserProfile() {
        String userId = securityContext.getCurrentUserId();
        log.debug("Getting profile for user: {}", userId);

        UserRepresentation user = keycloakAdmin.getUser(userId);
        return toResponse(user);
    }

    public UserProfileResponse updateCurrentUserProfile(UpdateProfileRequest request) {
        String userId = securityContext.getCurrentUserId();
        log.debug("Updating profile for user: {}", userId);

        UserRepresentation user = keycloakAdmin.getUser(userId);

        // Update basic fields
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());

        // Update custom attributes
        Map<String, List<String>> attributes = user.getAttributes();
        if (attributes == null) {
            attributes = new HashMap<>();
        }

        if (request.phone() != null) {
            attributes.put("phone", List.of(request.phone()));
        }
        if (request.birthday() != null) {
            attributes.put("birthday", List.of(request.birthday()));
        }
        user.setAttributes(attributes);

        keycloakAdmin.updateUser(userId, user);

        // Fetch and return updated user
        UserRepresentation updatedUser = keycloakAdmin.getUser(userId);
        return toResponse(updatedUser);
    }

    private UserProfileResponse toResponse(UserRepresentation user) {
        String phone = getAttributeValue(user, "phone");
        String birthday = getAttributeValue(user, "birthday");

        Instant createdAt = null;
        if (user.getCreatedTimestamp() != null) {
            createdAt = Instant.ofEpochMilli(user.getCreatedTimestamp());
        }

        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                Boolean.TRUE.equals(user.isEmailVerified()),
                phone,
                birthday,
                createdAt
        );
    }

    private String getAttributeValue(UserRepresentation user, String attributeName) {
        Map<String, List<String>> attributes = user.getAttributes();
        if (attributes == null) {
            return null;
        }
        List<String> values = attributes.get(attributeName);
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.get(0);
    }
}
