package com.kovic.employee_scheduler.util;

import com.kovic.employee_scheduler.repository.DemoSessionRepository;
import com.kovic.employee_scheduler.repository.ShiftRepository;
import com.kovic.employee_scheduler.repository.TaskAssignmentRepository;
import com.kovic.employee_scheduler.repository.WeekRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * I'm still unsure whether to make this as a Service or Util.
 * But its purpose is to delete expired sessions
 */
@Component
@RequiredArgsConstructor
public class DemoSessionUtil {

    private final TaskAssignmentRepository taskAssignmentRepository;
    private final ShiftRepository shiftRepository;
    private final WeekRepository weekRepository;
    private final DemoSessionRepository demoSessionRepository;

    /**
     * flush() does not commit the transaction. The changes can still be rolled back
     * if the transaction fails. Normally, JPA flushes automatically at transaction commit,
     * so explicit flush() is mainly useful when operation ordering matters,
     * such as delete-then-insert with a unique constraint.
     */
    @Transactional
    public void deleteExpiredSession(UUID demoSessionId) {
        taskAssignmentRepository.deleteByDemoSessionId(demoSessionId);
        shiftRepository.deleteByDemoSessionId(demoSessionId);
        weekRepository.deleteByDemoSessionId(demoSessionId);

        taskAssignmentRepository.flush();
        shiftRepository.flush();
        weekRepository.flush();

        demoSessionRepository.deleteById(demoSessionId);
        demoSessionRepository.flush();
    }

}
