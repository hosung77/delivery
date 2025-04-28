package com.example.delivery.dto.menu.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteMenuReqDTO {
    @NotNull(message = "공백일 수 없습니다.")
    private Long menuId;
    @NotNull(message = "공백일 수 없습니다.")
    private Long storeId;

}
