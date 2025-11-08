package dev.berke.app.productsearch.api.dto;

import java.util.List;

public record AutocompleteSuggestionResponse(
        List<String> suggestions
) {}