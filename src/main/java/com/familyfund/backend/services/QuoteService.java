package com.familyfund.backend.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuoteService {

    private static final String URL = "https://www.brainyquote.com/topics/saving-money-quotes";

    public List<String> getSavingQuotes() {
        List<String> quotes = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(URL).get();

            // Selector CSS de las frases
            Elements elements = doc.select(".b-qt"); // cada frase tendrá esta clase

            elements.forEach(el -> quotes.add(el.text()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return quotes;
    }

}
