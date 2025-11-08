package dev.berke.app.payment.infrastructure.paymentprovider.iyzipay;

import com.iyzipay.Options;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class IyzipayConfig {

    @Value("${iyzipay.api.key}")
    private String apiKey;

    @Value("${iyzipay.secret.key}")
    private String secretKey;

    @Value("${iyzipay.base.url}")
    private String baseUrl;

    @Bean
    public Options useIyzipayOptions() {
        Options options = new Options();
        options.setApiKey(apiKey);
        options.setSecretKey(secretKey);
        options.setBaseUrl(baseUrl);

        log.info("Iyzipay Options Configuration initialized: ");
        log.info("Base URL: " + options.getBaseUrl());

        return options;
    }
}