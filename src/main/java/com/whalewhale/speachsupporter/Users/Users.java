package com.whalewhale.speachsupporter.Users;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String user_job;

    @Column(nullable = false)
    private Boolean isAdmin;
}