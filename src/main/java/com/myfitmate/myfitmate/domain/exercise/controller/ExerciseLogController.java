package com.myfitmate.myfitmate.domain.exercise.controller;

import com.myfitmate.myfitmate.domain.exercise.dto.ExerciseLogRequestDto;
import com.myfitmate.myfitmate.domain.exercise.dto.ExerciseLogResponseDto;
import com.myfitmate.myfitmate.domain.exercise.service.ExerciseLogService;
import com.myfitmate.myfitmate.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exercise/log")
public class ExerciseLogController {

    private final ExerciseLogService exerciseLogService;

    @PostMapping
    public ResponseEntity<?> logExercise(@RequestBody @Valid ExerciseLogRequestDto dto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        exerciseLogService.logExercise(dto, userDetails.getUser().getId());
        return ResponseEntity.ok("운동 기록 완료");
    }

    @GetMapping
    public ResponseEntity<List<ExerciseLogResponseDto>> getLogs(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,

            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        List<ExerciseLogResponseDto> logs = exerciseLogService.getLogsByUser(userId, date);
        return ResponseEntity.ok(logs);
    }

}
