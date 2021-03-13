package syrbu.english_words_definition_bot.model.wordapi;

import lombok.Data;

import java.util.List;

@Data
public class WordApiResponse {
    private List<Result> results;

    public static WordApiResponse empty() {
        return new WordApiResponse();
    }
}
