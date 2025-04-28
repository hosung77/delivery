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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public GetCartResponseDto orderCart(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        log.info("user: {}", user);

        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new CustomException(ErrorCode.CART_NOT_FOUND));
        log.info("cart: {}", cart);

        List<CartItemEntity> cartItems = cartItemRepository.findByCart(cart);
        log.info("cartItems: {}", cartItems);

        StoreEntity store = cart.getStore();

        if(!store.isOperating()){
            throw new CustomException(ErrorCode.STORE_NOT_AVALIABLE);
        }

        List<GetCartResponseDto.CartItemDto> orderedItemDtos = cartItems.stream()
                .map(GetCartResponseDto.CartItemDto::from)
                .toList();

        int totalPrice = orderedItemDtos.stream()
                .mapToInt(GetCartResponseDto.CartItemDto::getTotalPrice)
                .sum();

        if(!store.isMinOrderPrice(totalPrice)){
            throw new CustomException(ErrorCode.NOT_OVER_MINPRICE);
        }

        OrderEntity order = OrderEntity.of(user, store, OrderEntity.Status.ORDERED, cartItems);

        orderRepository.save(order);
        return GetCartResponseDto.of(orderedItemDtos, totalPrice, order.getOrderId(), store);
    }

    @Transactional
    public ResponseOrderUpdateDto updateOrder(Long orderId, String status, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(()-> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        StoreEntity store = order.getStore();

        if(!store.isOwner(userId)){
            throw new CustomException(ErrorCode.OWNER_NOT_MATCH);
        }

        OrderEntity.Status receivedStatus = OrderEntity.Status.valueOf(status);
        order.updateStatus(receivedStatus);

        if (receivedStatus == OrderEntity.Status.ACCEPTED){
            order.startCooking();
        }

        orderRepository.save(order);
        return ResponseOrderUpdateDto.updatedStatus(order);
    }
}
