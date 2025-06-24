package com.myfitmate.myfitmate.domain.food.entity;

import com.myfitmate.myfitmate.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "food", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "name"})
})
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 식품명

    @Column(name = "origin_category")
    private String originCategory; // 식품대분류명

    @Column(name = "origin_sub_category")
    private String originSubCategory; // 식품중분류명

    @Column(name = "origin_detail_category")
    private String originDetailCategory; // 식품소분류명

    private Float standardAmount; // 영양성분함량기준량 (예: 100g, 1개 등)
    private Float calories;       // 에너지(kcal)
    private Float protein;        // 단백질(g)
    private Float fat;            // 지방(g)
    private Float carbohydrate;   // 탄수화물(g)
    private Float sodium;         // 나트륨(mg)

    @Column(name = "reference_basis")
    private String referenceBasis; // 기준 문자열 (ex. per 100g 등)

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.referenceBasis == null || this.referenceBasis.isBlank()) {
            this.referenceBasis = "per 기준량";
        }
    }
}
