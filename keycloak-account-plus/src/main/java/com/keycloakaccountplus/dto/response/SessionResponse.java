package com.keycloakaccountplus.dto.response;

import java.time.Instant;
import java.util.List;

public record SessionResponse(
        String id,
        String ipAddress,
        Instant startedAt,
        Instant lastAccessAt,
        List<String> clients,
        String userAgent,
        boolean current
) {}
