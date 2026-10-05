package com.kovic.employee_scheduler.service;

import com.kovic.employee_scheduler.dto.*;
import com.kovic.employee_scheduler.model.DemoSession;
import com.kovic.employee_scheduler.model.Employee;
import com.kovic.employee_scheduler.model.Shift;
import com.kovic.employee_scheduler.model.Week;
import com.kovic.employee_scheduler.repository.DemoSessionRepository;
import com.kovic.employee_scheduler.repository.EmployeeRepository;
import com.kovic.employee_scheduler.repository.ShiftRepository;
import com.kovic.employee_scheduler.repository.WeekRepository;
import com.kovic.employee_scheduler.util.WeekUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ShiftRepository shiftRepository;
    private final EmployeeService employeeService;
    private final WeekRepository weekRepository;
    private final EmployeeRepository employeeRepository;
    private final DemoSessionRepository demoSessionRepository;

    @Transactional(readOnly = true)
    public WeekDTO getWeek(int year, int weekNumber, UUID demoSessionId) {
        return weekRepository
                // should filter week also by demo_session_id, so it selects the unique(year, weekNumber, demoSessionId)
                .findByYearAndWeekNumberAndDemoSessionId(year, weekNumber, demoSessionId)
                .filter(w -> demoSessionId != null) // enforce non-null demoSessionId
                .map(week -> buildWeekScheduleDTO(week, demoSessionId))
                .orElseGet(() -> buildEmptyTemplate(year, weekNumber));
    }

    private WeekDTO buildEmptyTemplate(int year, int weekNumber) {
        LocalDate weekStart = WeekUtil.getStartOfIsoWeek(year, weekNumber);

        WeekDTO dto = new WeekDTO();
        dto.setYear(year);
        dto.setWeekNumber(weekNumber);
        dto.setStartDate(weekStart);
        dto.setStatus(null);
        dto.setEmployees(employeeService.getAllEmployees());
        dto.setAssignments(List.of());
        dto.setExistingWeek(false);

        return dto;
    }

    /**
     *
     * @param week
     * @param demoSessionId
     * @return
     */
    private WeekDTO buildWeekScheduleDTO(Week week, UUID demoSessionId) {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();

        // The returned WeekDTO has to already have the shifts associated with demoSessionId
        List<Shift> shifts = shiftRepository
                .findByWeekIdAndDemoSessionId(week.getId(), demoSessionId);

        Map<Long, List<Shift>> shiftsByEmployeeId = shifts.stream()
                .collect(Collectors.groupingBy(shift -> shift.getEmployee().getId()));

        List<ShiftAssignmentDTO> assignments = employees.stream()
                .map(employee -> {
                    ShiftAssignmentDTO dto = new ShiftAssignmentDTO();
                    dto.setEmployeeId(employee.getEmployeeId());

                    List<ShiftDTO> employeeShifts = shiftsByEmployeeId
                            .getOrDefault(employee.getEmployeeId(), List.of())
                            .stream()
                            .map(this::mapToShiftAssignmentDTO)
                            .toList();

                    dto.setShifts(employeeShifts);
                    return dto;
                })
                .toList();

        WeekDTO dto = new WeekDTO();
        dto.setYear(week.getYear());
        dto.setWeekNumber(week.getWeekNumber());
        dto.setStartDate(week.getWeekStartDate());
        dto.setStatus(week.getStatus());
        dto.setEmployees(employeeService.getAllEmployees());
        dto.setAssignments(assignments);
        dto.setExistingWeek(true);

        return dto;
    }

    private ShiftDTO mapToShiftAssignmentDTO(Shift shift) {
        ShiftDTO dto = new ShiftDTO();
        dto.setActualDate(shift.getActualDate());
        dto.setStartsAt(shift.getStartsAt());
        dto.setEndsAt(shift.getEndsAt());
        return dto;
    }

    @Transactional
    public void saveWeek(SaveWeekDTO dto, UUID demoSessionId) {

        DemoSession demoSession = demoSessionRepository
                .findById(demoSessionId)
                .orElseThrow(() -> new IllegalStateException(
                        "YOU WROTE THIS!: Demo session not found or invalid"
                ));

        Week week = weekRepository
                .findByYearAndWeekNumberAndDemoSessionId(
                        dto.getYear(),
                        dto.getWeekNumber(),
                        demoSessionId
                )
                .orElseGet(() -> {
                    Week newWeek = new Week();
                    newWeek.setYear(dto.getYear());
                    newWeek.setWeekNumber(dto.getWeekNumber());
                    newWeek.setWeekStartDate(dto.getWeekStartDate());
                    newWeek.setStatus(Week.Status.DRAFT);
                    newWeek.setDemoSession(demoSession);
                    return weekRepository.save(newWeek);
                });

        week.setWeekStartDate(dto.getWeekStartDate());

        for (ShiftAssignmentDTO assignmentDTO : dto.getAssignments()) {
            Employee employee = employeeRepository
                    .findById(assignmentDTO.getEmployeeId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Employee not found: " + assignmentDTO.getEmployeeId()
                    ));

            for (ShiftDTO shiftDTO : assignmentDTO.getShifts()) {
                saveOrUpdateAssignment(week, employee, shiftDTO, demoSession);
            }
        }
    }

    /**
     * If a shift already exists, it updates, else saves a new one
     */
    private void saveOrUpdateAssignment(
            Week week,
            Employee employee,
            ShiftDTO shiftDTO,
            DemoSession demoSession
    ) {
        Optional<Shift> existingShiftOpt = shiftRepository
                .findByWeekAndEmployeeAndActualDateAndDemoSession(
                        week,
                        employee,
                        shiftDTO.getActualDate(),
                        demoSession
                );

        boolean emptyShift = shiftDTO.getStartsAt() == null || shiftDTO.getEndsAt() == null;

        if (emptyShift) {
            existingShiftOpt.ifPresent(shiftRepository::delete);
            return;
        }

        if (existingShiftOpt.isPresent()) {
            Shift existinShift = existingShiftOpt.get();
            existinShift.setStartsAt(shiftDTO.getStartsAt());
            existinShift.setEndsAt(shiftDTO.getEndsAt());
            return;
        }

        Shift newShift = new Shift();
        newShift.setActualDate(shiftDTO.getActualDate());
        newShift.setStartsAt(shiftDTO.getStartsAt());
        newShift.setEndsAt(shiftDTO.getEndsAt());
        newShift.setEmployee(employee);
        newShift.setWeek(week);
        newShift.setDemoSession(demoSession);

        shiftRepository.save(newShift);
    }
}
