package syrbu.english_words_definition_bot.model.wordapi;

import lombok.Data;

import java.util.List;

@Data
public class Result {
    private String definition;
    private String partOfSpeech;
    private List<String> synonyms;
    private List<String> examples;
}
