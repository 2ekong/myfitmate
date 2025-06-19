package com.myfitmate.myfitmate.domain.food.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.food.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodOpenApiService {

    private final RestTemplate restTemplate;
    private final FoodRepository foodRepository;

    private static final String SERVICE_KEY = "J%2FJalA8XAuS31dPgptdrfWvvllBvJyLmwoVkHd7rV7S%2BO1VfV1rUdVLIMNsWLFhiMgacMEfiH8bbyK7ffY6IUA%3D%3D";
    private static final String BASE_URL = "https://api.odcloud.kr/api/15097972/v1/uddi:780a2373-bf11-4fb6-b3e4-ed4119571817";

    public void fetchAndSave() {
        try {
            String json = requestFoodData();
            List<Food> foodList = parseJsonToFoodList(json);
            foodRepository.saveAll(foodList);
            log.info("{}개의 음식 정보를 저장했습니다.", foodList.size());
        } catch (IOException e) {
            log.error("JSON 파싱 오류: {}", e.getMessage());
        } catch (Exception e) {
            log.error("저장 실패: {}", e.getMessage());
        }
    }

    private String requestFoodData() {
        int page = 1;
        int perPage = 100;

        URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("page", page)
                .queryParam("perPage", perPage)
                .queryParam("serviceKey", SERVICE_KEY)
                .build(true)
                .toUri();
        return restTemplate.getForObject(uri, String.class);
    }

    private List<Food> parseJsonToFoodList(String json) throws IOException {
        List<Food> foodList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode dataArray = root.path("data");

        for (JsonNode node : dataArray) {
            String name = node.path("식품명").asText();
            double calories = node.path("에너지(㎉)").asDouble(0.0);
            double protein = node.path("단백질(g)").asDouble(0.0);
            double fat = node.path("지방(g)").asDouble(0.0);
            double carb = node.path("탄수화물(g)").asDouble(0.0);
            double sodium = node.path("나트륨(㎎)").asDouble(0.0);

            if (!foodRepository.existsByName(name)) {
                Food food = Food.builder()
                        .name(name)
                        .calories((float) calories)
                        .protein((float) protein)
                        .fat((float) fat)
                        .carbohydrate((float) carb)
                        .sodium((float) sodium)
                        .referenceBasis("per 100g")
                        .build();
                foodList.add(food);
            }
        }

        return foodList;
    }
}
