package com.kovic.employee_scheduler.controller;

import com.kovic.employee_scheduler.util.DemoSessionCleanupUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/demo-sessions")
@RequiredArgsConstructor
public class DemoSessionCleanupController {

    private final DemoSessionCleanupUtil demoSessionCleanupUtil;

    @PostMapping("/cleanup")
    public ResponseEntity<Void> cleanup() {
        demoSessionCleanupUtil.removeExpiredSession();
        return ResponseEntity.noContent().build();
    }
}
