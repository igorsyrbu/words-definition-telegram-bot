package syrbu.english_words_definition_bot.service.wordsapi;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import syrbu.english_words_definition_bot.constant.wordsapi.PartOfSpeech;
import syrbu.english_words_definition_bot.model.wordapi.Result;
import syrbu.english_words_definition_bot.model.wordapi.WordApiTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static syrbu.english_words_definition_bot.constant.bot.BotMessage.API_ERROR_MESSAGE;
import static syrbu.english_words_definition_bot.constant.bot.BotMessage.INVALID_WORD;
import static syrbu.english_words_definition_bot.constant.wordsapi.PartOfSpeech.OTHER;
import static syrbu.english_words_definition_bot.constant.wordsapi.WordsApiConstant.DEFINITION_ENDPOINT;

@Service
@RequiredArgsConstructor
public class WordsService {

    private static final String NEXT_LINE = "\n";
    private static final String SYNONYMS_TITLE = "Synonyms";
    private static final String EXAMPLES_TITLE = "Examples";

    private final WordsApiService wordsApiService;

    public String getFormattedMessage(String text) {
        return wordsApiService.executeRequest(DEFINITION_ENDPOINT, text).map(response -> {
            if (CollectionUtils.isEmpty(response.getResults())) {
                return INVALID_WORD;
            } else {
                List<Result> results = fillMissingPartOfSpeech(response.getResults());
                List<String> partsOfSpeech = getPartsOfSpeech(results);
                String definitions = getFormattedDefinitions(buildDefinitionsTemplates(partsOfSpeech, results));
                String synonyms = getFormattedSynonyms(results);
                String examples = getFormattedExamples(results);
                return definitions + synonyms + examples;
            }
        }).orElse(API_ERROR_MESSAGE);
    }

    private String getFormattedSynonyms(List<Result> results) {
        List<String> synonyms = results.stream()
                .map(Result::getSynonyms)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(synonyms)) {
            return Strings.EMPTY;
        } else {
            return getFormattedTemplate(buildWordApiTemplate(SYNONYMS_TITLE, synonyms)) + NEXT_LINE;
        }
    }

    private String getFormattedExamples(List<Result> results) {
        List<String> examples = results.stream()
                .map(Result::getExamples)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(examples)) {
            return Strings.EMPTY;
        } else {
            return getFormattedTemplate(buildWordApiTemplate(EXAMPLES_TITLE, examples)) + NEXT_LINE;
        }
    }

    public boolean isResponseValid(String text) {
        return !text.equals(INVALID_WORD) && !text.equals(API_ERROR_MESSAGE);
    }

    private List<String> getPartsOfSpeech(List<Result> definitions) {
        Set<String> partsOfSpeechOfCurrentWord = definitions.stream()
                .map(Result::getPartOfSpeech)
                .collect(Collectors.toSet());

        List<String> filteredPartsOfSpeech = PartOfSpeech.getTitles();
        filteredPartsOfSpeech.retainAll(partsOfSpeechOfCurrentWord);
        return filteredPartsOfSpeech;
    }

    private List<WordApiTemplate> buildDefinitionsTemplates(List<String> partsOfSpeech, List<Result> results) {
        List<WordApiTemplate> templates = new ArrayList<>();
        for (String partOfSpeech : partsOfSpeech) {
            WordApiTemplate template = new WordApiTemplate();
            template.setTitle(partOfSpeech);
            List<String> points = results.stream()
                    .filter(result -> result.getPartOfSpeech().equals(partOfSpeech))
                    .map(Result::getDefinition)
                    .collect(Collectors.toList());
            template.addPoints(points);
            templates.add(template);
        }
        return templates;
    }

    private WordApiTemplate buildWordApiTemplate(String title, List<String> points) {
        return new WordApiTemplate()
                .setTitle(title)
                .addPoints(points);
    }

    private String getFormattedDefinitions(List<WordApiTemplate> templates) {
        String joinedDefinitions = templates.stream()
                .map(this::getFormattedTemplate)
                .collect(Collectors.joining(NEXT_LINE));
        return joinedDefinitions + NEXT_LINE;
    }

    private String getFormattedTemplate(WordApiTemplate template) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String point : template.getPoints()) {
            stringBuilder.append("- ");
            stringBuilder.append(point);
            stringBuilder.append(NEXT_LINE);
        }
        return "<b>" + StringUtils.capitalize(template.getTitle()) + "</b>" + NEXT_LINE + stringBuilder.toString();
    }

    private List<Result> fillMissingPartOfSpeech(List<Result> results) {
        return results.stream()
                .map(this::setOtherPartOfSpeechIfRequired)
                .collect(Collectors.toList());
    }

    private Result setOtherPartOfSpeechIfRequired(Result result) {
        if (StringUtils.isEmpty(result.getPartOfSpeech())) {
            result.setPartOfSpeech(OTHER.getTitle());
        }
        return result;
    }

}
