package com.whalewhale.speachsupporter.memorize;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import com.whalewhale.speachsupporter.Presentation.PresentationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final PresentationRepository presentationRepository;

    @PostMapping("/toggle/{id}")
    public ResponseEntity<Presentation> toggleBookmark(@PathVariable Integer id, @RequestBody Presentation request) {
        Presentation presentation = presentationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 프레젠테이션 ID: " + id));

        // 요청 본문에서 isBookmarked 값을 설정합니다.
        presentation.setIsBookmarked(request.getIsBookmarked());
        presentationRepository.save(presentation);

        return ResponseEntity.ok(presentation);
    }
}
