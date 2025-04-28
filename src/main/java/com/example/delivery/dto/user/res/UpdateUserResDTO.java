package com.example.delivery.dto.user.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateUserResDTO {
    private String email;
    private String name;
}
