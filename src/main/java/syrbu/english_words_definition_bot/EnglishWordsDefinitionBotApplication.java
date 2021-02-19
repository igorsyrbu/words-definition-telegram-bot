package syrbu.english_words_definition_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EnglishWordsDefinitionBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnglishWordsDefinitionBotApplication.class, args);
    }
}
