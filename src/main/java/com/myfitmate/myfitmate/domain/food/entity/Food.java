package com.myfitmate.myfitmate.domain.food.entity;

import com.myfitmate.myfitmate.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "food")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private Float standardAmount;
    private Float calories;
    private Float protein;
    private Float fat;
    private Float carbohydrate;
    private Float sodium;

    @Column(name = "reference_basis")
    private String referenceBasis = "per 100g";

    @Column(name = "created_at", columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
