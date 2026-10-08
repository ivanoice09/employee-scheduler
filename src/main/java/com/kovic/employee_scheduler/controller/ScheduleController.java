package com.kovic.employee_scheduler.controller;

import com.kovic.employee_scheduler.dto.SaveWeekDTO;
import com.kovic.employee_scheduler.dto.WeekDTO;
import com.kovic.employee_scheduler.service.ScheduleService;
import com.kovic.employee_scheduler.util.DemoSessionRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/schedule")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/{year}/{week}")
    public WeekDTO getWeek(@PathVariable int year,
                           @PathVariable int week,
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
