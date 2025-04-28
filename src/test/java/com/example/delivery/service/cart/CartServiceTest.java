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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    private UserEntity user;
    private CartEntity cart;
    private MenuEntity menu;
    private StoreEntity store;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 기본으로 쓸 Mock 객체
        store = StoreEntity.of();
        user = UserEntity.builder().userId(1L).build();
        menu = MenuEntity.builder().menuId(1L).store(store).build();
        cart = CartEntity.of(user, store);
    }

    @Test
    void 장바구니_조회_성공() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        // when
        GetCartResponseDto result = cartService.viewCart(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getTotalAmount()).isZero();

    }

    @Test
    void 장바구니에_아이템_추가_성공() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        // when
        CartResponseDto result = cartService.addCartItem(1L, 1L);

        // then
        assertThat(result.getMessage()).isEqualTo("메뉴가 성공적으로 추가되었습니다.");  // CartResponseDto 안 메세지 확인
    }

    @Test
    void 장바구니_수량_감소_후_아이템_삭제() {
        // given
        CartItemEntity cartItem = CartItemEntity.of(cart, menu, 1, user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        cart.getCartItems().add(cartItem);

        // when
        CartResponseDto result = cartService.decreaseCartItem(1L, 1L);

        // then
        assertThat(result.getMessage()).isEqualTo("장바구니 항목이 삭제되었습니다.");
        verify(cartItemRepository, times(1)).delete(cartItem);
    }

    @Test
    void 장바구니_전체_삭제() {
        // given
        CartItemEntity cartItem = CartItemEntity.of(cart, menu, 1, user);
        cart.getCartItems().add(cartItem);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        // when
        CartResponseDto result = cartService.deleteCartItem(1L);

        // then
        assertThat(result.getMessage()).isEqualTo("장바구니가 초기화되었습니다.");
        verify(cartItemRepository, times(1)).deleteAllByCart(cart);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void 유저가_없을때_예외_발생() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> cartService.viewCart(1L))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }
}