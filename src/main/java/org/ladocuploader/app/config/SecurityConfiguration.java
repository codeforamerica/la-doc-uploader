package org.ladocuploader.app.config;

import org.ladocuploader.app.filters.StatelessCsrfFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
public class SecurityConfiguration {

  public static final String CSRF_COOKIE = "CSRF-TOKEN";
  public static final String CSRF_FIELD = "csrfField";
  public static final String CSRF_HEADER = "X-CSRF-TOKEN";

  private final StatelessCsrfFilter statelessCsrfFilter = new StatelessCsrfFilter();

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.httpBasic(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .addFilterBefore(statelessCsrfFilter, CsrfFilter.class);

    return http.build();
  }
}
