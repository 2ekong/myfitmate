package com.myfitmate.myfitmate.domain.statistics.controller;

import com.myfitmate.myfitmate.domain.statistics.dto.NutrientSummaryDto;
import com.myfitmate.myfitmate.domain.statistics.dto.WeeklyCaloriesResponseDto;
import com.myfitmate.myfitmate.domain.statistics.service.StatisticsService;
import com.myfitmate.myfitmate.domain.user.entity.User;
import com.myfitmate.myfitmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/weekly")
    public WeeklyCaloriesResponseDto getWeeklyStatistics(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        return statisticsService.getWeeklyCalories(userId);
    }

    @GetMapping("/nutrients/today")
    public NutrientSummaryDto getTodayNutrients(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        return statisticsService.getTodayNutrients(userId);
    }
}