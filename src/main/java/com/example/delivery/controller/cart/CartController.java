package com.example.delivery.controller.cart;

import com.example.delivery.dto.cart.response.GetCartResponseDto;
import com.example.delivery.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    ResponseEntity<GetCartResponseDto> viewCart() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = (String) authentication.getPrincipal();
        Long userId = Long.parseLong(id);

        GetCartResponseDto dto = cartService.viewCart(userId);

        return ResponseEntity.ok().body(dto);
    }

}

