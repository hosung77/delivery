package com.example.delivery.controller.order;

import com.example.delivery.dto.order.request.RequestOrderDto;

import com.example.delivery.dto.order.response.ResponseOrderDto;
import com.example.delivery.dto.order.response.ResponseOrderUpdateDto;
import com.example.delivery.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{storeId}/orders")
    public ResponseEntity<ResponseOrderDto> createOrder(@RequestBody RequestOrderDto request,
                                                        @PathVariable Long storeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String id = (String) auth.getPrincipal(); // 토큰에 들어있던 subject 값
        Long userId = Long.parseLong(id); // String → Long 변환

        ResponseOrderDto responseOrderDto = orderService.createOrder(request, storeId, userId);

        return ResponseEntity.ok().body(responseOrderDto);
    }

    @PatchMapping("/{storeId}/orders/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Long storeId,
                                         @PathVariable Long orderId,
                                         @RequestParam(required = true) String status) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String id = (String) auth.getPrincipal(); // 토큰에 들어있던 subject 값
        Long userId = Long.parseLong(id); // String → Long 변환

        ResponseOrderUpdateDto dto = orderService.updateOrder(storeId, orderId, status, userId);

        return ResponseEntity.ok().body(dto);

    }
}
