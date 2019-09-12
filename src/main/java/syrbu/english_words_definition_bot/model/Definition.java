package syrbu.english_words_definition_bot.model;

public class Definition {
    private String definition;
    private String partOfSpeech;

    public Definition() {
    }

    public Definition(String definition, String partOfSpeech) {
        this.definition = definition;
        this.partOfSpeech = partOfSpeech;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    @Override
    public String toString() {
        return "Definition{" +
                "definition='" + definition + '\'' +
                ", partOfSpeech='" + partOfSpeech + '\'' +
                '}';
    }
}
