package com.example.delivery.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {
    private Long orderId;
    private int rating;
    private String comment;
}
