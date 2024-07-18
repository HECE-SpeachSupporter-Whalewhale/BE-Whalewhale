package com.whalewhale.speachsupporter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Notice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String 글제목;
    public String 날짜;

}
