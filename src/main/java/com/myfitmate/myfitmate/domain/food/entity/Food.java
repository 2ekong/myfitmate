package com.myfitmate.myfitmate.domain.food.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "food")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto_increment 설정
    private Long id;

    @Column(nullable = false, unique = true) // 이름 중복 방지
    private String name;

    private Float standardAmount;   // 1회 제공량

    private Float calories;

    private Float protein;

    private Float fat;

    private Float carbohydrate;

    private Float sodium;

    @Column(name = "reference_basis")
    private String referenceBasis = "per 100g";

    @Column(name = "created_at", columnDefinition = "datetime default current_timestamp")
    private java.time.LocalDateTime createdAt;
}
