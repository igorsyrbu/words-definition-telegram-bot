package syrbu.english_words_definition_bot.service.bot;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface CommandProcessor {

    List<String> process(Update update);
}
