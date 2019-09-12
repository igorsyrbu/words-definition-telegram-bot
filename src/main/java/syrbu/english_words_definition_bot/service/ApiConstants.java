package syrbu.english_words_definition_bot.service;

public class ApiConstants {
    public static final String WORDS_API_ENTRY_POINT = "{rapid_api_entry_point}";
    public static final String RAPID_API_HOST = "{rapid_api_host}";
    public static final String RAPID_API_KEY = "{rapid_api_key}";
    public static final String INVALID_WORD = "Sorry, but I can't recognize the word";
    public static final String API_ERROR_MESSAGE = "Whoops, there is something wrong...try a bit later";
    public static final String[] PARTS_OF_SPEECH = {
            "noun",
            "pronoun",
            "adjective",
            "verb",
            "adverb",
            "preposition",
            "conjunction",
            "interjection",
            null
    };
}
