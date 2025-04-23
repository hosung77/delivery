package com.example.delivery.dto.auth.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
	@NotBlank
	String refreshToken
) {
}