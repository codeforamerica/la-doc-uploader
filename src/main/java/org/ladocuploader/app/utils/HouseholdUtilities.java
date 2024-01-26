package org.ladocuploader.app.utils;

import formflow.library.data.Submission;

import java.util.*;

public class HouseholdUtilities {

  public static List<String> unmarriedStatuses = Arrays.asList("Never married", "Legally separated", "Divorced", "Widowed");


  public static boolean doesMemberProvideFinancialSupport(Map<String, Object> hhmember){
    // check if member has a job
    var providesSupport = false;
    try {
      var employerName = hhmember.get("employerName");
      if (employerName != null){
        providesSupport = true;
      }
    } catch (Exception e){
      return false;
    }
    return providesSupport;

  }


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
}
