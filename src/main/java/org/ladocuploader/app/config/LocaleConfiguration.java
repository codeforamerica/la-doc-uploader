package org.ladocuploader.app.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class LocaleConfiguration implements WebMvcConfigurer {

  @Value("${spring.web.locale: 'en'}")
  private String defaultLocale;
  private static final Map<String, Locale> LOCALE_MAP = new HashMap<>();
  static {
    LOCALE_MAP.put("en", Locale.ENGLISH);
    LOCALE_MAP.put("es", new Locale("es"));
    LOCALE_MAP.put("vi", new Locale("vi"));
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
    LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName("lang");
    return interceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
  }
}
