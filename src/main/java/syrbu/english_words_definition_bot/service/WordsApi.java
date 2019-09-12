package syrbu.english_words_definition_bot;


import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

public class WordsApi {

    private static final String[] PARTS_OF_SPEECH = {
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

    public static void main(String[] args) throws IOException, UnirestException {
        String word = "man";
        System.out.println(getDefinitions(word));
    }

    public static List<String> getDefinitions(String wordToSeek) {
        List<String> respond = new ArrayList<>();
        HttpResponse<String> response;
        try {
            response = Unirest.get(String.format("https://wordsapiv1.p.rapidapi.com/words/%s/definitions", wordToSeek))
                    .header("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                    .header("x-rapidapi-key", "9355a85c0dmsh267c1d12800ccdep1c2226jsn44c911ef8012")
                    .asString();
            String body = response.getBody();
            Gson gson = new Gson();
            Word word;
            word = gson.fromJson(body, Word.class);

            List<Definition> definitions = word.getDefinitions();
            if (definitions == null) {
                respond.add("Sorry, but I can't recognize the word");
            } else {
                List<String> sortedPartsOfSpeech = getSortedPartsOfSpeechOfCurrentWord(definitions);
                respond = addDefinitions(sortedPartsOfSpeech, definitions);
            }

        } catch (UnirestException e) {
            e.printStackTrace();
            respond.add("Whoops, there is something wrong...try a bit later");
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
                result.add("other");
                for (Definition definition : definitions) {
                    if (definition.getPartOfSpeech() == null) {
                        result.add("- " + definition.getDefinition());
                    }
                }
            } else {
                result.add(partOfSpeech);
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
