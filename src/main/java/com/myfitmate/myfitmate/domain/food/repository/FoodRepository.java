package com.myfitmate.myfitmate.domain.food.repository;

import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {

    // 이름 기준 전체 검색 (키워드 포함, 대소문자 구분 없이)
    List<Food> findByNameContainingIgnoreCase(String keyword);

    // 사용자 전체 음식 조회
    List<Food> findByUser(User user);

    // 사용자 + 키워드 검색
    List<Food> findByUserAndNameContainingIgnoreCase(User user, String keyword);

    // 이름으로 단건 검색
    Food findByName(String name);

    // 중복 검사
    boolean existsByName(String name);
}
