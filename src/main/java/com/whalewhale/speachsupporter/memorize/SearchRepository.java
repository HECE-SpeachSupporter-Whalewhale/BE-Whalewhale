package com.whalewhale.speachsupporter.memorize;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SearchRepository extends JpaRepository<Presentation, Integer> {
    List<Presentation> findByTitleContainingIgnoreCase(String title);
}