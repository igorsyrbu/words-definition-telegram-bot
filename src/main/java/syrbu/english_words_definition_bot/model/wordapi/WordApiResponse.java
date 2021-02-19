package syrbu.english_words_definition_bot.model.wordapi;

import lombok.Data;

import java.util.List;

@Data
public class WordApiResponse {
    private List<WordApiDefinition> results;
    private List<String> synonyms;
    private List<String> examples;

    public static WordApiResponse empty() {
        return new WordApiResponse();
    }
}
