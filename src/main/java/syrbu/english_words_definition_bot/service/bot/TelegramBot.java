package syrbu.english_words_definition_bot.service.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import syrbu.english_words_definition_bot.model.telegram.SendMessageWrapper;
import syrbu.english_words_definition_bot.model.telegram.SendVoiceWrapper;
import syrbu.english_words_definition_bot.property.TelegramProperties;

import java.io.InputStream;

import static syrbu.english_words_definition_bot.util.BotUtil.*;

@Log4j2
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final ObjectMapper objectMapper;
    private final TelegramProperties telegramProperties;

    @Override
    public void onUpdateReceived(Update update) {
        if (isTextMessage(update) && isPrivateChat(update)) {
            BotUpdatesQueue.add(update);
            log.info("Added to queue. Update: {}", getAsJsonString(update));
            log.info("Queue size: {}/{}", BotUpdatesQueue.getSize(), BotUpdatesQueue.getCapacity());
        } else if (isTextMessage(update) && isGroupChat(update)) {
            log.warn("Non-private chat update: {}", getAsJsonString(update));
        } else {
            log.warn("Non-text update: {}", getAsJsonString(update));
        }
    }

    @Override
    public String getBotUsername() {
        return telegramProperties.getUsername();
    }

    @Override
    public String getBotToken() {
        return telegramProperties.getToken();
    }

    public void sendMessage(String recipient, String text) {
        buildSendMessageWithTextSplit(text, recipient).forEach(this::sendMessage);
    }

    public void sendVoice(String recipient, InputStream inputStream, String fileName) {
        sendVoice(buildSendVoice(recipient, inputStream, fileName));
    }

    public void sendMessage(SendMessageWrapper method) {
        try {
            execute(method);
        } catch (TelegramApiException e) {
            log.error(e.getLocalizedMessage(), e);
            sendErrorNotification(method, e);
        }
    }

    public void sendVoice(SendVoiceWrapper method) {
        try {
            execute(method);
        } catch (TelegramApiException e) {
            log.error(e.getLocalizedMessage(), e);
            sendErrorNotification(method, e);
        }
    }

    public void sendErrorNotification(Object object, TelegramApiException exception) {
        try {
            execute(buildErrorNotificationMessage(telegramProperties.getAdminChatId(), getAsJsonString(object), exception));
        } catch (TelegramApiException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    @SneakyThrows(JsonProcessingException.class)
    private String getAsJsonString(Object object) {
        return objectMapper.writeValueAsString(object);
    }

}
