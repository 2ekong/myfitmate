package com.myfitmate.myfitmate.domain.food.repository;

import com.myfitmate.myfitmate.domain.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {

    // 이름 기준 전체 검색 (키워드 포함, 대소문자 구분 없이)
    List<Food> findByNameContainingIgnoreCase(String keyword);

    // 이름으로 단건 검색
    Food findByName(String name);

    // 중복 검사
    boolean existsByName(String name);
}
