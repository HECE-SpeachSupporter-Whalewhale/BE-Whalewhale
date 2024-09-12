package com.whalewhale.speachsupporter.memorize;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sort")
public class SortController {

    private final PresentationSortRepository presentationSortRepository;

    @PostMapping("/search")
    public ResponseEntity<List<Presentation>> searchPresentations(@RequestBody Map<String, Object> requestBody) {
        String title = (String) requestBody.getOrDefault("title", "");
        String sort = (String) requestBody.getOrDefault("sort", "latest");

        List<Presentation> presentations;

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

        return ResponseEntity.ok(presentations);
    }
}
