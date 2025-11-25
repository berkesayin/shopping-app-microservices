package dev.berke.app.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(FeignClientInterceptor.class);

    private static final String HEADER_CUSTOMER_ID = "X-User-CustomerId";
    private static final String HEADER_ROLES = "X-User-Roles";
    private static final String HEADER_EMAIL = "X-User-Email";

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                template.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
            }

            String customerId = request.getHeader(HEADER_CUSTOMER_ID);
            if (customerId != null) {
                template.header(HEADER_CUSTOMER_ID, customerId);
            }

            String roles = request.getHeader(HEADER_ROLES);
            if (roles != null) {
                template.header(HEADER_ROLES, roles);
            }

            String email = request.getHeader(HEADER_EMAIL);
            if (email != null) {
                template.header(HEADER_EMAIL, email);
            }

            logger.info("Sent headers for Feign Call to {}: Auth={}, CustomerId={}",
                    template.feignTarget().url() + template.url(),
                    (authorizationHeader != null ? "auth header exists" : "does not exist"),
                    customerId);
        }
    }
}