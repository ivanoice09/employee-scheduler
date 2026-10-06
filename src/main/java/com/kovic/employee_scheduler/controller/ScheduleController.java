package com.kovic.employee_scheduler.controller;

import com.kovic.employee_scheduler.dto.SaveWeekDTO;
import com.kovic.employee_scheduler.dto.WeekDTO;
import com.kovic.employee_scheduler.helper.DemoSessionHelper;
import com.kovic.employee_scheduler.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/schedule")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final DemoSessionHelper demoSessionHelper;

    @GetMapping("/{year}/{week}")
    public WeekDTO getWeek(@PathVariable int year,
                           @PathVariable int week,
                           HttpServletRequest request,
                           HttpServletResponse response
    ) {
        // extract the UUID received from the backend, if it doesn't exist...
        UUID demoSessionId = demoSessionHelper
                .readOrCreateDemoSessionRow(request, response) // create it
                .orElseThrow(() -> new IllegalStateException(
                        "Could not resolve cookie"
                ));

        return scheduleService.getWeek(year, week, demoSessionId);
    }

    @PostMapping("/save")
    public void saveWeek(@RequestBody SaveWeekDTO dto,
                         HttpServletRequest request,
                         HttpServletResponse response
    ) {
        UUID demoSessionId = demoSessionHelper
                .readOrCreateDemoSessionRow(request, response)
                .orElseThrow(() -> new IllegalStateException(
                        "Could not resolve cookie"
                ));

        scheduleService.saveWeek(dto, demoSessionId);
    }

}
