package com.example.delivery.service.review;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.config.error.ErrorCode;
import com.example.delivery.dto.review.ReviewRequestDTO;
import com.example.delivery.dto.review.ReviewResponseDTO;
import com.example.delivery.entity.OrderEntity;
import com.example.delivery.entity.ReviewEntity;
import com.example.delivery.entity.UserEntity;
import com.example.delivery.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 리뷰 작성
    public void createReview(ReviewRequestDTO dto, OrderEntity order, UserEntity user) {
        if (!"배달 완료".equals(order.getStatus())) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        if (reviewRepository.existsByOrder(order)) {
            throw new CustomException(ErrorCode.ALREADY_REVIEWED);
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

    // 리뷰 조회 (가게별 + 별점 범위)
    public List<ReviewResponseDTO> getReviews(Long storeId, int minRating, int maxRating) {
        return reviewRepository.findByStore_StoreIdAndRatingBetweenAndIsDeletedFalseOrderByCreatedAtDesc(storeId, minRating, maxRating)
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

    // 리뷰 수정
    public void updateReview(Long reviewId, ReviewRequestDTO dto, UserEntity user) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getUserId().equals(user.getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        review.update(dto.getComment(), dto.getRating());
        reviewRepository.save(review);
    }

    // 리뷰 삭제 (소프트 딜리트)
    public void deleteReview(Long reviewId, UserEntity user) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getUserId().equals(user.getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        review.delete();
        reviewRepository.save(review);
    }
}
