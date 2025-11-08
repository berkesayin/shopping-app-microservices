package dev.berke.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.lang.NonNull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.time.Duration;

@Configuration
@EnableElasticsearchRepositories(basePackages = {
    "dev.berke.app.productsearch.domain.repository",
    "dev.berke.app.ordersearch.domain.repository"
})
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris}")
    private String uris;

    @Value("${spring.elasticsearch.username}")
    private String username;

    @Value("${spring.elasticsearch.password}")
    private String password;

    @Override
    @NonNull
    public ClientConfiguration clientConfiguration() {

        return ClientConfiguration.builder()
                .connectedTo(uris.replace("https://", ""))

                .usingSsl(createTrustAllSslContext(), (hostname, session) -> true)
                .withBasicAuth(username, password)
                .withConnectTimeout(Duration.ofSeconds(10))
                .withSocketTimeout(Duration.ofSeconds(5))

                .build();
    }

    private SSLContext createTrustAllSslContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create a trusting SSL context for Elasticsearch", e);
        }
    }
}
