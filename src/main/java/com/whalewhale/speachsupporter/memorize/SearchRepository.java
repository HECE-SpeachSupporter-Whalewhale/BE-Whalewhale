package com.whalewhale.speachsupporter.memorize;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRepository extends JpaRepository<Presentation, Integer> {
    List<Presentation> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);
    List<Presentation> findByTitleContainingIgnoreCaseOrderByCreatedAtAsc(String title);
    List<Presentation> findAllByOrderByCreatedAtDesc();
    List<Presentation> findAllByOrderByCreatedAtAsc();
    List<Presentation> findByIsBookmarkedTrueOrderByCreatedAtDesc();
    List<Presentation> findByTitleContainingIgnoreCaseAndIsBookmarkedTrueOrderByCreatedAtDesc(String title);
}