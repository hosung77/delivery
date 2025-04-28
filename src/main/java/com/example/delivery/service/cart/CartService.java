package com.example.delivery.service.cart;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.config.error.ErrorCode;
import com.example.delivery.dto.cart.response.CartResponseDto;
import com.example.delivery.dto.cart.response.GetCartResponseDto;
import com.example.delivery.entity.*;
import com.example.delivery.repository.cart.CartItemRepository;
import com.example.delivery.repository.cart.CartRepository;
import com.example.delivery.repository.menu.MenuRepository;
import com.example.delivery.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public GetCartResponseDto viewCart(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new CustomException(ErrorCode.CART_NOT_FOUND));

        StoreEntity store = cart.getStore() != null ? cart.getStore() : StoreEntity.of();

        List<GetCartResponseDto.CartItemDto> itemDtos = cart.getCartItems().stream()
                .map(GetCartResponseDto.CartItemDto::from)
                .toList();

        int totalPrice = itemDtos.stream()
                .mapToInt(GetCartResponseDto.CartItemDto::getTotalPrice)
                .sum();

        return GetCartResponseDto.of(itemDtos, totalPrice, cart.getCartId(), store);
    }

    @Transactional
    public CartResponseDto addCartItem(Long menuId, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
        CartEntity cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    CartEntity newCart = CartEntity.of(user, menu.getStore());
                    return cartRepository.save(newCart);
                });

        if (!cart.getCartItems().isEmpty()) {
            StoreEntity currentStore = cart.getCartItems().get(0).getMenu().getStore();
            StoreEntity newMenuStore = menu.getStore();

            if (!currentStore.isSameStore(newMenuStore)) {
                // 다른 가게면 장바구니 비우기
                cart.clearCart();
                cart.addStore(menu.getStore());
            }
        } else {
            // 카트가 비어있을 때, store 정보 설정
            cart.addStore(menu.getStore());
        }

        CartItemEntity existingItem = cart.getCartItems().stream()
                .filter(item -> item.getMenu().getMenuId().equals(menuId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.addQuantity();
            cartItemRepository.save(existingItem);
        } else {
            CartItemEntity newItem = CartItemEntity.of(cart, menu, 1, user);
            cartItemRepository.save(newItem);
        }

        return CartResponseDto.itemAdded();
    }

    @Transactional
    public CartResponseDto decreaseCartItem(Long menuId, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new CustomException(ErrorCode.CART_NOT_FOUND));
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(()-> new CustomException(ErrorCode.MENU_NOT_FOUND));


        cart.getCartItems().stream()
                .filter(item -> item.getMenu().getMenuId().equals(menuId))
                .findFirst()
                .map(presentCart -> {
                    presentCart.decreaseQuantity();

                    if (presentCart.getQuantity() == 0) {
                        cartItemRepository.delete(presentCart);
                    } else {
                        cartItemRepository.save(presentCart);
                    }

                    return CartResponseDto.itemDeleted();
                })
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_MATCH_MENU));

        return CartResponseDto.itemDeleted();
    }

    @Transactional
    public CartResponseDto deleteCartItem(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new CustomException(ErrorCode.CART_NOT_FOUND));

        cart.clearCart(); // 아이템 초기화
        cartItemRepository.deleteAllByCart(cart); // DB에서 아이템 삭제
        cart.clearStore(); // 스토어 초기화
        cartRepository.save(cart); // 카트 저장

        return CartResponseDto.cartCleared();

    }

}
