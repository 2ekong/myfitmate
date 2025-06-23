package com.myfitmate.myfitmate.domain.food.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.AbstractBeanField;
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

    @CsvCustomBindByName(column = "영양성분함량기준량", converter = NullableFloatConverter.class)
    private Float standardAmount;

    @CsvCustomBindByName(column = "에너지(kcal)", converter = NullableFloatConverter.class)
    private Float calories;

    @CsvCustomBindByName(column = "단백질(g)", converter = NullableFloatConverter.class)
    private Float protein;

    @CsvCustomBindByName(column = "지방(g)", converter = NullableFloatConverter.class)
    private Float fat;

    @CsvCustomBindByName(column = "탄수화물(g)", converter = NullableFloatConverter.class)
    private Float carbohydrate;

    @CsvCustomBindByName(column = "나트륨(mg)", converter = NullableFloatConverter.class)
    private Float sodium;

    @CsvBindByName(column = "영양성분함량기준")
    private String referenceBasis;

    // 내부 클래스로 NullableFloatConverter 정의 (패키지 분리 안함)
    public static class NullableFloatConverter extends AbstractBeanField<Float, String> {
        @Override
        protected Float convert(String value) {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            try {
                return Float.parseFloat(value.trim());
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }
}
