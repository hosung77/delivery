package com.example.delivery.dto.order.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestOrderDto {
    private List<MenuOrder> orderItems;

    @Getter
    @Setter
    public static class MenuOrder {
        private Long menuId;
        private int quantity;
    }

}
