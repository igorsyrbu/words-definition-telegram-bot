package syrbu.english_words_definition_bot.aws.service;

import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class AmazonPollyService {

    private final AmazonPolly polly;
    private final Voice voice;

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
