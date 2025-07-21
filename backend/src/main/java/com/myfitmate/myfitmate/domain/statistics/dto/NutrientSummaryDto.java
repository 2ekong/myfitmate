package com.myfitmate.myfitmate.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NutrientSummaryDto {
    private Double protein;
    private Double fat;
    private Double carbohydrate;
}
