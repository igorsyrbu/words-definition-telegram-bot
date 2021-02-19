package syrbu.english_words_definition_bot.model.telegram;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Setter
@EqualsAndHashCode(callSuper = false)
public class SendMessageWrapper extends SendMessage {
    private boolean isError = false;

    public boolean isError() {
        return isError;
    }

}
