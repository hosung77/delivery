package com.example.delivery.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_user")
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
@ToString
public class UserEntity extends BaseTimeEntity{
    public enum Role{
        ADMIN, USER, OWNER
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 10)
    private String name;
    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role roles;

    @Column
    private boolean deleted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<StoreEntity> stores = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ReviewEntity> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<OrderEntity> orders = new ArrayList<>();

}
