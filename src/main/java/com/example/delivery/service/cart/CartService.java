package com.example.delivery.service.cart;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.config.error.ErrorCode;
import com.example.delivery.dto.cart.response.GetCartResponseDto;
import com.example.delivery.entity.CartEntity;
import com.example.delivery.entity.CartItemEntity;
import com.example.delivery.entity.MenuEntity;
import com.example.delivery.entity.UserEntity;
import com.example.delivery.repository.cart.CartItemRepository;
import com.example.delivery.repository.cart.CartRepository;
import com.example.delivery.repository.menu.MenuRepository;
import com.example.delivery.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public GetCartResponseDto viewCart(Long userId) {

        // 유저가 존재하는지 확인
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 카트가 존재하는지 확인 후 없으면 생성
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new CustomException(ErrorCode.C))

        // 카트에 있는 메뉴들을 조회 및 dto로 변환하여 list 형태로 반환
        List<GetCartResponseDto.CartItemDto> itemDtos = cart.getCartItems().stream()
                .map(item -> new GetCartResponseDto.CartItemDto(
                        item.getMenu().getMenuId(),
                        item.getMenu().getName(),
                        item.getMenu().getPrice(),
                        item.getQuantity(),
                        item.getTotalPrice()
                ))
                .toList();

        // dto에 있는 메뉴들의 총합을 구하는 메서드
        int totalPrice = itemDtos.stream()
                .mapToInt(GetCartResponseDto.CartItemDto::getTotalPrice)
                .sum();

        return GetCartResponseDto.of(itemDtos, totalPrice, cart.getCartId());

    }

    public void addCartItem(Long menuId, Long userId) {

        // 유저가 존재하는지 확인
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 카트에 물품 추가시 카트가 없다면 카트를 추가
        CartEntity cart = cartRepository.findByUser(user)
                .orElseGet(()->
                        cartRepository.save(CartEntity.of(user))
                );

        // 메뉴 확인, 임시 예외 사용
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        CartItemEntity existingItem = cart.getCartItems().stream()
                .filter(item -> item.getMenu().getName().equals(menuId))
                .findFirst()
                .orElse(null);

        if(existingItem != null){
            existingItem.addQuantity();
            cartItemRepository.save(existingItem);
        }else{
            CartItemEntity newItem = CartItemEntity.of(cart,menu,1);
            cartItemRepository.save(newItem);
        }

    }

    @Transactional
    public void decreaseCartItem(Long menuId, Long userId) {

        // 유저가 존재하는지 확인
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 카트가 존재하는지 확인
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new CustomException(ErrorCode.CART_NOT_FOUND));

        // 메뉴가 존재하는지 확인, 임시 예외
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        CartItemEntity presentCart = cart.getCartItems().stream()
                .filter(item -> item.getMenu().getMenuId().equals(menuId))
                .findFirst()
                .orElseThrow(()->new CustomException(ErrorCode.NOT_MATCH_MENU));

        presentCart.decreaseQuantity();

        if(presentCart.getQuantity() == 0){
            cartItemRepository.delete(presentCart);
        } else{
            cartItemRepository.save(presentCart);
        }

    }

}
