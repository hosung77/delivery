package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_menu")
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class MenuEntity extends BaseTimeEntity{
    public enum Status {
        SELLING, SOLD_OUT, DELETED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false)
    private int price;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    public void update(String name, Integer price, Status status) {
        this.name = name;
        this.price = price;
        this.status = status;
    }
    public void delete(){
        this.status = Status.DELETED;
    }
    public void soldOut(){
        this.status = Status.SOLD_OUT;
    }
}
