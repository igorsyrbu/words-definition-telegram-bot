package syrbu.english_words_definition_bot.model.wordapi;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Accessors(chain = true)
public class WordApiTemplate {
    private String title;
    private List<String> points;

    public WordApiTemplate addPoints(List<String> points) {
        points.forEach(this::addPoint);
        return this;
    }

    public void addPoint(String point) {
        if (Objects.isNull(points)) {
            points = new ArrayList<>();
        }
        points.add(point);
    }
}
