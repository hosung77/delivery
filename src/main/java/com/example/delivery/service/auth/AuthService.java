package com.example.delivery.service.auth;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.config.error.ErrorCode;
import com.example.delivery.entity.RefreshTokenEntity;
import com.example.delivery.entity.UserEntity;
import com.example.delivery.repository.auth.RefreshTokenRepository;
import com.example.delivery.repository.user.UserRepository;
import com.example.delivery.service.auth.model.Token;
import com.example.delivery.service.auth.model.TokenClaim;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final TokenService tokenService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenRepository refreshTokenRepository;

	// Token 생성
	@Transactional
	public Token generateToken(final String email, final String password) {
		// 존재하는 유저인지 확인
		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		// 비밀번호 일치 여부 확인
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
		}
		List<String> roles = List.of(user.getRoles().name());  // 예: "ADMIN", "USER", "OWNER"
		// 토큰 정보 생성 ID, Email, nickname, Roles -> 현재는 권한 관리 X
		TokenClaim tokenClaim = new TokenClaim(user.getUserId(), user.getEmail(), user.getName(), roles);
		// Token(accessToken, refreshToken)
		Token token = tokenService.generateToken(tokenClaim);
		// refreshToken 저장
		refreshTokenRepository.save(RefreshTokenEntity.of(token));

		return token;
	}
	public void deleteToken(HttpServletResponse response){
		Cookie accessTokenCookie = new Cookie("accessToken", null);
		accessTokenCookie.setMaxAge(0);
		accessTokenCookie.setPath("/");
		accessTokenCookie.setHttpOnly(true);
		response.addCookie(accessTokenCookie);

		Cookie refreshTokenCookie = new Cookie("refreshToken", null);
		refreshTokenCookie.setMaxAge(0);
		refreshTokenCookie.setPath("/");
		refreshTokenCookie.setHttpOnly(true);
		response.addCookie(refreshTokenCookie);
	}
}
