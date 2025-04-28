package com.example.delivery.service.user;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.config.error.ErrorCode;
import com.example.delivery.dto.user.req.CreateUserReqDTO;
import com.example.delivery.dto.user.req.UpdateUserReqDTO;
import com.example.delivery.dto.user.res.UpdateUserResDTO;
import com.example.delivery.entity.UserEntity;
import com.example.delivery.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Transactional
    public void createUser(CreateUserReqDTO createUserReqDTO) {
        String encodedPassword = passwordEncoder.encode(createUserReqDTO.getPassword());

        if(userRepository.existsByEmail(createUserReqDTO.getEmail())){
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }

        UserEntity userEntity = UserEntity.builder()
                .email(createUserReqDTO.getEmail())
                .password(encodedPassword)
                .name(createUserReqDTO.getName())
                .roles(UserEntity.Role.valueOf(createUserReqDTO.getRole()))
                .build();

        userRepository.save(userEntity);
    }

    @Transactional
    public UpdateUserResDTO updateUser(UpdateUserReqDTO user, Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.getEmail() != null && !user.getEmail().equals(userEntity.getEmail())) {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
            }
            userEntity.updateEmail(user.getEmail());
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            userEntity.updatePassword(encodedPassword);
        }

        if (user.getName() != null && !user.getName().equals(userEntity.getName())) {
            userEntity.updateName(user.getName());
        }

        userRepository.save(userEntity);

        return UpdateUserResDTO.builder()
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .build();

    }
    @Transactional
    public void deleteUser(Long userId){
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userEntity.deleted();
    }
}
