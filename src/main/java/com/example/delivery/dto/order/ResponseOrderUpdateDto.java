package com.example.delivery.dto.order;

import com.example.delivery.entity.OrderEntity;
import lombok.Getter;

@Getter
public class ResponseOrderUpdateDto {
    Long orderId;
    String message;

    public ResponseOrderUpdateDto(Long menuId, String message) {
        this.orderId = menuId;
        this.message = message;
    }

    public static ResponseOrderUpdateDto updatedStatus(OrderEntity order) {
        String statusString = order.getStatus().name();
        String message;

        if (statusString.equals("COOKING")) {
            message = "주문이 성공적으로 수락되었습니다.";
        } else if (statusString.equals("CANCELLED")) {
            message = "주문이 거절되었습니다.";
        } else{
            message = "주문이 존재하지 않습니다.";
        }
            return new ResponseOrderUpdateDto(order.getOrderId(), message);
    }

}