package com.example.delivery.entity;

import com.example.delivery.service.auth.model.Token;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_tokens")
@Entity
public class RefreshTokenEntity extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 512)
	private String accessToken;

	@Column(nullable = false)
	private LocalDateTime accessTokenExpiredAt;

	@Column(nullable = false, unique = true, length = 512)
	private String refreshToken;

	@Column(nullable = false)
	private LocalDateTime refreshTokenExpiredAt;

	public static RefreshTokenEntity of(Token token) {
		LocalDateTime accessTokenExpireDateTime = token.getAccessTokenExpiredAt().toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDateTime();

		LocalDateTime refreshTokenExpireDateTime = token.getRefreshTokenExpiredAt().toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDateTime();

		return RefreshTokenEntity.builder()
			.accessToken(token.getAccessToken())
			.accessTokenExpiredAt(accessTokenExpireDateTime)
			.refreshToken(token.getRefreshToken())
			.refreshTokenExpiredAt(refreshTokenExpireDateTime)
			.build();
	}
}
