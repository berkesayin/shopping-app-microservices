package dev.berke.app.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.http.HttpHeaders;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(FeignClientInterceptor.class);

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                logger.debug("order service FeignClientInterceptor: {}",
                        template.feignTarget().url() + template.url());

                template.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
            } else {
                logger.warn("Authorization header not found or not a Bearer token on " +
                                "path: {}. Cannot propagate JWT for Feign call to: {} {}. " ,
                        request.getRequestURI(), template.feignTarget().url(), template.url());
            }
        }
    }
}