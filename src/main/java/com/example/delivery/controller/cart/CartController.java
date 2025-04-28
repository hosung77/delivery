package com.example.delivery.controller.cart;

import com.example.delivery.dto.cart.response.CartResponseDto;
import com.example.delivery.dto.cart.response.GetCartResponseDto;
import com.example.delivery.dto.cart.response.OrderedMenuResponseDto;
import com.example.delivery.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    ResponseEntity<GetCartResponseDto> viewCart(@AuthenticationPrincipal String userIdStr) {
        Long userId = Long.parseLong(userIdStr);

        GetCartResponseDto dto = cartService.viewCart(userId);

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/{menuId}")
    ResponseEntity<CartResponseDto> addCartItem(@PathVariable Long menuId,
                                                @AuthenticationPrincipal String userIdStr) {
        Long userId = Long.parseLong(userIdStr);

        CartResponseDto dto = cartService.addCartItem(menuId, userId);

        return ResponseEntity.ok().body(dto);
    }

    @PatchMapping("/{menuId}")
    ResponseEntity<CartResponseDto> decreaseCartItem(@PathVariable Long menuId,
                                                     @AuthenticationPrincipal String userIdStr) {
        Long userId = Long.parseLong(userIdStr);

        CartResponseDto dto = cartService.decreaseCartItem(menuId, userId);

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping
    ResponseEntity<CartResponseDto> deleteCartItem(@AuthenticationPrincipal String userIdStr) {
        Long userId = Long.parseLong(userIdStr);

        CartResponseDto dto = cartService.deleteCartItem(userId);

        return ResponseEntity.ok().body(dto);
    }


}

