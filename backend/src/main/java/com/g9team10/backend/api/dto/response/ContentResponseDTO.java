package com.g9team10.backend.api.dto.response;

import java.util.List;

public record ContentResponseDTO(String category,
                                 Double probability,
                                 List<String> additionalInformation,
                                 String level) {
}
