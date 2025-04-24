package com.example.delivery.controller.order;

import com.example.delivery.dto.order.RequestOrderDto;
import com.example.delivery.dto.order.ResponseOrderDto;
import com.example.delivery.entity.OrderEntity;
import com.example.delivery.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/users/stores/{storeId}/orders")
    public ResponseEntity<ResponseOrderDto> createOrder(@RequestBody RequestOrderDto request,
                                                        @PathVariable Integer storeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String Id = (String) auth.getPrincipal(); // 토큰에 들어있던 subject 값
        int userId = Integer.parseInt(Id);

        ResponseOrderDto responseOrderDto = orderService.createOrder(request, storeId, userId);

        return ResponseEntity.ok().body(responseOrderDto);
    }

}
