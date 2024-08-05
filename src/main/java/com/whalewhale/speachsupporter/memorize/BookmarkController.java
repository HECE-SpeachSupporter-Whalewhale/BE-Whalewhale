package com.whalewhale.speachsupporter.memorize;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import com.whalewhale.speachsupporter.Presentation.PresentationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final PresentationRepository presentationRepository;

    @PostMapping("/toggle/{id}")
    public String toggleBookmark(@PathVariable Integer id) { //id로 발표자료 찾기
        Presentation presentation = presentationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid presentation Id:" + id));
        presentation.setIsBookmarked(!presentation.getIsBookmarked());
        presentationRepository.save(presentation);
        return "redirect:/search";
    }
}