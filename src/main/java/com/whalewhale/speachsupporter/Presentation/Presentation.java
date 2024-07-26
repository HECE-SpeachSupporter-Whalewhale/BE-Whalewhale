package com.whalewhale.speachsupporter.Presentation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

    @Entity
    @ToString
    @Getter
    @Setter
    public class Presentation {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer presentation_id;

        @Column(nullable = false)
        private String title;

        @Column(columnDefinition = "TEXT")
        private String body; // TEXT 자료형

        @Column(nullable = false)
        private Integer user_id;

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt; // 생성 시간

        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt; // 수정 시간// TIMESTAMP 자료형

    }

