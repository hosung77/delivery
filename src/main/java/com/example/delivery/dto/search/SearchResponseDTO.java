package com.example.delivery.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDTO {

    private List<StoreDTO> stores;
    private List<MenuDTO> menus;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreDTO {
        private Long storeId;
        private String storeName;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuDTO {
        private Long menuId;
        private String menuName;
    }
}
