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
import static org.ladocuploader.app.config.SecurityConfiguration.CSRF_HEADER;

public class StatelessCsrfFilter extends OncePerRequestFilter {
  private static final Set<String> SAFE_METHODS = new HashSet<>(Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));

  private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // if it's a GET type request, then pass through
    if (SAFE_METHODS.contains(request.getMethod())) {
      filterChain.doFilter(request, response);
      return;
    }

    // if it's not a GET type request, make sure that
    // the passed in CSRF_FIELD is equal to either a CSRF_HEADER
    // or CSRF_COOKIE that was passed in.

    // right now general pages pass in a CSRF_TOKEN.  The XHR requests
    // (like file upload page) will pass in an CSRF_HEADER.

    String csrfHeader = request.getHeader(CSRF_HEADER);
    String csrfFieldToken = (String) request.getParameter(CSRF_FIELD);
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

    // if we don't have the field parameter, we're done here
    if (csrfFieldToken != null) {
      // we should have one of these available
      if (csrfHeader != null || csrfCookieToken != null) {
        // one of these two things should equal the csrfFieldToken
        if((csrfCookieToken != null && csrfCookieToken.equals(csrfFieldToken)) ||
             (csrfHeader != null && csrfHeader.equals(csrfFieldToken))) {
          filterChain.doFilter(request, response);
          return;
        }
      }
    }

    // Tokens don't match!
    accessDeniedHandler.handle(request, response, new AccessDeniedException("CSRF tokens missing or not matching"));
  }
}
