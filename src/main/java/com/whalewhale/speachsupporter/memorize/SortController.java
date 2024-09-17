package com.whalewhale.speachsupporter.memorize;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import com.whalewhale.speachsupporter.Users.Users;
import com.whalewhale.speachsupporter.Users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sort")
public class SortController {

    private final PresentationSortRepository presentationSortRepository;
    private final UsersRepository usersRepository;

    @PostMapping("/search")
    public ResponseEntity<List<Presentation>> searchPresentations(@RequestBody Map<String, Object> requestBody) {
        String title = (String) requestBody.getOrDefault("title", "");
        String sort = (String) requestBody.getOrDefault("sort", "latest");

        // 현재 로그인된 유저 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = usersRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Presentation> presentations;

        // 정렬 조건에 따라 로그인된 유저의 프레젠테이션을 필터링
        switch (sort) {
            case "name":
                presentations = presentationSortRepository.findByUserAndTitleContainingOrderByTitleAsc(currentUser, title);
                break;
            case "bookmarks":
                presentations = presentationSortRepository.findByUserAndTitleContainingAndIsBookmarkedTrueOrderByCreatedAtDesc(currentUser, title);
                break;
            case "latest":
            default:
                presentations = presentationSortRepository.findByUserAndTitleContainingOrderByCreatedAtDesc(currentUser, title);
                break;
        }

        return ResponseEntity.ok(presentations);
    }
}
