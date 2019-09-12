package syrbu.english_words_definition_bot.service;


import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import syrbu.english_words_definition_bot.model.Definition;
import syrbu.english_words_definition_bot.model.Word;

import java.util.*;

import static syrbu.english_words_definition_bot.service.ApiConstants.*;

public class WordsApi {
    public static List<String> getDefinitions(String wordToSeek) {
        List<String> respond = new ArrayList<>();
        HttpResponse<String> response;
        try {
            response = Unirest.get(String.format(WORDS_API_ENTRY_POINT, wordToSeek))
                    .header("x-rapidapi-host", RAPID_API_HOST)
                    .header("x-rapidapi-key", RAPID_API_KEY)
                    .asString();
            String body = response.getBody();
            Gson gson = new Gson();
            Word word;
            word = gson.fromJson(body, Word.class);

            List<Definition> definitions = word.getDefinitions();
            if (definitions == null || definitions.size() == 0) {
                respond.add(INVALID_WORD);
            } else {
                List<String> sortedPartsOfSpeech = getSortedPartsOfSpeechOfCurrentWord(definitions);
                respond = addDefinitions(sortedPartsOfSpeech, definitions);
            }

        } catch (UnirestException e) {
            e.printStackTrace();
            respond.add(API_ERROR_MESSAGE);
        }
        return respond;
    }

    private static List<String> getSortedPartsOfSpeechOfCurrentWord(List<Definition> definitions) {
        Set<String> partsOfSpeechOfCurrentWord = new HashSet<>();
        for (Definition definition : definitions) {
            partsOfSpeechOfCurrentWord.add(definition.getPartOfSpeech());
        }

        List<String> sortedPartsOfSpeech = new ArrayList<>(Arrays.asList(PARTS_OF_SPEECH));
        sortedPartsOfSpeech.retainAll(partsOfSpeechOfCurrentWord);
        return sortedPartsOfSpeech;
    }

    private static List<String> addDefinitions(List<String> currentPartsOfSpeech, List<Definition> definitions) {
        List<String> result = new ArrayList<>();
        for (String partOfSpeech : currentPartsOfSpeech) {
            if (partOfSpeech == null) {
                result.add(String.format("*%s*", "other"));
                for (Definition definition : definitions) {
                    if (definition.getPartOfSpeech() == null) {
                        result.add("- " + definition.getDefinition());
                    }
                }
            } else {
                result.add(String.format("*%s*", partOfSpeech));
                for (Definition definition : definitions) {
                    if (definition.getPartOfSpeech() != null && definition.getPartOfSpeech().equals(partOfSpeech)) {
                        result.add("- " + definition.getDefinition());
                    }
                }
            }
        }
        return result;
    }
}
