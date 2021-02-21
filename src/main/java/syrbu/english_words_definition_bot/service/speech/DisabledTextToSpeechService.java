package syrbu.english_words_definition_bot.service.speech;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Optional;

@Log4j2
@Service
@ConditionalOnProperty(prefix = "telegram.bot", name = "speech-to-text", havingValue = "disabled")
public class DisabledTextToSpeechService implements TextToSpeechService {

    @PostConstruct
    public void init(){
        log.info("Text to speech service is disabled");
    }

    @Override
    public Optional<InputStream> synthesize(String text) {
        return Optional.empty();
    }
}
