package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tb_order_menu")
@Getter
@NoArgsConstructor
public class OrderMenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderMenuId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuEntity menu;

    private int quantity;
    private int price;

    public int getTotalPrice(){
        return price * quantity;
    }

    public OrderMenuEntity(OrderEntity order, MenuEntity menu, int quantity, int price) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
        this.price = price;
    }
}
