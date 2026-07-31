package com.g9team10.backend.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContentRequestDTO(@NotBlank String title, @NotBlank @Size(min = 10) String text) {
}
