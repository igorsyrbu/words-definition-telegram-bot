package syrbu.english_words_definition_bot.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "aws.polly")
public class AwsPollyProperties {
    private String accessKey;
    private String secretKey;
    private String region;
    private String voiceName;
    private boolean isIam;
}