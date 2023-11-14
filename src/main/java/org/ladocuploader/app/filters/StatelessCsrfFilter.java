package org.ladocuploader.app.filters;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.ladocuploader.app.config.SecurityConfiguration.CSRF_COOKIE;
import static org.ladocuploader.app.config.SecurityConfiguration.CSRF_FIELD;

public class StatelessCsrfFilter extends OncePerRequestFilter {
  private static final Set<String> SAFE_METHODS = new HashSet<>(Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));

  private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (!SAFE_METHODS.contains(request.getMethod())) {
      String csrfFieldToken = (String)request.getAttribute(CSRF_FIELD);
      String csrfCookieToken = null;
      Cookie[] cookies = request.getCookies();
      if (cookies != null) {
        Optional<Cookie> csrfCookie = Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals(CSRF_COOKIE))
            .findFirst();
        if (csrfCookie.isPresent()) {
          csrfCookieToken = csrfCookie.get().getValue();
        }
      }
      if (csrfFieldToken == null || csrfCookieToken == null || !csrfCookieToken.equals(csrfFieldToken)) {
        accessDeniedHandler.handle(request, response, new AccessDeniedException("CSRF tokens missing or not matching"));
        return;
      }
    }
    filterChain.doFilter(request, response);
  }
}
