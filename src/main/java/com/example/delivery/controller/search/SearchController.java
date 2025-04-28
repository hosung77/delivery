package com.example.delivery.controller.search;

import com.example.delivery.config.error.CustomException;
import com.example.delivery.config.error.ErrorCode;
import com.example.delivery.dto.search.SearchResponseDTO;
import com.example.delivery.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponseDTO> search(@RequestParam(required = false) String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new CustomException(ErrorCode.KEYWORD_REQUIRED);
        }

        SearchResponseDTO result = searchService.search(keyword);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<String>> getPopularKeywords(
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<String> popularKeywords = searchService.getPopularKeywords(limit);
        return ResponseEntity.ok(popularKeywords);
    }


}
