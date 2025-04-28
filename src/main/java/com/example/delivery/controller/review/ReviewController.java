package com.example.delivery.controller.review;

import com.example.delivery.dto.review.ReviewRequestDTO;
import com.example.delivery.dto.review.ReviewResponseDTO;
import com.example.delivery.entity.OrderEntity;
import com.example.delivery.entity.UserEntity;
import com.example.delivery.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping
    public ResponseEntity<Void> createReview(
            @Valid @RequestBody ReviewRequestDTO dto,
            @RequestAttribute UserEntity user,
            @RequestAttribute OrderEntity order
    ) {
        reviewService.createReview(dto, order, user);
        return ResponseEntity.ok().build();
    }

    // 리뷰 조회 (가게별 + 별점 범위로 필터링)
    @GetMapping("/{storeId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviews(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") int min,
            @RequestParam(defaultValue = "5") int max
    ) {
        List<ReviewResponseDTO> reviews = reviewService.getReviews(storeId, min, max);
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDTO dto,
            @RequestAttribute UserEntity user
    ) {
        reviewService.updateReview(reviewId, dto, user);
        return ResponseEntity.ok().build();
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @RequestAttribute UserEntity user
    ) {
        reviewService.deleteReview(reviewId, user);
        return ResponseEntity.ok().build();
    }
}
