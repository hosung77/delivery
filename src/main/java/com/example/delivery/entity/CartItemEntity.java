package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "tb_cart_item")
@Getter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
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

    private int quantity;

    public CartItemEntity(CartEntity cart, MenuEntity menu, int quantity) {
        this.cart = cart;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static CartItemEntity of (CartEntity cart, MenuEntity menu, int quantity) {
        return new CartItemEntity(cart, menu, quantity);
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



}