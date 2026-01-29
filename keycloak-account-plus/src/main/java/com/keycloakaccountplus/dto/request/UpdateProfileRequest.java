package com.keycloakaccountplus.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank(message = "First name is required")
        @Size(max = 250, message = "First name must not exceed 250 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 250, message = "Last name must not exceed 250 characters")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 250, message = "Email must not exceed 250 characters")
        String email,

        @Size(max = 50, message = "Phone must not exceed 50 characters")
        String phone,

        String birthday
) {}
