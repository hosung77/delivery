package com.example.delivery.dto.cart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetCartResponseDto {
    private List<CartItemDto> items;
    private int totalAmount;
    private Long cartId;

    @Getter
    @AllArgsConstructor
    public static class CartItemDto {
        private Long menuId;
        private String name;
        private int price;
        private int quantity;
        private int totalPrice;
    }

    public static GetCartResponseDto of(List<CartItemDto> items, int totalAmount, Long cartId) {
        return new GetCartResponseDto(items, totalAmount,cartId);
    }

}
