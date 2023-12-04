package org.ladocuploader.app.submission.actions;
import formflow.library.config.submission.Action;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.DateTime;

public class VerifyDate implements Action {

  DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy");
  DateTime MIN_DATE = dtf.parseDateTime("01/01/1900");
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
