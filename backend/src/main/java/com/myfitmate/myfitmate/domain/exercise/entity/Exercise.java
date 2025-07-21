package com.myfitmate.myfitmate.domain.exercise.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercise")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Float mets;

    private String source;
}
