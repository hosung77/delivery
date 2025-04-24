package com.example.delivery.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class ResponseOrderDto {

    private final Long orderId;
    private final String status;
    private final LocalDateTime createdAt;
    private final StoreInfo store;
    private final List<MenuInfo> orderItems;
    private final int totalAmount;

    @Getter
    public static class StoreInfo {
        private final Integer storeId;
        private final String storeName;

        public StoreInfo(Integer storeId, String storeName) {
            this.storeId = storeId;
            this.storeName = storeName;
        }
    }

    @Getter
    public static class MenuInfo {
        private final Long menuId;
        private final String name;
        private final int price;
        private final int quantity;
        private final int totalPrice;

        public MenuInfo(Long menuId, String name, int price, int quantity, int totalPrice) {
            this.menuId = menuId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
        }
    }


}
