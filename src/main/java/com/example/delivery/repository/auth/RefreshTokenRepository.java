package com.example.delivery.repository.auth;

import com.example.delivery.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author    : kimjungmin
 * Created on : 2025. 4. 7.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
	Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

	@Transactional
	@Modifying
	@Query("delete from RefreshTokenEntity r where r.refreshTokenExpiredAt < :now")
	void deleteAllExpiredSince(LocalDateTime now);
}
