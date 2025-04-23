package com.example.delivery.service.user;

import com.example.delivery.dto.user.req.UserReqDTO;
import com.example.delivery.entity.UserEntity;
import com.example.delivery.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public void createUser(UserReqDTO userReqDTO) {
        String encodedPassword = passwordEncoder.encode(userReqDTO.getPassword());

        UserEntity userEntity = UserEntity.builder()
                .email(userReqDTO.getEmail())
                .password(encodedPassword)
                .name(userReqDTO.getName())
                .roles(UserEntity.Role.valueOf(userReqDTO.getRole()))
                .build();

        userRepository.save(userEntity);
    }
}
