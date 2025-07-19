package dev.berke.app.product.dto;

import java.util.List;

public record AutocompleteSuggestionResponse(
        List<String> suggestions
) {}