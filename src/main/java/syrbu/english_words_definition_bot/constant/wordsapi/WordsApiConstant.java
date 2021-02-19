package syrbu.english_words_definition_bot.constant.wordsapi;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WordsApiConstant {

    public static final String DEFINITION_ENDPOINT = "https://wordsapiv1.p.rapidapi.com/words/{word}";
    public static final String SYNONYMS_ENDPOINT = "https://wordsapiv1.p.rapidapi.com/words/{word}/synonyms";
    public static final String EXAMPLES_ENDPOINT = "https://wordsapiv1.p.rapidapi.com/words/{word}/examples";
}
