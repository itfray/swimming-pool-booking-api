package pro.itfray.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.micrometer.tracing.CurrentTraceContext;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class AddTraceIdFilterTest {

  private static final String X_TRACE_ID_HEADER = "X-Trace-Id";

  private static final String TRACE_ID = "trace-123";

  @Test
  @DisplayName("Should add trace id header when current trace context contains a trace")
  void shouldAddTraceIdHeaderWhenTraceExists() throws ServletException, IOException {
    Tracer tracer = mock(Tracer.class);
    CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
    TraceContext traceContext = mock(TraceContext.class);

    when(tracer.currentTraceContext()).thenReturn(currentTraceContext);
    when(currentTraceContext.context()).thenReturn(traceContext);
    when(traceContext.traceId()).thenReturn(TRACE_ID);

    AddTraceIdFilter filter = new AddTraceIdFilter(tracer);
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    FilterChain filterChain = mock(FilterChain.class);

    filter.doFilter(request, response, filterChain);

    assertThat(response.getHeader(X_TRACE_ID_HEADER)).isEqualTo(TRACE_ID);
    verify(filterChain).doFilter(request, response);
  }

  @Test
  @DisplayName("Should not add trace id header when trace id is null")
  void shouldNotAddTraceIdHeaderWhenTraceIdIsNull() throws ServletException, IOException {
    Tracer tracer = mock(Tracer.class);
    CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);
    TraceContext traceContext = mock(TraceContext.class);

    when(tracer.currentTraceContext()).thenReturn(currentTraceContext);
    when(currentTraceContext.context()).thenReturn(traceContext);
    when(traceContext.traceId()).thenReturn(null);

    AddTraceIdFilter filter = new AddTraceIdFilter(tracer);
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    FilterChain filterChain = mock(FilterChain.class);

    filter.doFilter(request, response, filterChain);

    assertThat(response.getHeader(X_TRACE_ID_HEADER)).isNull();
    verify(filterChain).doFilter(request, response);
  }

  @Test
  @DisplayName("Should not add trace id header when current trace context is null")
  void shouldNotAddTraceIdHeaderWhenCurrentTraceContextIsNull()
      throws ServletException, IOException {
    Tracer tracer = mock(Tracer.class);
    CurrentTraceContext currentTraceContext = mock(CurrentTraceContext.class);

    when(tracer.currentTraceContext()).thenReturn(currentTraceContext);
    when(currentTraceContext.context()).thenReturn(null);

    AddTraceIdFilter filter = new AddTraceIdFilter(tracer);
    HttpServletRequest request = new MockHttpServletRequest();
    HttpServletResponse response = new MockHttpServletResponse();
    FilterChain filterChain = mock(FilterChain.class);

    filter.doFilter(request, response, filterChain);

    assertThat(response.getHeader(X_TRACE_ID_HEADER)).isNull();
    verify(filterChain).doFilter(request, response);
  }
}
