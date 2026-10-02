package com.kovic.employee_scheduler.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class DemoSessionInfoDTO {
    private OffsetDateTime expiresAt;
    private long remainingSeconds;

    public DemoSessionInfoDTO() {}

    public DemoSessionInfoDTO(OffsetDateTime expiresAt, long remainingSeconds) {
        this.expiresAt = expiresAt;
        this.remainingSeconds = remainingSeconds;
    }

}