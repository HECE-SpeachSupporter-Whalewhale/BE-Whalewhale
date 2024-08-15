package com.whalewhale.speachsupporter.memorize;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import com.whalewhale.speachsupporter.Users.Users;
import com.whalewhale.speachsupporter.Users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final SearchRepository searchRepository;
    private final UsersRepository usersRepository;

    @GetMapping("/search")
    public String search(@RequestParam(name = "title", required = false) String title,
                         @RequestParam(name = "sort", defaultValue = "latest") String sort,
                         Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = usersRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Presentation> results;

        if (title == null || title.isEmpty()) {
            switch (sort) {
                case "oldest":
                    results = searchRepository.findAllByUserOrderByCreatedAtAsc(currentUser);
                    break;
                case "bookmarked":
                    results = searchRepository.findByUserAndIsBookmarkedTrueOrderByCreatedAtDesc(currentUser);
                    break;
                case "latest":
                default:
                    results = searchRepository.findAllByUserOrderByCreatedAtDesc(currentUser);
                    break;
            }
        } else {
            switch (sort) {
                case "oldest":
                    results = searchRepository.findByUserAndTitleContainingIgnoreCaseOrderByCreatedAtAsc(currentUser, title);
                    break;
                case "bookmarked":
                    results = searchRepository.findByUserAndTitleContainingIgnoreCaseAndIsBookmarkedTrueOrderByCreatedAtDesc(currentUser, title);
                    break;
                case "latest":
                default:
                    results = searchRepository.findByUserAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(currentUser, title);
                    break;
            }
        }

        model.addAttribute("presentations", results);
        model.addAttribute("searchTitle", title);
        model.addAttribute("currentSort", sort);
        return "search";
    }
}