package org.ladocuploader.app.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.session.SimpleRedirectInvalidSessionStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Configuration
@ComponentScan
@Slf4j
public class SecurityConfiguration {

  @Autowired
  private SessionStrategy sessionStrategy;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.httpBasic().disable();

    http.sessionManagement(session -> session.invalidSessionStrategy(this.sessionStrategy));
    http.sessionManagement(session -> session.sessionConcurrency(concurrency -> concurrency.expiredSessionStrategy(this.sessionStrategy)));

    return http.build();
  }

  @Component
  public static class SessionStrategy implements InvalidSessionStrategy, SessionInformationExpiredStrategy {
    final private SimpleRedirectInvalidSessionStrategy errorRedirectInvalidSessionStrategy;

    public SessionStrategy(@Value("${server.servlet.session.timeout-url}") String timeoutUrl) {
      errorRedirectInvalidSessionStrategy = new SimpleRedirectInvalidSessionStrategy(timeoutUrl);
    }

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
      log.info("User session timed out on page: " + request.getRequestURL());
      errorRedirectInvalidSessionStrategy.onInvalidSessionDetected(request, response);
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
      log.info("User session timed out on page: " + event.getRequest().getRequestURL());
      errorRedirectInvalidSessionStrategy.onInvalidSessionDetected(event.getRequest(), event.getResponse());
    }
  }
}
