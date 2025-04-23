package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_user")
@NoArgsConstructor
@Getter
public class UserEntity extends BaseTimeEntity{
    public enum Role{
        ADMIN, USER, OWNER
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private boolean deleted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<StoreEntity> stores = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ReviewEntity> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<OrderEntity> orders = new ArrayList<>();

    protected UserEntity(String email, String password, Role role, boolean deleted) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.deleted = deleted;
    }

    public void deleted(){
        this.deleted = true;
    }
}
