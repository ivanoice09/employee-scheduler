package com.kovic.employee_scheduler.controller;

import com.kovic.employee_scheduler.service.ShiftService;
import com.kovic.employee_scheduler.util.DemoSessionRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/shift")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @DeleteMapping("/{shiftId}")
    public ResponseEntity<Void> deleteShift(
            @PathVariable Long shiftId,
            HttpServletRequest request
    ) {
        UUID demoSessionId = DemoSessionRequestUtil.getDemoSessionId(request);
        shiftService.deleteShift(shiftId, demoSessionId);
        return ResponseEntity.noContent().build();
    }
}
