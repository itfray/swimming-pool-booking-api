package pro.itfray.filter;

import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** Adds a trace header containing the current trace id to the response. */
@Component
@NullMarked
@RequiredArgsConstructor
public class AddTraceIdFilter extends OncePerRequestFilter {

  private static final String X_TRACE_ID_HEADER = "X-Trace-Id";

  private final Tracer tracer;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String traceId = getTraceId();
    if (Objects.nonNull(traceId)) {
      response.setHeader(X_TRACE_ID_HEADER, traceId);
    }

    filterChain.doFilter(request, response);
  }

  private @Nullable String getTraceId() {
    TraceContext context = this.tracer.currentTraceContext().context();
    return Objects.nonNull(context) ? context.traceId() : null;
  }
}
