package syrbu.english_words_definition_bot.service.bot;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import syrbu.english_words_definition_bot.model.telegram.UserState;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static syrbu.english_words_definition_bot.service.bot.BotCommand.DEFINITION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InMemoryUserState {

    private static final Map<String, BotCommand> USER_STATE = new ConcurrentHashMap<>();

    public static void updateState(String chatId, BotCommand command) {
        USER_STATE.put(chatId, command);
    }

    public static BotCommand getState(String chatId) {
        BotCommand botCommand = USER_STATE.get(chatId);
        if (Objects.nonNull(botCommand)) {
            return botCommand;
        } else {
            updateState(chatId, DEFINITION);
            return DEFINITION;
        }
    }

    public static List<UserState> getStateMap() {
        Set<Map.Entry<String, BotCommand>> entries = USER_STATE.entrySet();
        return entries.stream()
                .map(InMemoryUserState::buildUserState)
                .collect(Collectors.toList());
    }

    private static UserState buildUserState(Map.Entry<String, BotCommand> entry) {
        return new UserState()
                .setChatId(entry.getKey())
                .setState(entry.getValue().name());
    }

}
