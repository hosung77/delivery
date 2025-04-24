package com.example.delivery.service.cart;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.config.error.ErrorCode;
import com.example.delivery.dto.cart.response.GetCartResponseDto;
import com.example.delivery.entity.CartEntity;
import com.example.delivery.entity.UserEntity;
import com.example.delivery.repository.cart.CartRepository;
import com.example.delivery.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    // 해당 유저의 카트 조회하는 서비스 로직. 카트가 존재하지 않으면 카트를 생성
    public GetCartResponseDto viewCart(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        CartEntity cart = cartRepository.findByUser(user)
    }

}
