package com.example.delivery.service.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Builder
@Getter
@AllArgsConstructor
@ToString
public class Token {
	private String accessToken;
	private Date accessTokenIssuedAt;
	private Date accessTokenExpiredAt;
	private String refreshToken;
	private Date refreshTokenIssuedAt;
	private Date refreshTokenExpiredAt;
}
