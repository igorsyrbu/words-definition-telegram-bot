package syrbu.english_words_definition_bot.constant.bot;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BotMessage {

    public static final String GREETING_MESSAGE = "Hey %s!";
    public static final String GREETING_MESSAGE_ALT = "Hi There!";
    public static final String GREETING_MESSAGE_2 = "I can help you sense every word you have mixed feelings about! \uD83E\uDD13";
    public static final String UNKNOWN_COMMAND = "I'm afraid, I'm not familiar with this command yet \uD83D\uDE44";
    public static final String INVALID_WORD = "Sorry, but I can't recognize the word \uD83D\uDE14";
    public static final String API_ERROR_MESSAGE = "Whoops, something went wrong...please, try a bit later \uD83D\uDE48";
    public static final String DEFINITION_COMMAND = "Try me! Type the word you're interested in";
    public static final String SUGGESTION_COMMAND = "How could I get better?";
    public static final String SUGGESTION_RECEIVED = "Wow! I'll definitely consider it. Thank you! \uD83D\uDE0A";
    public static final String GO_BACK_MESSAGE = "To go back, please, tap /definition";
}
