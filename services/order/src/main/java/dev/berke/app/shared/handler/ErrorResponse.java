package dev.berke.app.shared.handler;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {
}
