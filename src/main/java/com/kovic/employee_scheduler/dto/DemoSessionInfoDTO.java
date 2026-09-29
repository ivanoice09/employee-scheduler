package com.kovic.employee_scheduler.dto;

import java.time.OffsetDateTime;

public class DemoSessionInfoDTO {
    private OffsetDateTime expiresAt;
    private long remainingSeconds;

    public DemoSessionInfoDTO() {}

    public DemoSessionInfoDTO(OffsetDateTime expiresAt, long remainingSeconds) {
        this.expiresAt = expiresAt;
        this.remainingSeconds = remainingSeconds;
    }

    public OffsetDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(OffsetDateTime expiresAt) { this.expiresAt = expiresAt; }

    public long getRemainingSeconds() { return remainingSeconds; }
    public void setRemainingSeconds(long remainingSeconds) { this.remainingSeconds = remainingSeconds; }
}