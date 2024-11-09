package config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties for AWS SNS, loaded from application.yml.
 * Manages SNS topic ARN and region for easy configuration across environments.
 */
@Configuration
@ConfigurationProperties(prefix = "aws.sns")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AwsConfigProperties {
    private String topicArn;
    private String region;
}

