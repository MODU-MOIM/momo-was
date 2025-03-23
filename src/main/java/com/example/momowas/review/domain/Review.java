package com.example.momowas.review.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String comment;

    protected Double rating;

}

