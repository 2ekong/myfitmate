package com.myfitmate.myfitmate.domain.meal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "meal_image")
public class MealImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long mealId;

    private String filePath;

    private String hash;

    @Column(name = "uploaded_at", columnDefinition = "datetime default current_timestamp")
    private LocalDateTime uploadedAt;
}
