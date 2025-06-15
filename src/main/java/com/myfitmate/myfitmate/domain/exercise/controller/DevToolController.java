package com.myfitmate.myfitmate.domain.exercise.controller;

import com.myfitmate.myfitmate.domain.exercise.service.ExerciseCsvImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dev")
public class DevToolController {

    private final ExerciseCsvImportService exerciseCsvImportService;

    @PostMapping("/import-exercises")
    public ResponseEntity<String> importExercises() throws IOException {
        exerciseCsvImportService.importExercises();
        return ResponseEntity.ok("운동 데이터 저장 완료");
    }
}