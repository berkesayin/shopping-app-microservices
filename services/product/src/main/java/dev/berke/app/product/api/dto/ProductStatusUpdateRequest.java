package dev.berke.app.product.api.dto;

import jakarta.validation.constraints.NotNull;

public record ProductStatusUpdateRequest(
        @NotNull(message = "Status cannot be null")
        Boolean status
) {
}