package syrbu.english_words_definition_bot.model.telegram;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserState {
    private String chatId;
    private String state;
}