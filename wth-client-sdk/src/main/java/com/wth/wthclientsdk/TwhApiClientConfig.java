package com.wth.wthclientsdk;

import com.wth.wthclientsdk.client.TApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("wthapi.client")
@Data
@ComponentScan
public class TwhApiClientConfig {

    private String accessKey;
    private String secretKey;

    @Bean
    public TApiClient tApiClient() {
        return new TApiClient(accessKey, secretKey);
    }



}
