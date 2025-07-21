package com.myfitmate.myfitmate.domain.food.service;

import com.myfitmate.myfitmate.domain.food.dto.FoodCsvDto;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodCsvService {

    private List<FoodCsvDto> cachedCsvFoods;

    @PostConstruct
    public void loadCsvData() {
        try {
            // ✅ CSV 경로 설정
            String path = "data/전국통합식품영양성분정보_음식_표준데이터.csv";
            InputStream is = getClass().getClassLoader().getResourceAsStream(path);
            if (is == null) {
                throw new RuntimeException("📛 CSV 파일을 찾을 수 없습니다: " + path);
            }

            InputStreamReader reader = new InputStreamReader(is, Charset.forName("EUC-KR"));
            BufferedReader bufferedReader = new BufferedReader(reader);

            // ✅ 첫 줄 (헤더) 확인 로그
            bufferedReader.mark(10000); // mark/reset 가능하게
            String headerLine = bufferedReader.readLine();
            log.info("📌 CSV 헤더 확인: {}", headerLine);
            bufferedReader.reset(); // 다시 처음부터 읽을 수 있게

            // ✅ CsvToBeanBuilder로 파싱
            cachedCsvFoods = new CsvToBeanBuilder<FoodCsvDto>(bufferedReader)
                    .withType(FoodCsvDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();

            log.info("✅ CSV 음식 데이터 총 {}건 로딩 완료", cachedCsvFoods.size());

            // ✅ 상위 10개 샘플 출력
            for (int i = 0; i < Math.min(10, cachedCsvFoods.size()); i++) {
                FoodCsvDto food = cachedCsvFoods.get(i);
                log.info("✔️ [{}] {} | kcal={} | fat={} | sodium={} | stdAmt={}",
                        i + 1, food.getName(), food.getCalories(),
                        food.getFat(), food.getSodium(), food.getStandardAmount());
            }

            // ✅ 누락 항목 확인
            cachedCsvFoods.stream()
                    .filter(f -> f.getCalories() == null || f.getFat() == null || f.getSodium() == null || f.getStandardAmount() == null)
                    .limit(10)
                    .forEach(f -> log.warn("⚠️ 누락 데이터 → name={} | kcal={} | fat={} | sodium={} | stdAmt={}",
                            f.getName(), f.getCalories(), f.getFat(), f.getSodium(), f.getStandardAmount()));

        } catch (Exception e) {
            log.error("❌ CSV 로딩 실패: {}", e.getMessage(), e);
            throw new RuntimeException("CSV 파싱 실패", e);
        }
    }

    // ✅ 전체 조회
    public List<FoodCsvDto> getAllFoods() {
        return cachedCsvFoods;
    }

    // ✅ 키워드 검색 (다중 필드 포함)
    public List<FoodCsvDto> searchFoods(String keyword) {
        if (keyword == null || keyword.isBlank()) {

            log.warn("❗ 빈 keyword 요청 → 빈 리스트 반환");
            return List.of(); // 또는 Collections.emptyList()
        }

        String lowerKeyword = keyword.toLowerCase();

        List<FoodCsvDto> results = cachedCsvFoods.stream()
                .filter(f -> {
                    String name = f.getName();
                    String category = f.getOriginCategory();
                    return (name != null && name.toLowerCase().contains(lowerKeyword)) ||
                            (category != null && category.toLowerCase().contains(lowerKeyword));
                })
                .limit(10) // ✅ 최대 10개 제한
                .toList();

        log.info("🔍 검색어 '{}' 결과 {}건 반환", keyword, results.size());
        return results;
    }

}
