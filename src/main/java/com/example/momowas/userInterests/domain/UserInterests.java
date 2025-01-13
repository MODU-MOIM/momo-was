package com.example.momowas.userInterests.domain;

import com.example.momowas.crew.domain.Category;
import com.example.momowas.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserInterests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    private Category category;

    @Builder
    public UserInterests(User user, Category category) {
        this.user = user;
        this.category = category;
    }
}
