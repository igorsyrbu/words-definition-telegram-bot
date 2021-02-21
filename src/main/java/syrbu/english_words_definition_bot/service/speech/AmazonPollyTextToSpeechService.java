package syrbu.english_words_definition_bot.service.speech;

import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "telegram.bot", name = "speech-to-text", havingValue = "aws")
public class AmazonPollyTextToSpeechService implements TextToSpeechService {

    private final AmazonPolly polly;
    private final Voice voice;

    @PostConstruct
    public void init(){
        log.info("Amazon Polly text to speech service has been initialized");
    }

    @Override
    public Optional<InputStream> synthesize(String text) {
        try {
            SynthesizeSpeechRequest speechRequest = new SynthesizeSpeechRequest()
                    .withEngine(Engine.Neural)
                    .withText(text)
                    .withVoiceId(voice.getId())
                    .withOutputFormat(OutputFormat.Mp3);
            SynthesizeSpeechResult speechResult = polly.synthesizeSpeech(speechRequest);
            return Optional.of(speechResult.getAudioStream());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return Optional.empty();
        }
    }
}
