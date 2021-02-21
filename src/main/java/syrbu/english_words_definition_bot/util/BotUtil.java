package syrbu.english_words_definition_bot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import syrbu.english_words_definition_bot.constant.bot.NotificationType;
import syrbu.english_words_definition_bot.model.telegram.SendMessageWrapper;
import syrbu.english_words_definition_bot.model.telegram.SendVoiceWrapper;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static syrbu.english_words_definition_bot.util.DateUtil.getFormattedUTCDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BotUtil {

    private static final String NEW_LINE = "\n";
    private static final String MESSAGE_PARSE_MODE = "html";
    private static final Integer MESSAGE_MAX_LENGTH = 4096;

    public static List<SendMessageWrapper> buildSendMessageWithTextSplit(String text, String recipient) {
        return splitTextByNewLineAndSize(text, MESSAGE_MAX_LENGTH).stream()
                .map(messageChunk -> buildSendMessage(recipient, messageChunk))
                .collect(Collectors.toList());
    }

    public static SendMessageWrapper buildSendMessage(String recipient, String text) {
        SendMessageWrapper sendMessage = new SendMessageWrapper();
        sendMessage.setChatId(recipient);
        sendMessage.setText(text);
        sendMessage.enableMarkdownV2(true);
        sendMessage.setParseMode(MESSAGE_PARSE_MODE);
        return sendMessage;
    }

    public static SendVoiceWrapper buildSendVoice(String chatId, InputStream inputStream, String fileName) {
        SendVoiceWrapper sendAudio = new SendVoiceWrapper();
        sendAudio.setChatId(chatId);
        sendAudio.setVoice(new InputFile(inputStream, fileName.replace("'", "")));
        return sendAudio;
    }

    public static Optional<SendMessageWrapper> buildNotification(String recipient, Update update, NotificationType type) {
        String chatId = getChatId(update);
        if (!chatId.equals(recipient)) {
            String messageText = "<b>" + type.getTitle() + "</b>" + NEW_LINE + NEW_LINE + buildNotificationText(update);
            return Optional.of(buildSendMessage(recipient, messageText));
        } else {
            return Optional.empty();
        }
    }

    public static SendMessageWrapper buildErrorNotificationMessage(String recipient, String messageJson, TelegramApiException e) {
        String text = "<b>" + NotificationType.ERROR.getTitle() + "</b>" + NEW_LINE + NEW_LINE + buildErrorNotificationText(messageJson, e);
        SendMessageWrapper sendMessage = buildSendMessage(recipient, text);
        sendMessage.setError(true);
        return sendMessage;
    }

    private static String buildNotificationText(Update update) {
        String text = getMessage(update);
        return "First name: " + getFirstName(update) + NEW_LINE +
                "Last name: " + getLastName(update) + NEW_LINE +
                "Username: " + getUsername(update) + NEW_LINE +
                "Chat id: " + getChatId(update) + NEW_LINE +
                "Language code: " + getLanguageCode(update) + NEW_LINE +
                "Is bot: " + isBot(update) + NEW_LINE +
                "Datetime: " + getFormattedUTCDate(getMessageDate(update)) + NEW_LINE +
                "Text: " + text;
    }

    private static String buildErrorNotificationText(String messageJson, TelegramApiException e) {
        if (e instanceof TelegramApiRequestException) {
            return buildApiRequestErrorNotification(messageJson, (TelegramApiRequestException) e);
        } else {
            return buildSimpleErrorNotificationText(messageJson, e);
        }
    }

    private static String buildSimpleErrorNotificationText(String messageJson, TelegramApiException e) {
        return "LocalDateTime: " + DateUtil.getFormattedUTCDate(LocalDateTime.now()) + NEW_LINE +
                "Exception message: " + e.getLocalizedMessage() + NEW_LINE +
                "SendMessage: " + messageJson + NEW_LINE;
    }

    private static String buildApiRequestErrorNotification(String messageJson, TelegramApiRequestException e) {
        return "LocalDateTime: " + DateUtil.getFormattedUTCDate(LocalDateTime.now()) + NEW_LINE +
                "Status code: " + e.getErrorCode() + NEW_LINE +
                "Api response: " + e.getApiResponse() + NEW_LINE +
                "Exception message: " + e.getLocalizedMessage() + NEW_LINE +
                "SendMessage: " + messageJson + NEW_LINE;
    }

    private static List<String> splitTextByNewLineAndSize(String text, int size) {
        List<String> result = new ArrayList<>();
        String[] split = text.split(NEW_LINE);
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : split) {
            if (str.length() > size) {
                result.addAll(splitTextBySize(str, size));
            } else {
                int nextStringBuilderLength = stringBuilder.toString().length() + str.length();
                if (nextStringBuilderLength > size) {
                    result.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                }
                stringBuilder.append(str);
                stringBuilder.append(NEW_LINE);
            }
        }
        result.add(stringBuilder.toString());
        return result;
    }

    private static List<String> splitTextBySize(String text, int size) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < text.length(); i += size) {
            result.add(text.substring(i, Math.min(text.length(), i + size)));
        }
        return result;
    }

    public static boolean isTextMessage(Update update) {
        return Objects.nonNull(update) &&
                Objects.nonNull(update.getMessage()) &&
                Objects.nonNull(update.getMessage().getText());
    }

    public static boolean isPrivateChat(Update update) {
        return update.getMessage().isUserMessage();
    }

    public static boolean isGroupChat(Update update) {
        return update.getMessage().isGroupMessage() ||
                update.getMessage().isSuperGroupMessage() ||
                update.getMessage().isChannelMessage();
    }

    public static boolean isBot(Update update) {
        return getUser(update).getIsBot();
    }

    public static String getLanguageCode(Update update) {
        return getUser(update).getLanguageCode();
    }

    public static String getFirstName(Update update) {
        return getUser(update).getFirstName();
    }

    public static String getLastName(Update update) {
        return getUser(update).getLastName();
    }

    public static String getUsername(Update update) {
        return getUser(update).getUserName();
    }

    private static User getUser(Update update) {
        return update.getMessage().getFrom();
    }

    public static boolean isCommand(Update update) {
        return update.getMessage().isCommand();
    }

    public static Integer getMessageDate(Update update) {
        return update.getMessage().getDate();
    }

    public static String getMessage(Update update) {
        return update.getMessage().getText();
    }

    public static String getChatId(Update update) {
        return String.valueOf(update.getMessage().getChatId());
    }

}
