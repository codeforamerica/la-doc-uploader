package org.ladocuploader.app.utils;

import formflow.library.data.Submission;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

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

  public static ArrayList<LinkedHashMap> formattedHouseholdData(Submission submission, String key) {
    Map<String, Object> inputData = submission.getInputData();

    ArrayList<LinkedHashMap> householdMembers = (ArrayList) inputData.get("household");
    ArrayList<String> hasPersonalSituations = (ArrayList) inputData.get(key);
    ArrayList<LinkedHashMap> householdDataObject = new ArrayList<>();

    hasPersonalSituations.forEach((String id) -> {
          LinkedHashMap user = new LinkedHashMap();
          if (id.equals("you")) {
            user.put("uuid", id);
            user.put("firstName", inputData.get("firstName") + " (you)");
            user.put("firstNamePossessive", inputData.get("firstName") + "'s (your)");

            householdDataObject.add(user);
          } else {
            householdDataObject.add(householdData(householdMembers, id));
          }
        }
    );

    return householdDataObject;

  }

  protected static LinkedHashMap householdData(ArrayList<LinkedHashMap> household, String uuid) {
    LinkedHashMap user = new LinkedHashMap();
    for (LinkedHashMap hhmember : household) {
      if (hhmember.get("uuid").equals(uuid)) {
        user.put("uuid", uuid);
        user.put("firstName", hhmember.get("householdMemberFirstName"));
        user.put("firstNamePossessive", hhmember.get("householdMemberFirstName")+ "'s");
        continue;
      }
    }

    return user;
  }
}
