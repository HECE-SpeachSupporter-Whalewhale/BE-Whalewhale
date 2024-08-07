package com.whalewhale.speachsupporter.memorize;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import com.whalewhale.speachsupporter.Presentation.PresentationRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final PresentationRepository presentationRepository;

    @PostMapping("/toggle/{id}")
    public String toggleBookmark(@PathVariable Integer id, HttpServletRequest request) {
        Presentation presentation = presentationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid presentation Id:" + id));
        presentation.setIsBookmarked(!presentation.getIsBookmarked());
        presentationRepository.save(presentation);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/search");
    }
}