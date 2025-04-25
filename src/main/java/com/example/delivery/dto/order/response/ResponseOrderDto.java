package com.example.delivery.dto.order.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class ResponseOrderDto {

    private Long orderId;
    private String status;
    private LocalDateTime createdAt;
    private StoreInfo store;
    private List<MenuInfo> orderItems;
    private int totalAmount;

    @Getter
    public static class StoreInfo {
        private Long storeId;
        private String storeName;

        public StoreInfo(Long storeId, String storeName) {
            this.storeId = storeId;
            this.storeName = storeName;
        }
    }

    @Getter
    public static class MenuInfo {
        private Long menuId;
        private String name;
        private int price;
        private int quantity;
        private int totalPrice;

        public MenuInfo(Long menuId, String name, int price, int quantity, int totalPrice) {
            this.menuId = menuId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
        }
    }
}