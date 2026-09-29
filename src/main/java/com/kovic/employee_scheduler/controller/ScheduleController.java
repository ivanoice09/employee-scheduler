package com.kovic.employee_scheduler.controller;

import com.kovic.employee_scheduler.dto.SaveWeekDTO;
import com.kovic.employee_scheduler.dto.WeekDTO;
import com.kovic.employee_scheduler.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
@CrossOrigin
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/{year}/{week}")
    public WeekDTO getWeek(@PathVariable int year, @PathVariable int week) {
        return scheduleService.getWeek(year, week);
    }

    @PostMapping("/save")
    public void saveWeek(@RequestBody SaveWeekDTO dto) {
        scheduleService.saveWeek(dto);
    }
}
