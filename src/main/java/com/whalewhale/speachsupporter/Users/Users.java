package com.whalewhale.speachsupporter.Users;

import com.whalewhale.speachsupporter.Presentation.Presentation;
import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Email
    @Column(unique = true, nullable = false)
    private String username;

    @NotEmpty
    @Size(min = 8, max = 12, message = "비밀번호는 8자에서 12자 사이어야 합니다.")
    @Column(nullable = false)
    private String password;

    @NotEmpty
    @Size(max = 4, message = "닉네임은 4자 이하로 설정해야 합니다.")
    @Column(nullable = false)
    private String nickname;

    @Column
    private String user_job;

    @NotEmpty
    @Column(nullable = false)
    private Boolean isAdmin;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Presentation> presentations;
}
