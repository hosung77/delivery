package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "tb_cart_item")
@Getter
@NoArgsConstructor
@ToString(exclude = {"cart", "user"})
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private MenuEntity menu;

    @ManyToOne
    @JoinColumn(name = "order_id")  // "order"라는 속성을 사용할 경우, "order"라는 필드명을 사용해야 합니다.
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;  // UserEntity와 연관관계 설정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    private int quantity;

    public CartItemEntity(CartEntity cart, MenuEntity menu, int quantity, UserEntity user) {
        this.cart = cart;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static CartItemEntity of (CartEntity cart, MenuEntity menu, int quantity, UserEntity user) {
        return new CartItemEntity(cart, menu, quantity, user);
    }

    public int getTotalPrice() {
        return menu.getPrice() * quantity;
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity() {
        this.quantity += 1;
    }

    public void decreaseQuantity() {
        this.quantity -= 1;
    }



}