package com.example.delivery.dto.auth.response;

import com.example.delivery.service.auth.model.Token;

import java.util.Date;

public record TokenResponse(
	String accessToken,
	Date accessTokenIssuedAt,
	Date accessTokenExpiredAt,
	String refreshToken,
	Date refreshTokenIssuedAt,
	Date refreshTokenExpiredAt
) {

	public static TokenResponse from(Token token) {
		return new TokenResponse(
			token.getAccessToken(),
			token.getAccessTokenIssuedAt(),
			token.getAccessTokenExpiredAt(),
			token.getRefreshToken(),
			token.getRefreshTokenIssuedAt(),
			token.getRefreshTokenExpiredAt()
		);
	}
}
