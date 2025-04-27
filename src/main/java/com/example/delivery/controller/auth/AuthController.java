package com.example.delivery.controller.auth;

import com.example.delivery.dto.auth.request.SigninRequest;
import com.example.delivery.dto.auth.response.TokenResponse;
import com.example.delivery.service.auth.AuthService;
import com.example.delivery.service.auth.model.Token;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.delivery.config.util.AuthCookieUtil.addAuthCookies;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	// 로그인
	@PostMapping("/api/auth/token")
	public ResponseEntity<TokenResponse> signin(
		@Valid @RequestBody SigninRequest request,
		HttpServletResponse response) {
		Token token = authService.generateToken(request.email(), request.password());
		addAuthCookies(response, token);
		return ResponseEntity.ok(TokenResponse.from(token));
	}
	@PostMapping("/api/auth/logout")
	public ResponseEntity<Void> logout(
			HttpServletResponse response
	){
		authService.deleteToken(response);
		return ResponseEntity.ok().build();
	}
}
