package syrbu.english_words_definition_bot.model;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import syrbu.english_words_definition_bot.service.WordsApi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        String greetingMessage = "Hey there, %s, let's expand your English knowledge!\n" +
                "Send me a word and I'll explain it \uD83D\uDE0A";
        Long chatId = update.getMessage().getChatId();
        String command = update.getMessage().getText();
        SendMessage message = new SendMessage()
                .enableMarkdown(true)
                .setChatId(chatId);
        if (command.equals("/start")) {
            message.setText(String.format(greetingMessage, update.getMessage().getFrom().getFirstName()));
        } else {
            String userMessage = update.getMessage().getText();
            List<String> results = WordsApi.getDefinitions(userMessage);
            StringBuilder stringBuilder = new StringBuilder();
            for (String result : results) {
                stringBuilder.append(result);
                stringBuilder.append("\n");
            }
            message.setText(stringBuilder.toString());
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        if (chatId != 140942276L) {
            sendMeUserMessageNotification(update, chatId);
        }
    }

    private void sendMeUserMessageNotification(Update update, long messageChatId) {
        SendMessage message = new SendMessage();
        String firstName = update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();
        String username = update.getMessage().getFrom().getUserName();
        Integer datetime = update.getMessage().getDate();
        String text = update.getMessage().getText();
        message.setText("First name: " + firstName + "\n" +
                "Last name: " + lastName + "\n" +
                "Username: " + username + "\n" +
                "Chat Id: " + messageChatId + "\n" +
                "Datetime: " + getDateFromJsonDate(datetime) + "\n" +
                "Text: " + text);
        message.setChatId(140942276L);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "{telegram.bot_username}";
    }

    @Override
    public String getBotToken() {
        return "{telegram.bot_token}";
    }

    private String getDateFromJsonDate(Integer unixTime) {
        Date date = new Date(unixTime * 1000L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy z");
        return dateFormat.format(date);
    }
}