package com.myfitmate.myfitmate.domain.food.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodCsvDto {

    @CsvBindByName(column = "식품명")
    private String name;

    @CsvBindByName(column = "식품대분류명")
    private String originCategory;

    @CsvBindByName(column = "식품중분류명")
    private String originSubCategory;

    @CsvBindByName(column = "식품소분류명")
    private String originDetailCategory;

    @CsvBindByName(column = "1회 섭취참고량")
    private String standardAmount;

    @CsvBindByName(column = "에너지(kcal)")
    private String calories;

    @CsvBindByName(column = "탄수화물(g)")
    private String carbohydrate;

    @CsvBindByName(column = "단백질(g)")
    private String protein;

    @CsvBindByName(column = "지방(g)")
    private String fat;

    @CsvBindByName(column = "나트륨(mg)")
    private String sodium;

    @CsvBindByName(column = "식품코드")
    private String code;

    @CsvBindByName(column = "출처명")
    private String source;

    @CsvBindByName(column = "제조사명")
    private String manufacturer;

    @CsvBindByName(column = "제공기관명")
    private String provider;

    @CsvBindByName(column = "데이터생성일자")
    private String dataCreatedAt;

    @CsvBindByName(column = "데이터기준일자")
    private String dataReferenceAt;
}
