package syrbu.english_words_definition_bot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
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

    public static List<SendMessageWrapper> buildSendMessageWithTextSplit(String text, String recipient) {
        int maxMessageLength = 4096;
        return splitTextByNewLineAndSize(text, maxMessageLength).stream()
                .map(messageChunk -> buildSendMessage(recipient, messageChunk))
                .collect(Collectors.toList());
    }

    public static SendMessageWrapper buildSendMessage(String recipient, String text) {
        SendMessageWrapper sendMessage = new SendMessageWrapper();
        sendMessage.setChatId(recipient);
        sendMessage.setText(text);
        sendMessage.enableMarkdownV2(true);
        sendMessage.setParseMode("html");
        return sendMessage;
    }

    public static SendVoiceWrapper buildSendVoice(String chatId, InputStream inputStream, String fileName) {
        SendVoiceWrapper sendAudio = new SendVoiceWrapper();
        sendAudio.setChatId(chatId);
        sendAudio.setVoice(new InputFile(inputStream, fileName.replace("'", "")));
        return sendAudio;
    }

    public static Optional<SendMessageWrapper> buildNotificationMessage(String recipient, Update update) {
        String chatId = getChatId(update);
        if (!chatId.equals(recipient)) {
            String text = getMessage(update);
            String messageText = "First name: " + getFirstName(update) + "\n" +
                    "Last name: " + getLastName(update) + "\n" +
                    "Username: " + getUsername(update) + "\n" +
                    "Chat id: " + getChatId(update) + "\n" +
                    "Language code: " + getLanguageCode(update) + "\n" +
                    "Is bot: " + isBot(update) + "\n" +
                    "Datetime: " + getFormattedUTCDate(getMessageDate(update)) + "\n" +
                    "Text: " + text;
            return Optional.of(buildSendMessage(recipient, messageText));
        } else {
            return Optional.empty();
        }
    }

    public static SendMessageWrapper buildErrorNotificationMessage(String recipient, String messageJson, TelegramApiException e) {
        String text = "Error notification\n" +
                "LocalDateTime: " + DateUtil.getFormattedUTCDate(LocalDateTime.now()) + "\n" +
                "Exception message: " + e.getLocalizedMessage() + "\n" +
                "SendMessage: " + messageJson + "\n";
        SendMessageWrapper sendMessage = buildSendMessage(recipient, text);
        sendMessage.setError(true);
        return sendMessage;
    }

    private static List<String> splitTextByNewLineAndSize(String text, int size) {
        List<String> result = new ArrayList<>();
        String[] split = text.split("\n");
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
                stringBuilder.append("\n");
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
