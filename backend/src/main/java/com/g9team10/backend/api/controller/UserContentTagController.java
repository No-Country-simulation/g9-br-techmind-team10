package com.g9team10.backend.api.controller;

import com.g9team10.backend.api.dto.response.ContentSummaryDTO;
import com.g9team10.backend.api.dto.request.UserContentTagRequestDTO;
import com.g9team10.backend.api.dto.response.UserContentTagResponseDTO;
import com.g9team10.backend.api.dto.response.UserContentTagSummaryDTO;
import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.User;
import com.g9team10.backend.domain.model.UserContentTag;
import com.g9team10.backend.domain.service.UserContentTagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/content")
@Tag(name = "Tags pessoais", description = "Endpoints responsáveis pelas tags personalizadas do usuário")
public class UserContentTagController {

    private final UserContentTagService userContentTagService;

    @GetMapping("/{contentId}/personal-tags")
    public ResponseEntity<List<UserContentTagResponseDTO>> listByContent(
            @PathVariable Long contentId,
            @AuthenticationPrincipal User user
    ) {
        List<UserContentTagResponseDTO> response = userContentTagService
                .listByContent(user, contentId)
                .stream()
                .map(UserContentTagResponseDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{contentId}/personal-tags")
    public ResponseEntity<UserContentTagResponseDTO> create(
            @PathVariable Long contentId,
            @RequestBody @Valid UserContentTagRequestDTO request,
            @AuthenticationPrincipal User user
    ) {
        UserContentTag tag = userContentTagService.create(user, contentId, request);

        return ResponseEntity.ok(UserContentTagResponseDTO.fromEntity(tag));
    }

    @DeleteMapping("/{contentId}/personal-tags/{tagId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long contentId,
            @PathVariable Long tagId,
            @AuthenticationPrincipal User user
    ) {
        userContentTagService.delete(user, contentId, tagId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/personal-tags")
    public ResponseEntity<List<UserContentTagSummaryDTO>> listUserTags(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(userContentTagService.listUserTags(user));
    }

    @GetMapping("/personal-tags/search")
    public ResponseEntity<List<ContentSummaryDTO>> searchContentsByPersonalTags(
            @RequestParam("tags") List<String> tags,
            @AuthenticationPrincipal User user
    ) {
        List<Content> contents = userContentTagService.searchContentsByPersonalTags(user, tags);

        List<ContentSummaryDTO> response = contents.stream()
                .map(ContentSummaryDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }
}