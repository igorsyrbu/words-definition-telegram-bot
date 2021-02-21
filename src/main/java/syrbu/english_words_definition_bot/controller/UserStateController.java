package syrbu.english_words_definition_bot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import syrbu.english_words_definition_bot.model.telegram.UserState;
import syrbu.english_words_definition_bot.service.bot.InMemoryUserState;

import java.util.List;

@RestController
public class UserStateController {

    @GetMapping("/states")
    public List<UserState> getUserStates() {
        return InMemoryUserState.getStateMap();
    }
}
