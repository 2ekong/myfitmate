package com.myfitmate.myfitmate.domain.exercise.repository;

import com.myfitmate.myfitmate.domain.exercise.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    boolean existsByName(String name);
}
