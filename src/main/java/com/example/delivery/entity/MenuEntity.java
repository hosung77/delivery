package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_menu")
@NoArgsConstructor
@Getter
public class MenuEntity extends BaseTimeEntity{
    public enum Status {
        SELLING, SOLD_OUT
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int menuId;
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    @Column(nullable = false)
    private int price;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    public MenuEntity(String name, int price, Status status) {
        this.name = name;
        this.price = price;
        this.status = status;
    }
}
