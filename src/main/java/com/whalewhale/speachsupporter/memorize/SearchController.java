package com.whalewhale.speachsupporter.memorize;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import com.whalewhale.speachsupporter.memorize.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final SearchRepository searchRepository;

    @GetMapping("/search")
    public String search(@RequestParam(name = "title", required = false) String title,
                         @RequestParam(name = "sort", defaultValue = "latest") String sort,
                         Model model) {
        List<Presentation> results;

        if (title == null || title.isEmpty()) {
            switch (sort) {
                case "oldest":
                    results = searchRepository.findAllByOrderByCreatedAtAsc();
                    break;
                case "bookmarked":
                    results = searchRepository.findByIsBookmarkedTrueOrderByCreatedAtDesc();
                    break;
                case "latest":
                default:
                    results = searchRepository.findAllByOrderByCreatedAtDesc();
                    break;
            }
        } else {
            switch (sort) {
                case "oldest":
                    results = searchRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtAsc(title);
                    break;
                case "bookmarked":
                    results = searchRepository.findByTitleContainingIgnoreCaseAndIsBookmarkedTrueOrderByCreatedAtDesc(title);
                    break;
                case "latest":
                default:
                    results = searchRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(title);
                    break;
            }
        }

        model.addAttribute("presentations", results);
        model.addAttribute("searchTitle", title);
        model.addAttribute("currentSort", sort);
        return "search";
    }

    @PostMapping("/deletePresentation/{id}")
    public String deletePresentation(@PathVariable("id") Integer id) {
        searchRepository.deleteById(id);
        return "redirect:/search";
    }


}