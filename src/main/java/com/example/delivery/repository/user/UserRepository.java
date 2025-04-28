package com.example.delivery.repository.user;

import com.example.delivery.dto.user.req.CreateUserReqDTO;
import com.example.delivery.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    CreateUserReqDTO getUserByEmail(String username);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
