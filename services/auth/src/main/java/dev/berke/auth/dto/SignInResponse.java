package dev.berke.auth.dto;

import java.util.List;

public record SignInResponse(
        String token,
        String type,
        Long id,
        String customerId,
        String username,
        String email,
        List<String> roles
) {

    // custom constructor to set default value of "String type" = "Bearer"
    public SignInResponse(
            String token,
            Long id,
            String customerId,
            String username,
            String email,
            List<String> roles
    ) {
        this(token, "Bearer", id, customerId, username, email, roles);
    }
}
