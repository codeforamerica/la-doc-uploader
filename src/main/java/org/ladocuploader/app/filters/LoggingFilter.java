package org.ladocuploader.app.filters;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
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
    MDC.put("sessionCreatedAt", String.valueOf(request.getSession().getCreationTime()));
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
