package syrbu.english_words_definition_bot.service.bot;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BotUpdatesQueue {

    private static final int CAPACITY = 100000;
    private static final BlockingQueue<Update> UPDATE_QUEUE = new ArrayBlockingQueue<>(CAPACITY);

    public static void add(Update update) {
        UPDATE_QUEUE.add(update);
    }

    public static Update poll() {
        return UPDATE_QUEUE.poll();
    }

    public static int getSize() {
        return UPDATE_QUEUE.size();
    }

    public static int getCapacity() {
        return CAPACITY;
    }

}
