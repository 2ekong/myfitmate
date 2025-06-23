package com.myfitmate.myfitmate.domain.food.service;

import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.food.repository.FoodRepository;
import com.myfitmate.myfitmate.domain.user.entity.User;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParserBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodCsvService {

    private final FoodRepository foodRepository;

    public List<Food> readFoodListFromCsv(MultipartFile file) {
        return parseCsv(file, null);
    }

    public void importFoodDataFromCsv(MultipartFile file, User user) {
        List<Food> foodList = parseCsv(file, user);
        foodRepository.saveAll(foodList);
    }

    private List<Food> parseCsv(MultipartFile file, User user) {
        try (
                CSVReader reader = new CSVReaderBuilder(
                        new InputStreamReader(file.getInputStream(), Charset.forName("EUC-KR")))
                        .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
                        .build()
        ) {
            List<Food> foodList = new ArrayList<>();
            String[] line;
            boolean isFirst = true;
            int lineNumber = 1;

            while ((line = reader.readNext()) != null) {
                lineNumber++;
                if (isFirst) {
                    isFirst = false;
                    continue;
                }

                log.debug("📄 [줄 {}] 전체 내용: {}", lineNumber, String.join(" | ", line));

                try {
                    Food food = Food.builder()
                            .name(getRequiredValue(line, 1, "식품명", lineNumber))
                            .originCategory(getRequiredValue(line, 7, "대분류", lineNumber))
                            .originSubCategory(getRequiredValue(line, 11, "중분류", lineNumber))
                            .originDetailCategory(getRequiredValue(line, 13, "소분류", lineNumber))
                            .standardAmount(parseFloatSafe(line, 16, "기준량", lineNumber))
                            .calories(parseFloatSafe(line, 17, "칼로리", lineNumber))
                            .protein(parseFloatSafe(line, 19, "단백질", lineNumber))
                            .fat(parseFloatSafe(line, 20, "지방", lineNumber))
                            .carbohydrate(parseFloatSafe(line, 22, "탄수화물", lineNumber))
                            .sodium(parseFloatSafe(line, 29, "나트륨", lineNumber))
                            .referenceBasis("per 기준량")
                            .user(user)
                            .build();

                    foodList.add(food);

                } catch (Exception e) {
                    log.error("❌ [{}번째 줄 오류] {}", lineNumber, e.getMessage());
                }
            }

            return foodList;

        } catch (Exception e) {
            throw new RuntimeException("CSV 파싱 실패: " + e.getMessage(), e);
        }
    }

    private String getRequiredValue(String[] line, int index, String fieldName, int lineNumber) {
        if (line.length <= index || isBlank(line[index])) {
            String msg = "필수값 누락: " + fieldName + " (index=" + index + ")";
            log.warn("⚠️ [줄 {}] {}", lineNumber, msg);
            throw new RuntimeException(msg);
        }
        log.debug("✅ [줄 {}] {} = {}", lineNumber, fieldName, line[index].trim());
        return line[index].trim();
    }

    private Float parseFloatSafe(String[] line, int index, String fieldName, int lineNumber) {
        try {
            if (line.length <= index || isBlank(line[index])) {
                log.warn("⚠️ [줄 {}] {} 값 없음 (index={})", lineNumber, fieldName, index);
                return null;
            }
            String raw = line[index].trim().replace(",", "");
            Float value = Float.parseFloat(raw);
            log.debug("✅ [줄 {}] {} = {}", lineNumber, fieldName, value);
            return value;
        } catch (NumberFormatException e) {
            log.warn("⚠️ [줄 {}] {} 숫자 파싱 실패: '{}'", lineNumber, fieldName, line[index]);
            return null;
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
