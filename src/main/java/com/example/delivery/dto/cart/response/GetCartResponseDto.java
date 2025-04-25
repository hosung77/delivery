package com.example.delivery.dto.cart.response;

import com.example.delivery.entity.CartItemEntity;
import com.example.delivery.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetCartResponseDto {
    private List<CartItemDto> items;
    private int totalAmount;
    private Long cartId;
    private StoreDto store;

    public static GetCartResponseDto of(List<CartItemDto> items, int totalAmount, Long cartId, StoreEntity store) {
        return new GetCartResponseDto(
                items,
                totalAmount,
                cartId,
                new StoreDto(store.getStoreId(), store.getName()
                ));
    }

    @Getter
    @AllArgsConstructor
    public static class CartItemDto {
        private Long menuId;
        private String name;
        private int price;
        private int quantity;
        private int totalPrice;

        public static CartItemDto from(CartItemEntity cartItem) {
            return new CartItemDto(
                    cartItem.getMenu().getMenuId(),
                    cartItem.getMenu().getName(),
                    cartItem.getMenu().getPrice(),
                    cartItem.getQuantity(),
                    cartItem.getTotalPrice()
            );
        }

    }

    @Getter
    @AllArgsConstructor
    public static class StoreDto {
        private Long storeId;
        private String storeName;
    }

}
