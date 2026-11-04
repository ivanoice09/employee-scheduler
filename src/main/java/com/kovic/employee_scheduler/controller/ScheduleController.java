package com.kovic.employee_scheduler.controller;

import com.kovic.employee_scheduler.dto.SaveWeekDTO;
import com.kovic.employee_scheduler.dto.WeekDTO;
import com.kovic.employee_scheduler.service.ScheduleService;
import com.kovic.employee_scheduler.util.DemoSessionRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/{year}/{week}")
    public WeekDTO getWeek(
            @PathVariable @Min(2000) @Max(2100) int year,
            @PathVariable @Min(1) @Max(53) int week,
            HttpServletRequest request
    ) {
        UUID demoSessionId = DemoSessionRequestUtil.getDemoSessionId(request);
        return scheduleService.getWeek(year, week, demoSessionId);
    }

    @PostMapping("/save")
    public void saveWeek(@RequestBody SaveWeekDTO dto,
                         HttpServletRequest request
    ) {
        UUID demoSessionId = DemoSessionRequestUtil.getDemoSessionId(request);
        scheduleService.saveWeek(dto, demoSessionId);
    }


}
