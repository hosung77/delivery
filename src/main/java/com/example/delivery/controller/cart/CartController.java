package com.example.delivery.controller.cart;

import com.example.delivery.dto.cart.response.CartResponseDto;
import com.example.delivery.dto.cart.response.GetCartResponseDto;
import com.example.delivery.dto.cart.response.OrderedMenuResponseDto;
import com.example.delivery.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    ResponseEntity<GetCartResponseDto> viewCart() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); // getPrincipal()이 Long 타입이라면, 이를 Long으로 캐스팅

        GetCartResponseDto dto = cartService.viewCart(userId);

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/{menuId}")
    ResponseEntity<CartResponseDto> addCartItem(@PathVariable Long menuId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); // getPrincipal()이 Long 타입이라면, 이를 Long으로 캐스팅

        CartResponseDto dto = cartService.addCartItem(menuId, userId);

        return ResponseEntity.ok().body(dto);
    }

    @PatchMapping("/{menuId}")
    ResponseEntity<CartResponseDto> decreaseCartItem(@PathVariable Long menuId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); // getPrincipal()이 Long 타입이라면, 이를 Long으로 캐스팅

        CartResponseDto dto = cartService.decreaseCartItem(menuId, userId);

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping
    ResponseEntity<CartResponseDto> deleteCartItem() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); // getPrincipal()이 Long 타입이라면, 이를 Long으로 캐스팅

        CartResponseDto dto = cartService.deleteCartItem(userId);

        return ResponseEntity.ok().body(dto);
    }


}

