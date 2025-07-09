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
            return cachedCsvFoods;
        }
        String lowerKeyword = keyword.toLowerCase();

        List<FoodCsvDto> results = cachedCsvFoods.stream()
                .filter(f ->
                        (f.getName() != null && f.getName().toLowerCase().contains(lowerKeyword)) ||
                                (f.getOriginCategory() != null && f.getOriginCategory().toLowerCase().contains(lowerKeyword)) ||
                                (f.getOriginSubCategory() != null && f.getOriginSubCategory().toLowerCase().contains(lowerKeyword)) ||
                                (f.getOriginDetailCategory() != null && f.getOriginDetailCategory().toLowerCase().contains(lowerKeyword)) ||
                                (f.getCode() != null && f.getCode().toLowerCase().contains(lowerKeyword)) ||
                                (f.getSource() != null && f.getSource().toLowerCase().contains(lowerKeyword)) ||
                                (f.getManufacturer() != null && f.getManufacturer().toLowerCase().contains(lowerKeyword))
                )
                .toList();

        log.info("🔍 '{}' 검색 결과: {}건", keyword, results.size());
        return results;
    }
}
