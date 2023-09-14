package com.zingiberfeel.nub.controller;


import com.zingiberfeel.nub.model.Snippet;
import com.zingiberfeel.nub.repository.SnippetRepository;
import com.zingiberfeel.nub.service.SnippetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;


@Controller
@RequestMapping("/snippet")
public class SnippetController {
    private SnippetService snippetService;

    public SnippetController(SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    @GetMapping("/{hash}")
    public String findByHash(@PathVariable String hash, Model model) {
        Snippet snippet = snippetService.getSnippetByHash(hash);
        if (snippet != null) {
            model.addAttribute("snippet", snippet);
            LocalDateTime currentTime = LocalDateTime.now();
            Duration duration = Duration.between(currentTime, snippet.getLifetime());

            // Calculate days, hours, and minutes
            long days = duration.toDays();
            long hours = duration.minusDays(days).toHours();
            long minutes = duration.minusDays(days).minusHours(hours).toMinutes();

            // Format the time left as Days-Hours-Minutes
            String timeLeft = String.format("%d Days %02d Hours %02d Minutes", days, hours, minutes);


            model.addAttribute("timeLeft", timeLeft);

            return "snippet";
        } else {
            return "snippetNotFound"; // You can create a corresponding HTML template for this case
        }
    }
}
