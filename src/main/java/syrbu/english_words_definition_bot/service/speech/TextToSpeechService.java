package syrbu.english_words_definition_bot.service.speech;

import java.io.InputStream;
import java.util.Optional;

public interface TextToSpeechService {

    Optional<InputStream> synthesize(String text);

}
