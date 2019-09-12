package syrbu.english_words_definition_bot.model;

import java.util.List;

public class Word {
    private String word;
    private List<Definition> definitions;

    public Word() {
    }

    public Word(String word, List<Definition> definitions) {
        this.word = word;
        this.definitions = definitions;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", definitions=" + definitions +
                '}';
    }
}
