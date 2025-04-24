package com.example.delivery.service.review;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.config.error.ErrorCode;
import com.example.delivery.dto.review.ReviewRequestDTO;
import com.example.delivery.dto.review.ReviewResponseDTO;
import com.example.delivery.entity.*;
import com.example.delivery.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public void createReview(ReviewRequestDTO dto, OrderEntity order, UserEntity user) {
        if (!"배달 완료".equals(order.getStatus())) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        ReviewEntity review = new ReviewEntity(
                dto.getComment(),
                dto.getRating(),
                order.getStore(),
                user,
                order
        );
        reviewRepository.save(review);
    }

    public List<ReviewResponseDTO> getReviews(Long storeId, int min, int max) {
        return reviewRepository.findByStore_StoreIdAndRatingBetweenOrderByCreatedAtDesc(storeId, min, max)
                .stream()
                .map(r -> new ReviewResponseDTO(
                        r.getReviewId(),
                        r.getComment(),
                        r.getRating(),
                        r.getUser().getName(),
                        r.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
