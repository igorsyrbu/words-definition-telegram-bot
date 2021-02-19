package syrbu.english_words_definition_bot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getFormattedUTCDate(Integer unixTime) {
        Instant instant = Instant.ofEpochSecond(unixTime);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return FORMATTER.format(convertToUtc(localDateTime));
    }

    public static String getFormattedUTCDate(LocalDateTime localDateTime) {
        return FORMATTER.format(convertToUtc(localDateTime));
    }

    public static LocalDateTime convertToUtc(LocalDateTime time) {
        return time
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
    }
}
