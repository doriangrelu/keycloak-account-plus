package com.keycloakaccountplus.controller;

import com.keycloakaccountplus.dto.response.SessionResponse;
import com.keycloakaccountplus.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account/sessions")
public class AccountSessionController {

    private final SessionService sessionService;

    public AccountSessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MANAGE-ACCOUNT')")
    public ResponseEntity<List<SessionResponse>> getSessions() {
        return ResponseEntity.ok(sessionService.getCurrentUserSessions());
    }

    @DeleteMapping("/{sessionId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGE-ACCOUNT')")
    public ResponseEntity<Void> logoutSession(@PathVariable String sessionId) {
        sessionService.logoutSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER') or hasRole('MANAGE-ACCOUNT')")
    public ResponseEntity<Void> logoutAllSessions(
            @RequestParam(defaultValue = "false") boolean includeCurrent) {
        if (includeCurrent) {
            sessionService.logoutAllSessionsIncludingCurrent();
        } else {
            sessionService.logoutAllSessions();
        }
        return ResponseEntity.noContent().build();
    }
}
