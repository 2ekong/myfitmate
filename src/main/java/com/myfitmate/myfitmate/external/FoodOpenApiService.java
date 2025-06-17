package com.myfitmate.myfitmate.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.food.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodOpenApiService {

    private final RestTemplate restTemplate;

    private final String SERVICE_KEY = "J%2FJalA8XAuS31dPgptdrfWvvllBvJyLmwoVkHd7rV7S%2BO1VfV1rUdVLIMNsWLFhiMgacMEfiH8bbyK7ffY6IUA%3D%3D";
    private final FoodRepository foodRepository;

    public String getFoodData(int page, int perPage){
        URI uri = UriComponentsBuilder.fromUriString("https://api.odcloud.kr/api/15097972/v1/uddi:780a2373-bf11-4fb6-b3e4-ed4119571817")
                .queryParam("page", page)
                .queryParam("perPage", perPage)
                .queryParam("serviceKey", SERVICE_KEY)
                .build(true)    //인코딩 된 상태 ㅇ지
                .toUri();
        return restTemplate.getForObject(uri, String.class);
    }

    //json 파싱 및 food 리스트로 반환
    public List<Food> parseFoodData(String json) throws IOException {
        List<Food> foodList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode dataArray = root.path("data");

        for (JsonNode node : dataArray) {
            String name = node.path("식품명").asText();
            double calories = node.path("에너지(㎉)").asDouble();
            double protein = node.path("단백질(g)").asDouble();
            double fat = node.path("지방(g)").asDouble();
            double carb = node.path("탄수화물(g)").asDouble();
            double sodium = node.path("나트륨(㎎)").asDouble();

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


    public void fetchAndSave(int page, int perPage) throws IOException {
        String json = getFoodData(page, perPage);
        List<Food> foodList = parseFoodData(json);
        foodRepository.saveAll(foodList);
    }

}
