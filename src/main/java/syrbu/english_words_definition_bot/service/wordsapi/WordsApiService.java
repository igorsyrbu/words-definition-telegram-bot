package syrbu.english_words_definition_bot.service.wordsapi;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import syrbu.english_words_definition_bot.model.wordapi.WordApiResponse;
import syrbu.english_words_definition_bot.property.RapidApiProperties;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class WordsApiService {

    private final RestTemplate restTemplate;
    private final RapidApiProperties rapidApiProperties;

    public Optional<WordApiResponse> executeRequest(String url, String word) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("x-rapidapi-key", rapidApiProperties.getApplicationKey());
            headers.add("x-rapidapi-host", rapidApiProperties.getHost());
            ResponseEntity<WordApiResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<String>(headers), WordApiResponse.class, word);
            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.of(WordApiResponse.empty());
            } else {
                log.error(e.getLocalizedMessage(), e);
                return Optional.empty();
            }
        }
    }

}
