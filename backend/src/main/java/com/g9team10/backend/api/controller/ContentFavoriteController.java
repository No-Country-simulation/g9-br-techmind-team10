package com.g9team10.backend.api.controller;

import com.g9team10.backend.domain.model.User;
import com.g9team10.backend.domain.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/content")
public class ContentFavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{id}/favorite")
    public ResponseEntity<Void> checkFavorite(@PathVariable Long id, @AuthenticationPrincipal User user) {
        favoriteService.check(user, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/favorite")
    public ResponseEntity<Void> uncheckFavorite(@PathVariable Long id, @AuthenticationPrincipal User user) {
        favoriteService.uncheck(user, id);
        return ResponseEntity.noContent().build();
    }
}
