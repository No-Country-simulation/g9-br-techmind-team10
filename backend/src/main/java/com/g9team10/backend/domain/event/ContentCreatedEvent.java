package com.g9team10.backend.domain.event;

import com.g9team10.backend.domain.model.Content;

public record ContentCreatedEvent(Content content) {
}
