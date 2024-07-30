package com.whalewhale.speachsupporter.memorize;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final SearchRepository presentationRepository;

    // ... 기존 메소드들 ...

    @GetMapping("/search")
    public String search(@RequestParam(name = "title", required = false) String title, Model model) {
        List<Presentation> results;
        if (title != null && !title.isEmpty()) {
            results = presentationRepository.findByTitleContainingIgnoreCase(title);
        } else {
            results = presentationRepository.findAll();
        }
        model.addAttribute("presentations", results);
        model.addAttribute("searchTitle", title);
        return "search";
    }
}