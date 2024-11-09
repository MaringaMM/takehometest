package com.hometest.demo.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AwsConfig {

    @Bean
    public SnsClient snsClient() {
        return SnsClient.builder().build();
    }
}
