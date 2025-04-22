package com.example.delivery.config.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldErrorDTO> fieldErrors;

    public static ErrorResponseDTO from(ErrorCode errorCode, String path) {
        return ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(errorCode.getStatus().value())
                .error(errorCode.getStatus().name())
                .message(errorCode.getMessage())
                .path(path)
                .build();
    }

    public static ErrorResponseDTO from(MethodArgumentNotValidException ex, String path) {
        List<FieldErrorDTO> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorDTO(error.getField(), error.getDefaultMessage()))
                .toList();

        return ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("BAD_REQUEST")
                .message("잘못된 입력값입니다")
                .path(path)
                .fieldErrors(fieldErrors)
                .build();
    }
}
