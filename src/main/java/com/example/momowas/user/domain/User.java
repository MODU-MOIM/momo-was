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

    @Column
    private String password;

    @Column
    private String nickname;

    @Column
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


    @Column(name = "provider", nullable = false, length = 10)
    private String provider;

    @Column(name = "provider_id", nullable = false, length = 50)
    private String providerId;

    @Builder

    public User(String email, String password, String nickname, String cp, double score, LocalDateTime createdAt, Gender gender, int age, String profileImage, String provider, String providerId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.cp = cp;
        this.score = score;
        this.createdAt = createdAt;
        this.gender = gender;
        this.age = age;
        this.profileImage = profileImage;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void updateProfileImage(String newProfileImage){
        this.profileImage = newProfileImage;
    }

    public void updateUserInfo(String newNickname, String newCp, Gender newGender, int newAge){
        this.nickname = newNickname;
        this.cp = newCp;
        this.gender = newGender;
        this.age= newAge;
    }
}
