package syrbu.english_words_definition_bot.constant.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

    DEFINITION("DEFINITION NOTIFICATION"),
    SUGGESTION("SUGGESTION NOTIFICATION"),
    COMMAND("COMMAND NOTIFICATION");

    private final String title;

}
