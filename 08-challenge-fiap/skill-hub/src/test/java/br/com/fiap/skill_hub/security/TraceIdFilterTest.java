package br.com.fiap.skill_hub.security;

import br.com.fiap.skill_hub.config.TraceContext;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TraceIdFilterTest {

    private final TraceIdFilter traceIdFilter = new TraceIdFilter();

    @Test
    void shouldPreserveIncomingTraceIdInResponse() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(TraceContext.TRACE_ID_HEADER, "trace-123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (req, res) -> { };

        traceIdFilter.doFilter(request, response, chain);

        assertEquals("trace-123", response.getHeader(TraceContext.TRACE_ID_HEADER));
    }

    @Test
    void shouldGenerateTraceIdWhenHeaderIsMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (req, res) -> { };

        traceIdFilter.doFilter(request, response, chain);

        String generatedTraceId = response.getHeader(TraceContext.TRACE_ID_HEADER);
        assertFalse(generatedTraceId == null || generatedTraceId.isBlank());
    }
}

