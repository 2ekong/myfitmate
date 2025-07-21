package com.myfitmate.myfitmate.domain.exercise.controller;

import com.myfitmate.myfitmate.domain.exercise.dto.ExerciseSimpleDto;
import com.myfitmate.myfitmate.domain.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exercise")
public class ExerciseController {

    private final ExerciseRepository exerciseRepository;

    @GetMapping
    public List<ExerciseSimpleDto> getAllExercises() {
        return exerciseRepository.findAll().stream()
                .map(e -> new ExerciseSimpleDto(e.getId(), e.getName()))
                .toList();
    }
}
