package com.example.delivery.controller.order;

import com.example.delivery.config.annotation.AdminOnlyLog;
import com.example.delivery.dto.cart.response.GetCartResponseDto;
import com.example.delivery.dto.order.response.ResponseOrderUpdateDto;
import com.example.delivery.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @AdminOnlyLog
    public ResponseEntity<GetCartResponseDto> orderCart(@AuthenticationPrincipal String userIdStr) {
        Long userId = Long.parseLong(userIdStr);

        GetCartResponseDto orderedMenu = orderService.orderCart(userId);

        return ResponseEntity.ok().body(orderedMenu);
    }

    @PatchMapping("/{orderId}")
    @AdminOnlyLog
    public ResponseEntity<ResponseOrderUpdateDto> updateOrder(@PathVariable Long orderId,
                                                              @RequestParam(required = true) String status,
                                                              @AuthenticationPrincipal String userIdStr) {
        Long userId = Long.parseLong(userIdStr);

        ResponseOrderUpdateDto dto = orderService.updateOrder(orderId, status, userId);

        return ResponseEntity.ok().body(dto);
    }
}
