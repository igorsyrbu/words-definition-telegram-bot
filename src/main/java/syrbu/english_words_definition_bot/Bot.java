package syrbu.english_words_definition_bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
        message.setChatId(update.getMessage().getChatId());
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
}