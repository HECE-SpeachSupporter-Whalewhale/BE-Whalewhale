package com.whalewhale.speachsupporter.Presentation;

import com.whalewhale.speachsupporter.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresentationRepository extends JpaRepository<Presentation, Integer> {
    List<Presentation> findByUser(Users user);
}
