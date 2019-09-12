package syrbu.english_words_definition_bot;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
works not perfectly, because of Jsoup parsing restrictions
use WordsApi version based on JSON responses {@link syrbu.english_words_definition_bot.WordsApi}
 */

public class CambridgeDictionaryHtmlParser {
    private final static String noResultsString = "The most popular dictionary and thesaurus Meanings & definitions of words in English with examples, synonyms, pronunciations and translations";
    private final static String cambridgeDictionaryEntryUrl = "https://dictionary.cambridge.org/dictionary/english/";

    public static List<String> getDefinitions(String word) {
        List<String> resultList = new ArrayList<>();
        String html = null;
        try {
            html = IOUtils.toString(new URL(cambridgeDictionaryEntryUrl + word), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc = Jsoup.parse(html);
        Elements metaTags = doc.getElementsByTag("meta");

        for (Element metaTag : metaTags) {
            String content = metaTag.attr("content");
            String name = metaTag.attr("name");
            if (name.equals("description")) {
                String[] array = content.split("[0-9].");
                for (String s : array) {
                    if (s.contains(" definition:")) {
                        s = s.substring(s.lastIndexOf("definition:"));
                        s = s.replace("definition:", "");
                    }
                    if (s.contains("Learn more."))
                        s = s.replace("Learn more.", "");
                    if (s.trim().length() > 1) {
                        s = s.replace(":", "");
                        s = s.replace(".", "");
                        if (s.equals(noResultsString)) {
                            resultList.add("Sorry, but I can't recognize the word");
                            break;
                        } else {
                            resultList.add("- " + s.trim());
                        }
                    }
                }
                break;
            }
        }
        return resultList;
    }

}
