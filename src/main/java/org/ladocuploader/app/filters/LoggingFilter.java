package org.ladocuploader.app.filters;


import formflow.library.FormFlowController;
import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.joda.time.DateTime;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;
import org.springframework.util.AntPathMatcher;

@Component
public class LoggingFilter implements Filter {

  public static final String PATH_FORMAT = "/flow/{flow}/{screen}";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws ServletException, IOException {

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpSession session = request.getSession(false);
    Map<String, UUID> submissionMap =
        session != null ? (Map) session.getAttribute(FormFlowController.SUBMISSION_MAP_NAME) : null;
    UUID subId = null;
    Map<String, String> parsedUrl = null;
    try {
      parsedUrl = new AntPathMatcher().extractUriTemplateVariables(PATH_FORMAT, request.getRequestURI());

      if (submissionMap != null && submissionMap.get(parsedUrl.get("flow")) != null) {
        subId = submissionMap.get(parsedUrl.get("flow"));
      }
    } catch (IllegalStateException e)  {
      // do nothing - it means the URL didn't match the /flow/{flow}/{screen} format
    }

    String sessionCreatedAt = session != null ?
        new DateTime(session.getCreationTime()).toString("HH:mm:ss.SSS") : "no session";

    MDC.put("xForwardedFor", request.getHeader("X-Forwarded-For"));
    MDC.put("method", request.getMethod());
    MDC.put("request", request.getRequestURI());
    MDC.put("sessionId", session == null ? "null" : session.getId());
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
