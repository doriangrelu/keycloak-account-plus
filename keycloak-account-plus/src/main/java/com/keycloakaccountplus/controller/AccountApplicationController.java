package com.keycloakaccountplus.controller;

import com.keycloakaccountplus.dto.response.ApplicationResponse;
import com.keycloakaccountplus.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account/applications")
public class AccountApplicationController {

    private final ApplicationService applicationService;

    public AccountApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MANAGE-ACCOUNT')")
    public ResponseEntity<List<ApplicationResponse>> getApplications() {
        return ResponseEntity.ok(applicationService.getConsentedApplications());
    }

    @DeleteMapping("/{clientId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGE-ACCOUNT')")
    public ResponseEntity<Void> revokeConsent(@PathVariable String clientId) {
        applicationService.revokeConsent(clientId);
        return ResponseEntity.noContent().build();
    }
}
