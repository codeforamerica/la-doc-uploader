package org.ladocuploader.app.config;

import java.io.IOException;
import java.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

@Configuration
@Slf4j
public class LocaleConfiguration implements WebMvcConfigurer {

  @Value("${spring.web.locale: 'en'}")
  private String defaultLocale;
  private static final Map<String, Locale> LOCALE_MAP = new HashMap<>();
  static {
    LOCALE_MAP.put("en", Locale.ENGLISH);
    LOCALE_MAP.put("es", new Locale("es"));
    LOCALE_MAP.put("vi", new Locale("vi"));
  }

  public static class SupportedLocaleAwareLocaleChangeInterceptor extends LocaleChangeInterceptor {

    @Override
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler)
            throws RuntimeException {

      String newLocale = request.getParameter("lang");
      if (newLocale != null && LOCALE_MAP.containsKey(newLocale )) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver == null) {
          throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request");
        }
        localeResolver.setLocale(request, response, StringUtils.parseLocaleString(newLocale));
      } else if (newLocale != null) {
        try {
          response.sendRedirect("/error");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      return true;
    }
  }

  @Bean
  public LocaleResolver localeResolver() {
    CookieLocaleResolver localeResolver = new CookieLocaleResolver();
    localeResolver.setCookieHttpOnly(true);
    localeResolver.setCookieSecure(true);
    localeResolver.setDefaultLocale(LOCALE_MAP.getOrDefault(defaultLocale, Locale.ENGLISH));
    return localeResolver;
  }

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor interceptor = new SupportedLocaleAwareLocaleChangeInterceptor();
//    interceptor.preHandle()
//    interceptor.setIgnoreInvalidLocale(true);
    interceptor.setParamName("lang");
    return interceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
  }
}
