package com.kovic.employee_scheduler.service;

import com.kovic.employee_scheduler.model.Shift;
import com.kovic.employee_scheduler.repository.ShiftRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;

    public void deleteShift(Long shiftId, UUID demoSessionId) {
        Shift shift = shiftRepository
                .findByIdAndDemoSessionId(shiftId, demoSessionId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shift not found for demo session: " + demoSessionId
                ));

        shiftRepository.delete(shift);
    }
}
