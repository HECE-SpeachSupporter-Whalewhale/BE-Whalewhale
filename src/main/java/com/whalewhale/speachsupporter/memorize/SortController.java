package com.whalewhale.speachsupporter.memorize;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SortController {

    private final PresentationSortRepository presentationSortRepository;

    @GetMapping("/sort/search")
    public String searchPresentations(@RequestParam(required = false) String title,
                                      @RequestParam(required = false, defaultValue = "latest") String sort,
                                      Model model) {
        List<Presentation> presentations;

        if (title == null) title = "";

        switch (sort) {
            case "name":
                presentations = presentationSortRepository.findByTitleContainingOrderByTitleAsc(title);
                break;
            case "bookmarks":
                presentations = presentationSortRepository.findByTitleContainingOrderByIsBookmarkedDescCreatedAtDesc(title);
                break;
            case "latest":
            default:
                presentations = presentationSortRepository.findByTitleContainingOrderByCreatedAtDesc(title);
                break;
        }

        model.addAttribute("presentations", presentations);
        model.addAttribute("searchTitle", title);
        model.addAttribute("currentSort", sort);
        return "search";
    }
}