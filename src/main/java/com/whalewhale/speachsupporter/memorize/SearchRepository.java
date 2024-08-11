package com.whalewhale.speachsupporter.memorize;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<Presentation, Integer> {
    List<Presentation> findAllByOrderByCreatedAtAsc();
    List<Presentation> findAllByOrderByCreatedAtDesc();
    List<Presentation> findByIsBookmarkedTrueOrderByCreatedAtDesc();
    List<Presentation> findByTitleContainingIgnoreCaseOrderByCreatedAtAsc(String title);
    List<Presentation> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);
    List<Presentation> findByTitleContainingIgnoreCaseAndIsBookmarkedTrueOrderByCreatedAtDesc(String title);
}
