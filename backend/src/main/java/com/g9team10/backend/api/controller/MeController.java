package com.g9team10.backend.api.controller;

import com.g9team10.backend.api.dto.response.ContentResponseDTO;
import com.g9team10.backend.api.dto.response.ContentSummaryDTO;
import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.User;
import com.g9team10.backend.domain.model.valueObject.UserContentTagSummary;
import com.g9team10.backend.domain.service.FavoriteService;
import com.g9team10.backend.domain.service.HistoryService;
import com.g9team10.backend.domain.service.UserContentTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/me")
@Tag(name = "Listagem", description = "Lista: Favoritos, Histórico e Tags")
public class MeController {

    private final FavoriteService favoriteService;
    private final HistoryService historyService;
    private final UserContentTagService userContentTagService;

    @GetMapping("/favorites")
    @Operation(
            summary = "Lista os conteúdos favoritos"
    )
    @ApiResponse(
            responseCode = "204",
            description = "A busca dos Favoritos feita com sucesso!"
    )
    @ApiResponse(
            responseCode = "400",
            description = "A busca dos favoritos falhou!"
    )
    public ResponseEntity<List<ContentSummaryDTO>> listFavorites(@AuthenticationPrincipal User user){
        List<Content> favorites = favoriteService.list(user.getId());
        List<ContentSummaryDTO> response = favorites.stream()
                .map(ContentSummaryDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    @Operation(
            summary = "Lista o Histórico de busca"
    )
    @ApiResponse(
            responseCode = "204",
            description = "A busca do Histórico feita com sucesso!"
    )
    @ApiResponse(
            responseCode = "400",
            description = "A busca do histórico falhou!"
    )
    public ResponseEntity<List<ContentSummaryDTO>> listHistory(@AuthenticationPrincipal User user){
        List<Content> history = historyService.list(user.getId());
        List<ContentSummaryDTO> response = history.stream()
                .map(ContentSummaryDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/tags")
    @Operation(
            summary = "Lista as Tags"
    )
    @ApiResponse(
            responseCode = "204",
            description = "A busca das Tags feita com sucesso!"
    )
    @ApiResponse(
            responseCode = "400",
            description = "A busca das Tags falhou!"
    )
    public ResponseEntity<List<UserContentTagSummary>> listUserTags(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(userContentTagService.listUserTags(user));
    }

}
