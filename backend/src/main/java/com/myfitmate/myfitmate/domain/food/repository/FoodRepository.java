package com.myfitmate.myfitmate.domain.food.repository;

import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {

    boolean existsByNameAndUser(String name, User user);

    List<Food> findAllByUser(User user);

    List<Food> findByNameContainingIgnoreCaseAndUser(String keyword, User user);
}
