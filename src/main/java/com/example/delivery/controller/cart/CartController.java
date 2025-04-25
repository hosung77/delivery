package com.example.delivery.controller.cart;

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
        String id = (String) authentication.getPrincipal();
        Long userId = Long.parseLong(id);

        GetCartResponseDto dto = cartService.viewCart(userId);

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("{/menuId}")
    ResponseEntity<String> addCartItem(@PathVariable Long menuId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = (String) authentication.getPrincipal();
        Long userId = Long.parseLong(id);

        cartService.addCartItem(menuId, userId);

        return ResponseEntity.ok().body("메뉴가 성공적으로 추가되었습니다.");

    }

    @PatchMapping("{/menuId}")
    ResponseEntity<String> decreaseCartItem(@PathVariable Long menuId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = (String) authentication.getPrincipal();
        Long userId = Long.parseLong(id);

        cartService.decreaseCartItem(menuId, userId);

        return ResponseEntity.ok().body("메뉴가 성공적으로 감소되었습니다.");
    }

    @DeleteMapping
    ResponseEntity<String> deleteCartItem() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = (String) authentication.getPrincipal();
        Long userId = Long.parseLong(id);

        cartService.deleteCartItem(userId);

        return ResponseEntity.ok().body("카트 초기화가 완료되었습니다.");
    }


}

