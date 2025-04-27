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
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @AdminOnlyLog
    ResponseEntity<GetCartResponseDto> orderCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); // getPrincipal()이 Long 타입이라면, 이를 Long으로 캐스팅

        GetCartResponseDto orderedMenu = orderService.orderCart(userId);

        return ResponseEntity.ok().body(orderedMenu);
    }

    @PatchMapping("/{orderId}")
    @AdminOnlyLog
    public ResponseEntity<ResponseOrderUpdateDto> updateOrder(@PathVariable Long orderId,
                                         @RequestParam(required = true) String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); // getPrincipal()이 Long 타입이라면, 이를 Long으로 캐스팅

        ResponseOrderUpdateDto dto = orderService.updateOrder(orderId, status, userId);

        return ResponseEntity.ok().body(dto);
    }
}
