package com.keycloakaccountplus.controller;

import com.keycloakaccountplus.dto.request.ChangePasswordRequest;
import com.keycloakaccountplus.service.PasswordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account/password")
public class AccountPasswordController {

    private final PasswordService passwordService;

    public AccountPasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PutMapping
    @PreAuthorize("hasRole('USER') or hasRole('MANAGE-ACCOUNT')")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        passwordService.changePassword(request);
        return ResponseEntity.noContent().build();
    }
}
