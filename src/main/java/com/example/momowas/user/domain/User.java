package com.example.momowas.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String cp;

    @Column
    private double score;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private int age;

    @Column
    private String profileImage;

    @Builder
    public User(String email, String password, String nickname, String cp, double score, LocalDateTime createdAt, Gender gender, int age, String profileImage) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.cp = cp;
        this.score = score;
        this.createdAt = createdAt;
        this.gender = gender;
        this.age = age;
        this.profileImage = profileImage;
    }
}
