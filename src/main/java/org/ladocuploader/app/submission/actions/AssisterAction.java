package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AssisterAction implements Action {

  protected static final String DATE_REGEX_PATTERN = "MM/dd/yyyy";
  protected static final String MIN_DATE_ALLOWED = "01/01/1900";
  protected static final String PHONE_REGEX_PATTERN="^\\(\\d{3}\\) \\d{3}-\\d{4}$";
  protected static final String EMAIL_REGEX_PATTERN="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";



  @Autowired
  MessageSource source;
  public String translateMessage(String messageKey) {
    return source.getMessage(messageKey, null, LocaleContextHolder.getLocale());

  }
}
