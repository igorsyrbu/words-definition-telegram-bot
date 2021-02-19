package syrbu.english_words_definition_bot.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "rapid-api")
public class RapidApiProperties {
    private String host;
    private String applicationKey;
}