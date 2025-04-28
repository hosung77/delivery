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

        // 유저가 존재하는지 확인
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 카트가 존재하는지 확인
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new CustomException(ErrorCode.CART_NOT_FOUND));

        // store가 null일 경우 기본값을 가진 StoreEntity 반환
        StoreEntity store = cart.getStore() != null ? cart.getStore() : StoreEntity.of();

        // 카트에 있는 메뉴들을 조회 및 dto로 변환하여 list 형태로 반환
        List<GetCartResponseDto.CartItemDto> itemDtos = cart.getCartItems().stream()
                .map(GetCartResponseDto.CartItemDto::from)
                .toList();

        // dto에 있는 메뉴들의 총합을 구하는 메서드
        int totalPrice = itemDtos.stream()
                .mapToInt(GetCartResponseDto.CartItemDto::getTotalPrice)
                .sum();

        return GetCartResponseDto.of(itemDtos, totalPrice, cart.getCartId(), store);

    }

    @Transactional
    public CartResponseDto addCartItem(Long menuId, Long userId) {

        // 유저가 존재하는지 확인
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 메뉴 확인
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        // 카트가 없다면 카트를 생성
        CartEntity cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    // 카트를 새로 만들 때, store 정보를 함께 설정
                    CartEntity newCart = CartEntity.of(user, menu.getStore());
                    return cartRepository.save(newCart);
                });

        // 장바구니에 이미 메뉴가 있다면 store 체크
        if (!cart.getCartItems().isEmpty()) {
            StoreEntity currentStore = cart.getCartItems().get(0).getMenu().getStore();
            StoreEntity newMenuStore = menu.getStore();

            if (!currentStore.isSameStore(newMenuStore)) {
                // 다른 가게면 장바구니 비우기
                cart.clearCart(); // cart에서 cartItems 다 지워주는 메소드 필요
                cart.addStore(menu.getStore()); // 새로 추가되는 아이템의 store 정보를 설정
            }
        } else {
            // 카트가 비어있을 때, store 정보 설정
            cart.addStore(menu.getStore()); // 첫 아이템이 추가될 때 store 정보 설정
        }

        // 이미 장바구니에 아이템이 있는지 확인
        CartItemEntity existingItem = cart.getCartItems().stream()
                .filter(item -> item.getMenu().getMenuId().equals(menuId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.addQuantity();
            cartItemRepository.save(existingItem);
        } else {
            // 새 아이템을 추가할 때, store 정보도 함께 설정
            CartItemEntity newItem = CartItemEntity.of(cart, menu, 1, user);
            cartItemRepository.save(newItem);
        }

        return CartResponseDto.itemAdded();
    }

    @Transactional
    public CartResponseDto decreaseCartItem(Long menuId, Long userId) {

        // 유저가 존재하는지 확인
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 카트가 존재하는지 확인
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new CustomException(ErrorCode.CART_NOT_FOUND));

        // 메뉴가 존재하는지 확인, 임시 예외
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(()-> new CustomException(ErrorCode.MENU_NOT_FOUND));

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

        return CartResponseDto.itemDeleted();
    }

    @Transactional
    public CartResponseDto deleteCartItem(Long userId) {

        // 유저가 존재하는지 확인
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 카트가 존재하는지 확인
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new CustomException(ErrorCode.CART_NOT_FOUND));

        // Cart를 유지한채 카트의 목록을 초기화
        cart.clearCart(); // 아이템 초기화
        cartItemRepository.deleteAllByCart(cart); // DB에서 아이템 삭제
        cart.clearStore(); // 스토어 초기화
        cartRepository.save(cart); // 카트 저장

        return CartResponseDto.cartCleared();

    }

}
