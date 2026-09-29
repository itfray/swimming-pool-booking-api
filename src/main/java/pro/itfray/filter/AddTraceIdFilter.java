package pro.itfray.filter;

import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** Adds a trace header containing the current trace id to the response. */
@Component
@RequiredArgsConstructor
public class AddTraceIdFilter extends OncePerRequestFilter {

  private static final String X_TRACE_ID_HEADER = "X-Trace-Id";

  private final Tracer tracer;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String traceId = getTraceId();
    if (Objects.nonNull(traceId)) {
      response.setHeader(X_TRACE_ID_HEADER, traceId);
    }

    filterChain.doFilter(request, response);
  }

  private String getTraceId() {
    TraceContext context = this.tracer.currentTraceContext().context();
    return Objects.nonNull(context) ? context.traceId() : null;
  }
}
