package syrbu.english_words_definition_bot.service.wordsapi;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import syrbu.english_words_definition_bot.constant.wordsapi.PartOfSpeech;
import syrbu.english_words_definition_bot.model.wordapi.WordApiDefinition;
import syrbu.english_words_definition_bot.model.wordapi.WordApiTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static syrbu.english_words_definition_bot.constant.bot.BotMessage.API_ERROR_MESSAGE;
import static syrbu.english_words_definition_bot.constant.bot.BotMessage.INVALID_WORD;
import static syrbu.english_words_definition_bot.constant.wordsapi.PartOfSpeech.OTHER;
import static syrbu.english_words_definition_bot.constant.wordsapi.WordsApiConstant.*;

@Service
@RequiredArgsConstructor
public class WordsService {

    private static final String NEXT_LINE = "\n";
    private final WordsApiService wordsApiService;

    public String getFormattedMessage(String text) {
        String definition = getFormattedDefinitions(text);
        if (!isResponseValid(definition)) {
            return definition;
        } else {
            String synonyms = getFormattedSynonyms(text);
            String examples = getFormattedExamples(text);
            return definition + NEXT_LINE + synonyms + NEXT_LINE + examples;
        }
    }

    private String getFormattedDefinitions(String text) {
        return wordsApiService.executeRequest(DEFINITION_ENDPOINT, text).map(response -> {
            if (CollectionUtils.isEmpty(response.getResults())) {
                return INVALID_WORD;
            } else {
                List<WordApiDefinition> definitions = response.getResults();
                List<String> partsOfSpeech = getPartsOfSpeech(definitions);
                return getFormatted(getFormattedDefinitions(partsOfSpeech, definitions));
            }
        }).orElse(API_ERROR_MESSAGE);
    }

    private String getFormattedSynonyms(String wordForSearch) {
        return wordsApiService.executeRequest(SYNONYMS_ENDPOINT, wordForSearch).map(response -> {
            if (CollectionUtils.isEmpty(response.getSynonyms())) {
                return Strings.EMPTY;
            } else {
                return getFormatted(getFormatted("Synonyms", response.getSynonyms()));
            }
        }).orElse(Strings.EMPTY);
    }

    private String getFormattedExamples(String wordForSearch) {
        return wordsApiService.executeRequest(EXAMPLES_ENDPOINT, wordForSearch).map(response -> {
            if (CollectionUtils.isEmpty(response.getExamples())) {
                return Strings.EMPTY;
            } else {
                return getFormatted(getFormatted("Examples", response.getExamples()));
            }
        }).orElse(Strings.EMPTY);
    }

    public boolean isResponseValid(String text) {
        return !text.equals(INVALID_WORD) && !text.equals(API_ERROR_MESSAGE);
    }

    private List<String> getPartsOfSpeech(List<WordApiDefinition> definitions) {
        Set<String> partsOfSpeechOfCurrentWord = definitions.stream()
                .map(WordApiDefinition::getPartOfSpeech)
                .collect(Collectors.toSet());

        List<String> filteredPartsOfSpeech = PartOfSpeech.getTitles();
        filteredPartsOfSpeech.retainAll(partsOfSpeechOfCurrentWord);
        return filteredPartsOfSpeech;
    }

    private List<WordApiTemplate> getFormattedDefinitions(List<String> partsOfSpeech, List<WordApiDefinition> definitions) {
        List<WordApiTemplate> templates = new ArrayList<>();
        List<WordApiDefinition> fulfilledDefinitions = fillMissingPartOfSpeech(definitions);
        for (String partOfSpeech : partsOfSpeech) {
            WordApiTemplate template = new WordApiTemplate();
            template.setTitle(partOfSpeech);
            List<String> points = fulfilledDefinitions.stream()
                    .filter(wordApiDefinition -> wordApiDefinition.getPartOfSpeech().equals(partOfSpeech))
                    .map(WordApiDefinition::getDefinition)
                    .collect(Collectors.toList());
            template.addPoints(points);
            templates.add(template);
        }
        return templates;
    }

    private WordApiTemplate getFormatted(String title, List<String> points) {
        return new WordApiTemplate()
                .setTitle(title)
                .addPoints(points);
    }

    private String getFormatted(List<WordApiTemplate> templates) {
        return templates.stream()
                .map(this::getFormatted)
                .collect(Collectors.joining(NEXT_LINE));
    }

    private String getFormatted(WordApiTemplate template) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String point : template.getPoints()) {
            stringBuilder.append("- ");
            stringBuilder.append(point);
            stringBuilder.append(NEXT_LINE);
        }
        return "<b>" + StringUtils.capitalize(template.getTitle()) + "</b>" + NEXT_LINE + stringBuilder.toString();
    }

    private List<WordApiDefinition> fillMissingPartOfSpeech(List<WordApiDefinition> definitions) {
        return definitions.stream()
                .map(this::setOtherPartOfSpeech)
                .collect(Collectors.toList());
    }

    private WordApiDefinition setOtherPartOfSpeech(WordApiDefinition definition) {
        if (StringUtils.isEmpty(definition.getPartOfSpeech())) {
            definition.setPartOfSpeech(OTHER.getTitle());
        }
        return definition;
    }

}
