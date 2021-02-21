package syrbu.english_words_definition_bot.service.bot;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import syrbu.english_words_definition_bot.util.BotUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static syrbu.english_words_definition_bot.constant.bot.BotMessage.*;
import static syrbu.english_words_definition_bot.util.BotUtil.getFirstName;

@Getter
public enum BotCommand implements CommandProcessor {

    START("/start") {
        @Override
        public List<String> process(Update update) {
            updateState(update, DEFINITION);
            String firstName = getFirstName(update);
            String greetingMessage = StringUtils.isNotEmpty(firstName) ?
                    String.format(GREETING_MESSAGE, firstName) : GREETING_MESSAGE_ALT;
            return Arrays.asList(greetingMessage, GREETING_MESSAGE_2);
        }
    },

    DEFINITION("/definition") {
        @Override
        public List<String> process(Update update) {
            updateState(update, DEFINITION);
            return Collections.singletonList(DEFINITION_COMMAND);
        }
    },

    SUGGESTION("/suggestion") {
        @Override
        public List<String> process(Update update) {
            updateState(update, SUGGESTION);
            return Collections.singletonList(SUGGESTION_COMMAND);
        }
    },

    UNKNOWN("/unknown") {
        @Override
        public List<String> process(Update update) {
            return Collections.singletonList(UNKNOWN_COMMAND);
        }
    };

    private final String value;

    BotCommand(String value) {
        this.value = value;
    }

    public static BotCommand getCommand(String message) {
        return Arrays.stream(BotCommand.values())
                .filter(botCommand -> botCommand.getValue().equalsIgnoreCase(message))
                .findFirst()
                .orElse(UNKNOWN);
    }

    private static void updateState(Update update, BotCommand command) {
        String chatId = BotUtil.getChatId(update);
        InMemoryUserState.updateState(chatId, command);
    }

}
