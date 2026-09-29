package com.kovic.employee_scheduler.service;

import com.kovic.employee_scheduler.dto.*;
import com.kovic.employee_scheduler.model.Employee;
import com.kovic.employee_scheduler.model.Shift;
import com.kovic.employee_scheduler.model.Week;
import com.kovic.employee_scheduler.repository.EmployeeRepository;
import com.kovic.employee_scheduler.repository.ShiftRepository;
import com.kovic.employee_scheduler.repository.WeekRepository;
import com.kovic.employee_scheduler.util.DemoSessionContext;
import com.kovic.employee_scheduler.util.WeekUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ShiftRepository shiftRepository;
    private final EmployeeService employeeService;
    private final WeekRepository weekRepository;
    private final EmployeeRepository employeeRepository;
    private final DemoSessionContext demoSessionContext;

    public ScheduleService(
            ShiftRepository shiftRepository,
            EmployeeService employeeService,
            WeekRepository weekRepository,
            EmployeeRepository employeeRepository,
            DemoSessionContext demoSessionContext
    ) {
        this.shiftRepository = shiftRepository;
        this.employeeService = employeeService;
        this.weekRepository = weekRepository;
        this.employeeRepository = employeeRepository;
        this.demoSessionContext = demoSessionContext;
    }

    @Transactional(readOnly = true)
    public WeekDTO getWeek(int year, int weekNumber) {
        UUID sessionId = demoSessionContext.getRequired();

        return weekRepository.findByDemoSessionIdAndYearAndWeekNumber(
                sessionId, year, weekNumber
                )
                .map(this::buildWeekScheduleDTO)
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

    private WeekDTO buildWeekScheduleDTO(Week week) {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();

        UUID sessionId = demoSessionContext.getRequired();

        List<Shift> shifts = shiftRepository
                .findByWeekIdAndDemoSessionId(week.getId(), sessionId);

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
    public void saveWeek(SaveWeekDTO dto) {
        UUID sessionId = demoSessionContext.getRequired();

        Week week = weekRepository
                .findByDemoSessionIdAndYearAndWeekNumber(
                        sessionId,
                        dto.getYear(),
                        dto.getWeekNumber()
                )
                .orElseGet(() -> {
                    Week newWeek = new Week();
                    newWeek.setYear(dto.getYear());
                    newWeek.setWeekNumber(dto.getWeekNumber());
                    newWeek.setWeekStartDate(dto.getWeekStartDate());
                    newWeek.setStatus(Week.Status.DRAFT);
                    newWeek.setDemoSessionId(sessionId);
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
                saveOrUpdateAssignment(week, employee, shiftDTO, sessionId);
            }
        }
    }

    /**
     * If a shift already exists, it updates, else saves a new one
     */
    private void saveOrUpdateAssignment(
            Week week,
            Employee employee,
            ShiftDTO dto,
            UUID sessionId
    ) {

        Optional<Shift> existingShiftOpt = shiftRepository
                .findByWeekAndEmployeeAndActualDateAndDemoSessionId(
                        week,
                        employee,
                        dto.getActualDate(),
                        sessionId
                );

        boolean emptyShift = dto.getStartsAt() == null || dto.getEndsAt() == null;

        if (emptyShift) {
            existingShiftOpt.ifPresent(shiftRepository::delete);
            return;
        }

        if (existingShiftOpt.isPresent()) {
            Shift existinShift = existingShiftOpt.get();
            existinShift.setStartsAt(dto.getStartsAt());
            existinShift.setEndsAt(dto.getEndsAt());
            return;
        }

        Shift newShift = new Shift();
        newShift.setActualDate(dto.getActualDate());
        newShift.setStartsAt(dto.getStartsAt());
        newShift.setEndsAt(dto.getEndsAt());
        newShift.setEmployee(employee);
        newShift.setWeek(week);
        newShift.setDemoSessionId(sessionId);

        shiftRepository.save(newShift);
    }
}
