package com.example.delivery.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponseDTO {
    private Long reviewId;
    private String comment;
    private int rating;
    private String username;
    private LocalDateTime createdAt;
}
