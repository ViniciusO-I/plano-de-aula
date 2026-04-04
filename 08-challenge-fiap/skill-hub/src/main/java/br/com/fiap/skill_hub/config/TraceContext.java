package br.com.fiap.skill_hub.config;

import org.slf4j.MDC;

public final class TraceContext {

    public static final String TRACE_ID_HEADER = "X-Trace-Id";
    public static final String TRACE_ID_MDC_KEY = "traceId";

    private TraceContext() {
    }

    public static String currentTraceId() {
        return MDC.get(TRACE_ID_MDC_KEY);
    }
}

