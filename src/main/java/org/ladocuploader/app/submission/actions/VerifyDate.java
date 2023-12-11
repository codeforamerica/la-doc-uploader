package org.ladocuploader.app.submission.actions;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.DateTime;

public class VerifyDate extends AssisterAction {

  DateTimeFormatter dtf = DateTimeFormat.forPattern(DATE_REGEX_PATTERN);
  DateTime MIN_DATE = dtf.parseDateTime(MIN_DATE_ALLOWED);

  protected boolean validRange(String date){
    try {
      DateTime birthdate = dtf.parseDateTime(date);
      return validRange(birthdate);
    } catch (Exception e) {
      return false;
    }
  }

  private boolean validRange(DateTime inputDate) {
    return inputDate.isBefore(DateTime.now()) && inputDate.isAfter(MIN_DATE);
  }
}
