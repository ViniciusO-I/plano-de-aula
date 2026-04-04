package br.com.fiap.skill_hub.controller.dto;

import br.com.fiap.skill_hub.config.TraceContext;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDto(
        LocalDateTime timestamp,
        int status,
        String code,
        String message,
        String details,
        String traceId
) {
    public ErrorResponseDto(int status, String code, String message) {
        this(LocalDateTime.now(), status, code, message, null, TraceContext.currentTraceId());
    }

    public ErrorResponseDto(int status, String code, String message, String details) {
        this(LocalDateTime.now(), status, code, message, details, TraceContext.currentTraceId());
    }
}

