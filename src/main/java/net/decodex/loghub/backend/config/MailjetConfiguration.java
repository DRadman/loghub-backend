package net.decodex.loghub.backend.config;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailjetConfiguration {

    @Value("${mailjet.public-key}")
    String publicKey;

    @Value("${mailjet.private-key}")
    String privateKey;

    @Bean
    public MailjetClient mailjetClient() {
        ClientOptions options = ClientOptions.builder()
                .apiKey(publicKey)
                .apiSecretKey(privateKey)
                .build();

        return new MailjetClient(options);
    }

}
