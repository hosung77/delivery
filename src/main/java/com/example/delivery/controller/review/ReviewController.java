package com.example.delivery.controller.review;

import com.example.delivery.dto.review.ReviewRequestDTO;
import com.example.delivery.dto.review.ReviewResponseDTO;
import com.example.delivery.entity.OrderEntity;
import com.example.delivery.entity.UserEntity;
import com.example.delivery.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public void createReview(
            @RequestBody ReviewRequestDTO dto,
            @RequestAttribute UserEntity user,
            @RequestAttribute OrderEntity order
    ) {
        reviewService.createReview(dto, order, user);
    }

    @GetMapping("/{storeId}")
    public List<ReviewResponseDTO> getReviews(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") int min,
            @RequestParam(defaultValue = "5") int max
    ) {
        return reviewService.getReviews(storeId, min, max);
    }
}
