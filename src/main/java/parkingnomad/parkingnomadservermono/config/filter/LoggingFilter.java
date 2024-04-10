package parkingnomad.parkingnomadservermono.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String REQUEST_ID = "REQUEST_ID";

    private final LatencyRecorder latencyRecorder;

    public LoggingFilter(final LatencyRecorder latencyRecorder) {
        this.latencyRecorder = latencyRecorder;
    }

    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain
    ) throws IOException, ServletException {
        final String requestId = String.join("", UUID.randomUUID().toString().split("-"));
        final String requestURI = ((HttpServletRequest) request).getRequestURI();
        final String requestMethod = ((HttpServletRequest) request).getMethod();
        MDC.put(REQUEST_ID, requestId);
        LOGGER.info("START PROCESSING REQUEST | [ URI : {}] | METHOD : {} |", requestURI, requestMethod);
        latencyRecorder.start();
        chain.doFilter(request, response);
        final double latency = latencyRecorder.getDurationSeconds();
        LOGGER.info("END PROCESSING REQUEST | [ URI : {}] | [ METHOD : {} ] | [ LATENCY : {} ] |", requestURI, requestMethod, latency);
        MDC.clear();
    }
}
