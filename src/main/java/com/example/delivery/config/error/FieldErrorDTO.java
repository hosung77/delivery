package com.example.delivery.config.error;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FieldErrorDTO {
    private String field;
    private String message;
}