package com.whalewhale.speachsupporter.Presentation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.whalewhale.speachsupporter.Speed.Speed;
import com.whalewhale.speachsupporter.Users.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Presentation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer presentation_id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Users user;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean isBookmarked = false;

    @PrePersist
    protected void onCreate() {
        if (title == null || title.isEmpty()) {
            title = "제목 없는 글입니다.";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @OneToOne(mappedBy = "presentation", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonManagedReference // 무한 루프 방지
    private Speed speed;

    @Override
    public String toString() {
        return "Presentation{" +
                "presentation_id=" + presentation_id +
                ", title='" + title + '\'' +
                ", body='" + (body != null ? body.substring(0, Math.min(body.length(), 20)) + "..." : null) + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isBookmarked=" + isBookmarked +
                '}';
    }
}
