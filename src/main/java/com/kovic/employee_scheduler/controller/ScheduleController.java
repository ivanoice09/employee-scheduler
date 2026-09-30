package com.kovic.employee_scheduler.controller;

import com.kovic.employee_scheduler.dto.SaveWeekDTO;
import com.kovic.employee_scheduler.dto.WeekDTO;
import com.kovic.employee_scheduler.service.ScheduleService;
import com.kovic.employee_scheduler.util.DemoSessionContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final DemoSessionContext demoSessionContext;

    @GetMapping("/{year}/{week}")
    public WeekDTO getWeek(@PathVariable int year, @PathVariable int week, HttpServletResponse response) {
        long expiryEpochMs = demoSessionContext.getExpiryEpochMs();
        response.setHeader("X-Demo-Session-Expiry", String.valueOf(expiryEpochMs));
        return scheduleService.getWeek(year, week);
    }

    @PostMapping("/save")
    public void saveWeek(@RequestBody SaveWeekDTO dto) {
        scheduleService.saveWeek(dto);
    }

}
