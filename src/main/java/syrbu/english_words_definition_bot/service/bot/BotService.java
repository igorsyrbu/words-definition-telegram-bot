package syrbu.english_words_definition_bot.service.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import syrbu.english_words_definition_bot.aws.service.AmazonPollyService;
import syrbu.english_words_definition_bot.service.wordsapi.WordsService;

import java.util.Objects;

import static syrbu.english_words_definition_bot.constant.bot.BotCommand.COMMAND_START;
import static syrbu.english_words_definition_bot.constant.bot.BotMessage.*;
import static syrbu.english_words_definition_bot.util.BotUtil.*;

@Log4j2
@Service
@EnableAsync
@RequiredArgsConstructor
public class BotService {

    private final TelegramBot telegramBot;
    private final WordsService wordsService;
    private final AmazonPollyService amazonPollyService;

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
        if (getMessage(update).equalsIgnoreCase(COMMAND_START)) {
            String firstName = getFirstName(update);
            String formatArg = StringUtils.isNotEmpty(firstName) ? ", " + firstName : Strings.EMPTY;
            String text = String.format(GREETING_MESSAGE, formatArg);
            telegramBot.sendMessage(chatId, text);
            telegramBot.sendMessage(chatId, GREETING_MESSAGE_2);
        } else {
            telegramBot.sendMessage(chatId, UNKNOWN_COMMAND);
        }
    }

    private void processText(Update update) {
        String chatId = getChatId(update);
        String text = wordsService.getFormattedMessage(getMessage(update));
        telegramBot.sendMessage(chatId, text);
        if (wordsService.isResponseValid(text)) {
            String fileName = getMessage(update).toLowerCase();
            amazonPollyService.synthesize(fileName).ifPresent(inputStream ->
                    telegramBot.sendVoice(chatId, inputStream, fileName));
        }
    }
}
