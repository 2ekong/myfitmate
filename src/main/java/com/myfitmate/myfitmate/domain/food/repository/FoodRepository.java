package com.myfitmate.myfitmate.domain.food.repository;

import com.myfitmate.myfitmate.domain.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
    boolean existsByName(String name);
}
