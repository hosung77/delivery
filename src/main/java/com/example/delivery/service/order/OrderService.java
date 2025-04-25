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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.delivery.entity.OrderMenuEntity.toOrderMenu;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public GetCartResponseDto orderCart(Long userId) {

        // 존재하는 유저인지 확인
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 유저의 카트 불러오기
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new CustomException(ErrorCode.CART_NOT_FOUND));

        // 카트에서 물건들 가져오기
        List<CartItemEntity> cartItems = cartItemRepository.findByUserId(userId);

        // 가게 정보 불러오기
        StoreEntity store = cart.getStore();

        // 가게가 열었는지 확인
        if(!store.isOperating()){
            throw new CustomException(ErrorCode.USER_NOT_FOUND); // 임시 예외 사용
        }

        // 카트에 담긴 물건들을 dto로 변환
        List<GetCartResponseDto.CartItemDto> orderedItemDtos = cartItems.stream()
                .map(GetCartResponseDto.CartItemDto::from)
                .toList();

        // 총 가격 계산
        int totalPrice = orderedItemDtos.stream()
                .mapToInt(GetCartResponseDto.CartItemDto::getTotalPrice)
                .sum();

        // 주문 내역이 최소 금액 이상인지 확인
        if(!store.isMinOrderPrice(totalPrice)){
            throw new CustomException(ErrorCode.NOT_OVER_MINPRICE);
        }

        // 주문 엔티티 생성
        OrderEntity order = OrderEntity.of(user, store, OrderEntity.Status.ORDERED, cartItems);

        orderRepository.save(order);

        return GetCartResponseDto.of(orderedItemDtos, totalPrice, order.getOrderId(), store);

    }

    @Transactional
    public ResponseOrderUpdateDto updateOrder(Long storeId, Long orderId, String status, Long userId) {
        // 가게 조회
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 쿠키에서 받아온 유저 아이디로 유저 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 주문 조회
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(()-> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if(!store.isOwner(userId)){
            throw new CustomException(ErrorCode.ONER_NOT_MATCH);
        }

        // status 값을 Status enum으로 변환
        OrderEntity.Status receivedStatus = OrderEntity.Status.valueOf(status);

        order.updateStatus(receivedStatus);

        if (receivedStatus == OrderEntity.Status.ACCEPTED){
            order.startCooking();
        }

        orderRepository.save(order);

        return ResponseOrderUpdateDto.updatedStatus(order);

    }
}
