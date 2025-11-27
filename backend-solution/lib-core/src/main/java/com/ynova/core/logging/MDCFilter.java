package com.ynova.core.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MDCFilter implements Filter {

    private static final String TRACE_ID = "traceId";
    private static final String SPAN_ID = "spanId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            String traceId = UUID.randomUUID().toString();
            String spanId = UUID.randomUUID().toString();

            MDC.put(TRACE_ID, traceId);
            MDC.put(SPAN_ID, spanId);

            if (response instanceof HttpServletResponse httpServletResponse) {
                httpServletResponse.setHeader("X-Trace-Id", traceId);
            }

            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
