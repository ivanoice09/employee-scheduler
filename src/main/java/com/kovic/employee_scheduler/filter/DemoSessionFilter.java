package com.kovic.employee_scheduler.filter;

import com.kovic.employee_scheduler.helper.DemoSessionHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // can be on a specific order but for now this will do as I don't have any other existing filters
@RequiredArgsConstructor
public class DemoSessionFilter extends OncePerRequestFilter {

    private final DemoSessionHelper demoSessionHelper;

    public static final String DEMO_SESSION_ID_ATTR = "DEMO_SESSION_ID";


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        Optional<UUID> demoSessionId = demoSessionHelper.readOrCreateDemoSessionRow(request, response);

        demoSessionId.ifPresent(id -> request.setAttribute(DEMO_SESSION_ID_ATTR, id));

        filterChain.doFilter(request, response);
    }
}
