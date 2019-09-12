package syrbu.english_words_definition_bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        String command = update.getMessage().getText();
        SendMessage message = new SendMessage();
        if (command.equals("/start")) {
            message.setText("Hey there, " + update.getMessage().getFrom().getFirstName() + ", " +
                    "let's expand your English knowledge!" + "" +
                    "\nSend me a word and I'll explain it \uD83D\uDE0A");
        } else {
            String userMessage = update.getMessage().getText();
            List<String> results = CambridgeDictionaryHtmlParser.getDefinitions(userMessage);
            if (results.size() == 1) {
                message.setText(results.get(0));
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                for (String result : results) {
                    stringBuilder.append(result);
                    stringBuilder.append("\n");
                }
                message.setText(stringBuilder.toString());
            }
        }
        Long messageChatId = update.getMessage().getChatId();
        try {
            message.setChatId(messageChatId);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        if (messageChatId != 140942276L) {
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
    }

    @Override
    public String getBotUsername() {
        return "english_words_definition_bot";
    }

    @Override
    public String getBotToken() {
        return "689499600:AAFTYW1MwqUm1C0A-01JHRoLJyauBdvUfwo";
    }

    private String getDateFromJsonDate(Integer unixTime) {
        Date date = new Date(unixTime * 1000L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy z");
        return dateFormat.format(date);
    }
}