package com.example.delivery.service.order;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.config.error.ErrorCode;
import com.example.delivery.dto.cart.response.GetCartResponseDto;
import com.example.delivery.dto.order.response.ResponseOrderUpdateDto;
import com.example.delivery.entity.*;
import com.example.delivery.repository.cart.CartItemRepository;
import com.example.delivery.repository.cart.CartRepository;
import com.example.delivery.repository.order.OrderRepository;
import com.example.delivery.repository.store.StoreRepository;
import com.example.delivery.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private OrderService orderService;

    private UserEntity user;
    private CartEntity cart;
    private StoreEntity store;
    private CartItemEntity cartItem;
    private OrderEntity order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 기본 객체 생성
        user = UserEntity.builder().userId(1L).build();

        // store 객체를 실제로 빌드
        store = StoreEntity.builder()
                .storeId(1L)
                .name("Test Store")
                .open(LocalTime.of(9, 0))
                .close(LocalTime.of(22, 0))
                .minOrderPrice(1000)  // 최소 주문 금액을 1000으로 설정
                .status(StoreEntity.Status.OPEN)
                .closed(false)
                .user(user)
                .build();

        cart = CartEntity.of(user, store);

        // 가격이 최소 주문 금액 이상인 메뉴 설정
        MenuEntity menu = MenuEntity.builder()
                .menuId(1L)
                .store(store)
                .price(1000)  // 메뉴 가격을 최소 금액(1000) 이상으로 설정
                .build();

        cartItem = CartItemEntity.of(cart, menu, 1, user);  // 카트 아이템 추가

        // 카트에 아이템 추가
        cart.getCartItems().add(cartItem);

        // 주문 생성
        order = OrderEntity.of(user, store, OrderEntity.Status.ORDERED, List.of(cartItem));
    }

    @Test
    void 주문_성공() {
        // given
        UserEntity user = UserEntity.builder().userId(1L).build(); // 유저 객체 생성

        // StoreEntity를 실제 객체로 생성하고 최소 주문 금액 설정
        StoreEntity store = StoreEntity.builder()
                .storeId(1L)
                .status(StoreEntity.Status.OPEN)
                .minOrderPrice(1000) // 최소 주문 금액을 1000으로 설정
                .build();  // 실제 객체로 생성

        // MenuEntity의 가격을 설정 (예: 1000원)
        MenuEntity menu = MenuEntity.builder().menuId(1L).store(store).price(1000).build();

        // 카트 아이템 생성
        CartEntity cart = CartEntity.of(user, store);  // 카트 객체 생성
        CartItemEntity cartItem = CartItemEntity.of(cart, menu, 1, user);  // 카트 아이템 생성
        List<CartItemEntity> cartItems = List.of(cartItem);  // 카트 아이템 리스트

        // user, cart, cartItem, store, menu, orderRepository와 같은 의존성 mock 설정
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCart(cart)).thenReturn(cartItems);
        when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));

        // 주문이 저장되는 동작 mock 설정
        OrderEntity order = OrderEntity.of(user, store, OrderEntity.Status.ORDERED, cartItems);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(order);  // 주문 저장

        // when
        GetCartResponseDto result = orderService.orderCart(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(order.getOrderId());  // 반환된 주문 ID 검증
        assertThat(result.getTotalAmount()).isEqualTo(1000);  // 총 금액이 예상대로 계산되었는지 검증
        verify(orderRepository, times(1)).save(any(OrderEntity.class));  // orderRepository가 한 번만 호출되었는지 확인
    }

    @Test
    void 가게가_운영중이지_않을때_주문_실패() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCart(cart)).thenReturn(List.of(cartItem));

        // 가게가 운영 중이지 않도록 설정
        store.setStatus(StoreEntity.Status.CLOSE); // 가게 상태를 CLOSED로 설정

        when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));

        // when & then
        assertThatThrownBy(() -> orderService.orderCart(1L))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.STORE_NOT_AVALIABLE.getMessage());
    }

    @Test
    void 가게_소유자_이외의_유저가_주문_상태_업데이트_시_예외_발생() {
        // given
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));  // 다른 유저
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderService.updateOrder(order.getOrderId(), "ACCEPTED", 2L))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 주문_상태_업데이트_주문_존재하지_않을때_예외_발생() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());  // 존재하지 않는 주문 ID

        // when & then
        assertThatThrownBy(() -> orderService.updateOrder(999L, "ACCEPTED", 1L))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.ORDER_NOT_FOUND.getMessage());
    }

    @Test
    void updateOrder_성공() {
        // given
        UserEntity user = UserEntity.builder().userId(1L).build(); // user 객체 생성
        StoreEntity store = StoreEntity.builder().storeId(1L).user(user).build();  // store 객체 생성
        CartItemEntity cartItem = CartItemEntity.builder().build(); // cartItem 객체 생성
        OrderEntity order = OrderEntity.builder().orderId(1L).store(store).status(OrderEntity.Status.ORDERED).build(); // 주문 생성

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // when
        ResponseOrderUpdateDto result = orderService.updateOrder(1L, "ACCEPTED", 1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("주문이 성공적으로 수락되었습니다.");  // 메시지 검증
        verify(orderRepository, times(1)).save(order);  // orderRepository가 한번만 호출되었는지 확인
    }
}