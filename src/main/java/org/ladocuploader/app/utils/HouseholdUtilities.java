package org.ladocuploader.app.utils;

import java.text.NumberFormat;
import java.util.Calendar;

public class HouseholdUtilities {

  public static boolean isMember18orOlder(int day, int month, int year) throws NumberFormatException {

    if (day <= 0 || month <= 0 || year <= 0 ) {
      throw new NumberFormatException("cannot analyze birthdate as fields are missing");
    }

    Calendar memberBirthDayCal = Calendar.getInstance();
    memberBirthDayCal.set(Calendar.YEAR, year);
    memberBirthDayCal.set(Calendar.MONTH, month);
    memberBirthDayCal.set(Calendar.DAY_OF_MONTH, day);

    Calendar cal = Calendar.getInstance();
    cal.set(year, month, day);
    cal.add(Calendar.YEAR, -18);

    // these are converted to milliseconds since Epoch and then compared.
    // if the memberBirthDayCal is < or == the cal, then they are 18+ years old.
    return memberBirthDayCal.compareTo(cal) <= 0;
  }
}