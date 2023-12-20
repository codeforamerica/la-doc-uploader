package org.ladocuploader.app.filters;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.utils.CsrfHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Component;
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

@Slf4j
@Component()
public class StatelessCsrfFilter extends OncePerRequestFilter {
  private static final Set<String> SAFE_METHODS = new HashSet<>(Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));

  private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

  @Value("${form-flow.csrf-key}")
  String csrfKey;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // if it's a GET type request, then pass through
    if (SAFE_METHODS.contains(request.getMethod())) {
      filterChain.doFilter(request, response);
      return;
    }

    // grab the csrf token from the field.
    String csrfFieldToken = (String) request.getParameter(CSRF_FIELD);

    if (csrfFieldToken != null && !csrfFieldToken.isBlank()) {

      // decode the token
      int bang = csrfFieldToken.lastIndexOf("!");
      int period = csrfFieldToken.lastIndexOf(".");
      if (bang == -1 || period == -1) {
        accessDeniedHandler.handle(request, response,
            new AccessDeniedException("Invalid CSRF token. It is not formatted correctly: " + csrfFieldToken));
        return;
      }

      String submissionId = csrfFieldToken.substring(period + 1, bang);
      String randomValue = csrfFieldToken.substring(bang + 1);
      UUID submissionUUID;

      try {
        submissionUUID = UUID.fromString(submissionId);
      } catch (IllegalArgumentException e) {
        log.error("Invalid syntax for UUID: {}", submissionId);
        accessDeniedHandler.handle(request, response,
            new AccessDeniedException("Invalid CSRF token. Submission id is not a UUID: " + submissionId));
        return;
      }

      CsrfHelper csrfHelper = new CsrfHelper();
      // first make sure the token itself is a legit token.
      String hmac = csrfHelper.generateCsrf(submissionUUID, randomValue, csrfKey);
      if (hmac != null && hmac.equals(csrfFieldToken)) {

        // the token is legit, now make sure the passed in CSRF_FIELD is
        // equal to either a CSRF_HEADER or CSRF_COOKIE that was passed in.

        // right now general pages pass in a CSRF_TOKEN.  The XHR requests
        // (like file upload page) will pass in an CSRF_HEADER.

        String csrfHeader = request.getHeader(CSRF_HEADER);
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

        // we should have one of these available
        if (csrfHeader != null || csrfCookieToken != null) {
          // one of these two things should equal the csrfFieldToken
          if ((csrfCookieToken != null && csrfCookieToken.equals(csrfFieldToken)) ||
              (csrfHeader != null && csrfHeader.equals(csrfFieldToken))) {
            filterChain.doFilter(request, response);
            return;
          }
        }
      } else {
        log.error("CsrfFieldToken is not a legitimate token! ('{}')", csrfFieldToken);
      }
    }

    // Something's wrong with the tokens, or they don't match
    accessDeniedHandler.handle(request, response, new AccessDeniedException("CSRF tokens missing or not matching"));
  }
}
