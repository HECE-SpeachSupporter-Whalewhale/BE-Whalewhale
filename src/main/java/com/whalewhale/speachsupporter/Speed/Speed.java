package com.whalewhale.speachsupporter.Speed;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Speed {
    @Id
    private Integer presentation_id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "presentation_id")
    private Presentation presentation;

    private Boolean speed_check;
    private Integer speed_minute;
    private Integer speed_second;
}