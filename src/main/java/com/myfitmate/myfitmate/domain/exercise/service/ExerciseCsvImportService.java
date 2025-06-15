package com.myfitmate.myfitmate.domain.exercise.service;

import com.myfitmate.myfitmate.domain.exercise.entity.Exercise;
import com.myfitmate.myfitmate.domain.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class ExerciseCsvImportService {

    private final ExerciseRepository exerciseRepository;

    public void importExercises() {
        System.out.println("CSV 데이터 가져오는 중");

        int count = 0;
        int maxCount = 500;

        try (InputStream is = getClass().getResourceAsStream("/data/한국건강증진개발원_보건소 모바일 헬스케어 운동_20240919.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("EUC-KR")))) {

            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }

                if (count >= maxCount) break;

                try {
                    String[] tokens = line.split(",");
                    String name = tokens[0].trim();
                    float mets = Float.parseFloat(tokens[1].trim());

                    if (!exerciseRepository.existsByName(name)) {
                        Exercise exercise = Exercise.builder()
                                .name(name)
                                .mets(mets)
                                .source("csv")
                                .build();
                        exerciseRepository.save(exercise);
                        count++;
                        System.out.println("저장됨: " + name);
                    }

                } catch (Exception e) {
                    System.out.println("CSV 한 줄 처리 실패 → " + line);
                    System.out.println("이유: " + e.getMessage());
                    // continue 자동으로 됨
                }
            }

            System.out.println("총 저장된 운동 개수: " + count);

        } catch (Exception e) {
            System.out.println("CSV 전체 처리 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


