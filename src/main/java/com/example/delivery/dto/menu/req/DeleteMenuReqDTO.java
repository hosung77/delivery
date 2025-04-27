package com.example.delivery.dto.menu.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeleteMenuReqDTO {
    @NotBlank(message = "공백일 수 없습니다.")
    private Long menuId;
    @NotBlank(message = "공백일 수 없습니다.")
    private Long storeId;

}
