package dev.berke.app.product;

import jakarta.validation.constraints.NotNull;

public record ProductStatusUpdateRequest(
        @NotNull(message = "Status cannot be null")
        Boolean status
) {
}