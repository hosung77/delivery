package com.example.delivery.controller.order;

import com.example.delivery.config.annotation.AdminOnlyLog;
import com.example.delivery.dto.cart.response.GetCartResponseDto;
import com.example.delivery.dto.order.response.ResponseOrderUpdateDto;
import com.example.delivery.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @AdminOnlyLog
    ResponseEntity<GetCartResponseDto> orderCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = (String) authentication.getPrincipal();
        Long userId = Long.parseLong(id);

        GetCartResponseDto orderedMenu = orderService.orderCart(userId);

        return ResponseEntity.ok().body(orderedMenu);
    }

    @PatchMapping("/{orderId}")
    @AdminOnlyLog
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId,
                                         @RequestParam(required = true) String status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String id = (String) auth.getPrincipal(); // 토큰에 들어있던 subject 값
        Long userId = Long.parseLong(id); // String → Long 변환

        ResponseOrderUpdateDto dto = orderService.updateOrder(orderId, status, userId);

        return ResponseEntity.ok().body(dto);

    }
}
