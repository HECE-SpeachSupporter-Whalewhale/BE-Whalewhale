package com.whalewhale.speachsupporter.Speed;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.whalewhale.speachsupporter.Presentation.Presentation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Speed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate speed_id
    private Integer speed_id;

    @OneToOne
    @JoinColumn(name = "presentation_id", nullable = false) // Presentation ID를 외래키로 사용
    @JsonBackReference // 무한 루프 방지
    private Presentation presentation;

    private Boolean speed_check;
    private Integer speed_minute;
    private Integer speed_second;
}
