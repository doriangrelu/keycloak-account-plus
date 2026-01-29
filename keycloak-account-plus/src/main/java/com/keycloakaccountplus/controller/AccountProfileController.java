package com.keycloakaccountplus.controller;

import com.keycloakaccountplus.dto.request.UpdateProfileRequest;
import com.keycloakaccountplus.dto.response.UserProfileResponse;
import com.keycloakaccountplus.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account/profile")
public class AccountProfileController {

    private final ProfileService profileService;

    public AccountProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MANAGE-ACCOUNT')")
    public ResponseEntity<UserProfileResponse> getProfile() {
        return ResponseEntity.ok(profileService.getCurrentUserProfile());
    }

    @PutMapping
    @PreAuthorize("hasRole('USER') or hasRole('MANAGE-ACCOUNT')")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(profileService.updateCurrentUserProfile(request));
    }
}
