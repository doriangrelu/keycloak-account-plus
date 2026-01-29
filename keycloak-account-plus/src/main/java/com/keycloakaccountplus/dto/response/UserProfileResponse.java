package com.keycloakaccountplus.dto.response;

import java.time.Instant;

public record UserProfileResponse(
        String id,
        String username,
        String firstName,
        String lastName,
        String email,
        boolean emailVerified,
        String phone,
        String birthday,
        Instant createdAt
) {}
