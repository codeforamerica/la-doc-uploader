package org.ladocuploader.app.filters;


import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

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
    HttpSession session = request.getSession(false);
    UUID subId = session != null ? (UUID) session.getAttribute("id") : null;
    String sessionCreatedAt = session != null ?
        new DateTime(session.getCreationTime()).toString("HH:mm:ss.SSS") :
        "no session";
    MDC.put("ip", request.getRemoteAddr());
    MDC.put("method", request.getMethod());
    MDC.put("request", request.getRequestURI());
    MDC.put("sessionId", request.getSession().getId());
    MDC.put("sessionCreatedAt", sessionCreatedAt);
    MDC.put("submissionId", subId == null ? "null" : subId.toString());
    filterChain.doFilter(servletRequest, servletResponse);
    MDC.clear();
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }
}
