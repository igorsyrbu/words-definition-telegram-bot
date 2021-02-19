package syrbu.english_words_definition_bot.constant.bot;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BotMessage {

    public static final String GREETING_MESSAGE = "Hey there%s!";
    public static final String GREETING_MESSAGE_2 = "Let's expand your English knowledge.\nSend me a word and I'll explain it ☺️";
    public static final String UNKNOWN_COMMAND = "I'm afraid, I'm not familiar with this command yet \uD83D\uDE44";
    public static final String INVALID_WORD = "Sorry, but I can't recognize the word \uD83D\uDE14";
    public static final String API_ERROR_MESSAGE = "Whoops, there is something wrong...try a bit later";
}
