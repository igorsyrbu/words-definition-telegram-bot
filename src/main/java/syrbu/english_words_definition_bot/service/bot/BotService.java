package syrbu.english_words_definition_bot.service.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import syrbu.english_words_definition_bot.constant.bot.NotificationType;
import syrbu.english_words_definition_bot.property.TelegramProperties;
import syrbu.english_words_definition_bot.service.speech.TextToSpeechService;
import syrbu.english_words_definition_bot.service.wordsapi.WordsService;

import java.util.List;
import java.util.Objects;

import static syrbu.english_words_definition_bot.constant.bot.BotMessage.GO_BACK_MESSAGE;
import static syrbu.english_words_definition_bot.constant.bot.BotMessage.SUGGESTION_RECEIVED;
import static syrbu.english_words_definition_bot.util.BotUtil.*;

@Log4j2
@Service
@EnableAsync
@RequiredArgsConstructor
public class BotService {

    private final TelegramBot telegramBot;
    private final TelegramProperties telegramProperties;
    private final WordsService wordsService;
    private final TextToSpeechService textToSpeechService;

    @Async
    @Scheduled(fixedRate = 100)
    public void handleUpdate() {
        Update update = BotUpdatesQueue.poll();
        if (Objects.nonNull(update)) {
            process(update);
        }
    }

    private void process(Update update) {
        if (isCommand(update)) {
            processCommand(update);
        } else {
            processText(update);
        }
    }

    private void processCommand(Update update) {
        String chatId = getChatId(update);
        String command = getMessage(update);
        List<String> messages = BotCommand.getCommand(command).process(update);
        messages.forEach(message -> telegramBot.sendMessage(chatId, message));
        buildNotification(telegramProperties.getAdminChatId(), update, NotificationType.COMMAND).ifPresent(telegramBot::sendMessage);
    }

    private void processText(Update update) {
        String chatId = getChatId(update);
        String adminChatId = telegramProperties.getAdminChatId();
        BotCommand command = InMemoryUserState.getState(chatId);
        if (command == BotCommand.SUGGESTION) {
            telegramBot.sendMessage(chatId, SUGGESTION_RECEIVED);
            telegramBot.sendMessage(chatId, GO_BACK_MESSAGE);
            buildNotification(adminChatId, update, NotificationType.SUGGESTION).ifPresent(telegramBot::sendMessage);
        } else {
            String text = wordsService.getFormattedMessage(getMessage(update));
            String textWithPraiseToUkraine = getTextWithPraiseToUkraine(text);

            telegramBot.sendMessage(chatId, textWithPraiseToUkraine);
            if (wordsService.isResponseValid(text)) {
                String fileName = getMessage(update).toLowerCase();
                textToSpeechService.synthesize(fileName).ifPresent(inputStream -> telegramBot.sendVoice(chatId, inputStream, fileName));
            }
            InMemoryUserState.updateState(chatId, BotCommand.DEFINITION);
            buildNotification(adminChatId, update, NotificationType.DEFINITION).ifPresent(telegramBot::sendMessage);
        }
    }

    private String getTextWithPraiseToUkraine(String text) {
        String space = wordsService.isResponseValid(text) ? "" : "\n\n";
        return text + space +
                "Слава Україні! \uD83C\uDDFA\uD83C\uDDE6";
    }
}
