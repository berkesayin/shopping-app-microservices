package dev.berke.app.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductStatusUpdateRequest(
        @NotNull(message = "Status cannot be null")
        @Min(value = 0, message = "Status must be 0 or 1")
        @Max(value = 1, message = "Status must be 0 or 1")
        Integer status
) {
}