package syrbu.english_words_definition_bot.constant.wordsapi;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum PartOfSpeech {
    NOUN("noun"),
    PRONOUN("pronoun"),
    ADJECTIVE("adjective"),
    VERB("verb"),
    ADVERB("adverb"),
    PREPOSITION("preposition"),
    CONJUNCTION("conjunction"),
    INTERJECTION("interjection"),
    OTHER("other");

    private final String title;

    PartOfSpeech(String title) {
        this.title = title;
    }

    public static List<String> getTitles() {
        return Arrays.stream(PartOfSpeech.values())
                .map(PartOfSpeech::getTitle)
                .collect(Collectors.toList());
    }

}
