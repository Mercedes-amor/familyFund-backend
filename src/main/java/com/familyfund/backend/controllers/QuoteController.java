package com.familyfund.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.familyfund.backend.services.QuoteService;

@RestController
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("/api/quotes/saving")
    public List<String> getSavingQuotes() {
        return quoteService.getSavingQuotes();
    }
}
