package com.whalewhale.speachsupporter.memorize;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import com.whalewhale.speachsupporter.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<Presentation, Integer> {
    List<Presentation> findAllByUserOrderByCreatedAtAsc(Users user);
    List<Presentation> findAllByUserOrderByCreatedAtDesc(Users user);
    List<Presentation> findByUserAndIsBookmarkedTrueOrderByCreatedAtDesc(Users user);
    List<Presentation> findByUserAndTitleContainingIgnoreCaseOrderByCreatedAtAsc(Users user, String title);
    List<Presentation> findByUserAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(Users user, String title);
    List<Presentation> findByUserAndTitleContainingIgnoreCaseAndIsBookmarkedTrueOrderByCreatedAtDesc(Users user, String title);
}