package com.keycloakaccountplus.controller;

import com.keycloakaccountplus.dto.response.CredentialResponse;
import com.keycloakaccountplus.service.CredentialService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account/credentials")
public class AccountCredentialController {

    private final CredentialService credentialService;

    public AccountCredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MANAGE-ACCOUNT')")
    public ResponseEntity<List<CredentialResponse>> getCredentials() {
        return ResponseEntity.ok(credentialService.getCurrentUserCredentials());
    }

    @GetMapping("/totp")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGE-ACCOUNT')")
    public ResponseEntity<List<CredentialResponse>> getTotpCredentials() {
        return ResponseEntity.ok(credentialService.getCurrentUserTotpCredentials());
    }

    @DeleteMapping("/totp/{credentialId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGE-ACCOUNT')")
    public ResponseEntity<Void> removeTotp(@PathVariable String credentialId) {
        credentialService.removeTotp(credentialId);
        return ResponseEntity.noContent().build();
    }
}
