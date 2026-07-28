package com.g9team10.backend.api.controller;

import com.g9team10.backend.api.dto.response.ContentSummaryDTO;
import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.User;
import com.g9team10.backend.domain.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/content/history")
public class ContentHistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<List<ContentSummaryDTO>> listFavorites(@AuthenticationPrincipal User user){
        List<Content> history = historyService.list(user.getId());
        List<ContentSummaryDTO> response = history.stream()
                .map(ContentSummaryDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }
}
