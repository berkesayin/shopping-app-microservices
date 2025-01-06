package dev.berke.app.iyzipay;

import com.iyzipay.Options;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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

        System.out.println("Iyzipay Options Configuration initialized: ");
        System.out.println("API Key: " + options.getApiKey());
        System.out.println("Secret Key: " + options.getSecretKey());
        System.out.println("Base URL: " + options.getBaseUrl());

        return options;
    }
}