package org.ladocuploader.app.filters;


import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.joda.time.DateTime;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class LoggingFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws ServletException, IOException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    MDC.put("sessionId", request.getSession().getId());
    MDC.put("sessionCreatedAt", new DateTime(request.getSession().getCreationTime()).toString("HH:mm:ss.SSS"));
    MDC.put("method", request.getMethod());
    MDC.put("request", request.getRequestURI());
    filterChain.doFilter(servletRequest, servletResponse);
    MDC.clear();
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }
}
