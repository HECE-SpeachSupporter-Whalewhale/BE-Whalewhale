package com.whalewhale.speachsupporter.memorize;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresentationSortRepository extends JpaRepository<Presentation, Integer> {
    List<Presentation> findByTitleContainingOrderByCreatedAtDesc(String title);
    List<Presentation> findByTitleContainingOrderByTitleAsc(String title);
    List<Presentation> findByTitleContainingOrderByIsBookmarkedDescCreatedAtDesc(String title);
}