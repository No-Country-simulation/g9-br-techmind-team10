package com.g9team10.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserContentTagRequestDTO(
        @NotBlank
        @Size(max = 80)
        String name
) {
}