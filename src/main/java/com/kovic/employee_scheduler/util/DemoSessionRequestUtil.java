package com.kovic.employee_scheduler.util;

import com.kovic.employee_scheduler.filter.DemoSessionFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

public final class DemoSessionRequestUtil {

    private DemoSessionRequestUtil() {}

    public static UUID getDemoSessionId(HttpServletRequest request) {
        Object attr = request.getAttribute(DemoSessionFilter.DEMO_SESSION_ID_ATTR);
        if (attr instanceof UUID uuid) {
            return uuid;
        }
        throw new IllegalStateException("Demo session ID not present in request");
    }
}
