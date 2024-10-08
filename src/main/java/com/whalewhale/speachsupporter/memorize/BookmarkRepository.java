package com.whalewhale.speachsupporter.memorize;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Presentation, Integer> {
    List<Presentation> findByIsBookmarkedOrderByCreatedAtDesc(boolean isBookmarked);
    List<Presentation> findByIsBookmarkedFalseOrderByCreatedAtDesc();
}