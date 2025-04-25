package com.example.delivery.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {
    private String comment;

    @Min(1)
    @Max(5)
    private int rating;
}
