package com.keycloakaccountplus.dto.response;

import java.time.Instant;
import java.util.List;

public record ApplicationResponse(
        String clientId,
        String clientName,
        String description,
        List<String> grantedScopes,
        Instant consentDate
) {}
