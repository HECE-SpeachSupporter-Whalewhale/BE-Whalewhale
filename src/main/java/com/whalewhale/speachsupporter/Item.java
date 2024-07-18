package com.whalewhale.speachsupporter;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)//not null
//    @Column(columnDefinition = "TEXT", unique = true)// 길이연장, 유니크 설정
    private String title;
    private Integer price;
    //변수에 public 붙이면 다른 모든 클래스에서 문제없이 사용 가능


}
