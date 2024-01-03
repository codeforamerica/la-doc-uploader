package org.ladocuploader.app.utils;

import formflow.library.data.Submission;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

  public static List<Map<String, Object>> formattedHouseholdData(Submission submission, String key) {
    Map<String, Object> inputData = submission.getInputData();

    List<Map<String, Object>> householdMembers = (List) inputData.get("household");
    List<String> hasPersonalSituations = (List) inputData.get(key);
    List<Map<String, Object>> householdDataObject = new ArrayList<>();

    hasPersonalSituations.forEach((String id) -> {
          Map<String, Object> user = new LinkedHashMap<>();
          if (id.equals("you")) {
            user.put("uuid", id);
            user.put("firstName", inputData.get("firstName"));

            householdDataObject.add(user);
          } else {
            householdDataObject.add(householdData(householdMembers, id));
          }
        }
    );

    return householdDataObject;

  }

  private static Map<String, Object> householdData(List<Map<String, Object>> household, String uuid) {
    Map<String, Object> user = new LinkedHashMap<>();
    for (Map<String, Object> hhmember : household) {
      if (hhmember.get("uuid").equals(uuid)) {
        user.put("uuid", uuid);
        user.put("firstName", hhmember.get("householdMemberFirstName"));
        continue;
      }
    }

    return user;
  }

  /**
   * Gathers the UUID's from each map in the list passed in.
   * We pass in something called "household", but this will work on any list with Maps that have
   * "uuid" in it.
   *
   * @param household List of Maps containing "uuid" entries
   * @return List of UUID's pulled from the maps
   */
  public static List<String> getHouseholdUuids(List<Map<String, Object>> household) {
    List<String> uuids = new ArrayList<>();

    if (household == null || household.isEmpty()) {
      return uuids;
    }

    for(Map<String, Object> member : household) {
      uuids.add((String)member.get("uuid"));
    }

    return uuids;
  }
}
