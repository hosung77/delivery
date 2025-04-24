package com.example.delivery.service.order;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.config.error.ErrorCode;
import com.example.delivery.dto.order.RequestOrderDto;
import com.example.delivery.dto.order.ResponseOrderDto;
import com.example.delivery.dto.order.ResponseOrderUpdateDto;
import com.example.delivery.entity.*;
import com.example.delivery.mapper.OrderMapper;
import com.example.delivery.repository.order.OrderRepository;
import com.example.delivery.repository.store.StoreRepository;
import com.example.delivery.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.delivery.entity.OrderMenuEntity.toOrderMenu;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public ResponseOrderDto createOrder(RequestOrderDto request, Long storeId, Long userId) {
        // store 관련된 예외 만드실까봐 임시로 아무 예외 넣어놓음, 추후 수정
        // PathVariable로 받아온 id로 가게 조회
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 쿠키에서 받아온 유저 아이디로 유저 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 정적 메소드를 활용하여 order객체 생성
        OrderEntity order = OrderEntity.of(user, store, OrderEntity.Status.ORDERED);

        // 영업시간 체크 (StoreEntity에서 직접 확인)
        if (!store.isOperating()) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND); // 영업시간 아님, 임시 예외
        }

        // 요청받은 주문 항목들을 순회하면서,
        // 각 메뉴 ID에 해당하는 실제 메뉴 정보를 매칭해 OrderMenuEntity로 변환한 뒤,
        // 현재 주문(order)에 모두 추가한다.
        List<OrderMenuEntity> orderMenus =  request.getOrderItems().stream().
                map(menuOrder -> {
                    MenuEntity menu = store.getMenus().stream()
                            .filter(m -> m.getMenuId().equals(menuOrder.getMenuId()))
                            .findFirst()
                            .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

                    return toOrderMenu(order, menu, menuOrder.getQuantity());
                })
                .toList();

        order.getOrderMenus().addAll(orderMenus);

        // 최소 주문 금액 체크
        int totalOrderPrice = orderMenus.stream()
                .mapToInt(OrderMenuEntity::getTotalPrice)
                .sum();

        if (totalOrderPrice < store.getMinOrderPrice()) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND); // 임시 예외 나중에 수정
        }

        orderRepository.save(order);

        return orderMapper.toDto(order);
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
