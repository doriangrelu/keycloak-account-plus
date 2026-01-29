package com.keycloakaccountplus.dto.response;

import java.time.Instant;

public record CredentialResponse(
        String id,
        String type,
        String userLabel,
        Instant createdAt
) {}
