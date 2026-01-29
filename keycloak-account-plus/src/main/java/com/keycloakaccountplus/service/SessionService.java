package com.keycloakaccountplus.service;

import com.keycloakaccountplus.dto.response.SessionResponse;
import com.keycloakaccountplus.exception.UnauthorizedException;
import com.keycloakaccountplus.security.KeycloakPrincipal;
import com.keycloakaccountplus.security.SecurityContextService;
import org.keycloak.representations.idm.UserSessionRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final KeycloakAdminService keycloakAdmin;
    private final SecurityContextService securityContext;

    public SessionService(KeycloakAdminService keycloakAdmin, SecurityContextService securityContext) {
        this.keycloakAdmin = keycloakAdmin;
        this.securityContext = securityContext;
    }

    public List<SessionResponse> getCurrentUserSessions() {
        String userId = securityContext.getCurrentUserId();
        log.debug("Getting sessions for user: {}", userId);

        // Get current session ID from the token to mark it
        KeycloakPrincipal principal = securityContext.getCurrentPrincipal();
        String currentSessionId = principal.getAttribute("sid");

        List<UserSessionRepresentation> sessions = keycloakAdmin.getUserSessions(userId);

        return sessions.stream()
                .map(session -> toResponse(session, currentSessionId))
                .toList();
    }

    public void logoutSession(String sessionId) {
        String userId = securityContext.getCurrentUserId();
        log.debug("Logging out session {} for user {}", sessionId, userId);

        // Verify session belongs to current user
        List<UserSessionRepresentation> sessions = keycloakAdmin.getUserSessions(userId);
        boolean sessionBelongsToUser = sessions.stream()
                .anyMatch(s -> s.getId().equals(sessionId));

        if (!sessionBelongsToUser) {
            log.warn("User {} attempted to logout session {} that doesn't belong to them", userId, sessionId);
            throw new UnauthorizedException("Session does not belong to current user");
        }

        keycloakAdmin.removeSession(sessionId);
        log.info("Logged out session {} for user {}", sessionId, userId);
    }

    public void logoutAllSessions() {
        String userId = securityContext.getCurrentUserId();
        log.debug("Logging out all sessions for user: {}", userId);

        // Get current session ID to optionally skip it
        KeycloakPrincipal principal = securityContext.getCurrentPrincipal();
        String currentSessionId = principal.getAttribute("sid");

        List<UserSessionRepresentation> sessions = keycloakAdmin.getUserSessions(userId);

        int loggedOut = 0;
        for (UserSessionRepresentation session : sessions) {
            // Skip current session to avoid logging out the user making the request
            if (session.getId().equals(currentSessionId)) {
                log.debug("Skipping current session: {}", currentSessionId);
                continue;
            }
            keycloakAdmin.removeSession(session.getId());
            loggedOut++;
        }

        log.info("Logged out {} sessions for user {} (excluding current)", loggedOut, userId);
    }

    public void logoutAllSessionsIncludingCurrent() {
        String userId = securityContext.getCurrentUserId();
        log.debug("Logging out ALL sessions (including current) for user: {}", userId);

        List<UserSessionRepresentation> sessions = keycloakAdmin.getUserSessions(userId);

        for (UserSessionRepresentation session : sessions) {
            keycloakAdmin.removeSession(session.getId());
        }

        log.info("Logged out all {} sessions for user {}", sessions.size(), userId);
    }

    private SessionResponse toResponse(UserSessionRepresentation session, String currentSessionId) {
        Instant startedAt = Instant.ofEpochSecond(session.getStart());
        Instant lastAccessAt = Instant.ofEpochSecond(session.getLastAccess());

        // Get clients from the session
        Map<String, String> clients = session.getClients();
        List<String> clientNames = clients != null ? List.copyOf(clients.values()) : List.of();

        // Parse user agent if available
        String userAgent = null;
        Map<String, String> notes = session.getNotes();
        if (notes != null) {
            userAgent = notes.get("user_agent");
        }

        boolean isCurrent = session.getId().equals(currentSessionId);

        return new SessionResponse(
                session.getId(),
                session.getIpAddress(),
                startedAt,
                lastAccessAt,
                clientNames,
                userAgent,
                isCurrent
        );
    }
}
