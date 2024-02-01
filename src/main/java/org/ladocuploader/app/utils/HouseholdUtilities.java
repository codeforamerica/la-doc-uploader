package org.ladocuploader.app.utils;

import formflow.library.data.Submission;

import java.time.LocalDate;
import java.util.*;

public class HouseholdUtilities {

  public static List<String> unmarriedStatuses = Arrays.asList("NeverMarried", "LegallySeparated", "Divorced", "Widowed");

  public static Calendar ECE_CUTOFF_DATE = new Calendar.Builder().setDate(2019, 9, 30).build();
  /**
   * Checks the birthdate against the current calendar date (now) to see if the birthdate is 18 years or older.
   * @param day day of Birth
   * @param month month of Birth
   * @param year year of Birth
   * @return boolean - true if member is 18 or older, otherwise false
   * @throws NumberFormatException throws this if the day, month or year passed in are invalid
   */
  public static boolean isMember18orOlder(int year, int month, int day) throws NumberFormatException {
    return isMember18orOlder(year, month, day, Calendar.getInstance());
  }
  /**
   * Checks the birthdate against the calendar date passed in to see if the birthdate is 18 years old or older.
   *
   * @param day day of Birth
   * @param month month of Birth
   * @param year year of Birth
   * @param calendar calendar to compare the birthdate to
   * @return boolean - true if member is 18 or older, otherwise false
   * @throws NumberFormatException throws this if the day, month or year passed in are invalid
   */
  public static boolean isMember18orOlder(int year, int month, int day, Calendar calendar) throws NumberFormatException {

    if (day <= 0 || month <= 0 || year <= 0 ) {
      throw new NumberFormatException("cannot analyze birthdate as fields are missing");
    }

    LocalDate currentDate = LocalDate.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());
    LocalDate memberBirthDate = LocalDate.of(year, month, day);
    LocalDate eighteenthBirthday = memberBirthDate.plusYears(18);

    return eighteenthBirthday.isBefore(currentDate) || eighteenthBirthday.isEqual(currentDate);
  }

  /**
   * Checks to see if the member is eligible for ECE, based on if the member is pregnant or if they are young enough.
   *
   * @param member household member data
   * @param inputData full input data for the submission
   * @return boolean - true if member is eligible, false if not
   * @throws NumberFormatException throws this if the day, month or year passed in are invalid
   */
  public static boolean isMemberEceEligible(Map<String, Object> member, Map<String, Object> inputData) throws NumberFormatException {
    boolean isPregnant = ((List)inputData.getOrDefault("pregancies[]", List.of())).contains(member.get("uuid"));

    if (isPregnant) {
      return true;
    }

    int birthDay = Integer.parseInt((String) member.get("householdMemberBirthDay"));
    int birthMonth = Integer.parseInt((String) member.get("householdMemberBirthMonth"));
    int birthYear = Integer.parseInt((String) member.get("householdMemberBirthYear"));

    if (birthDay <= 0 || birthMonth <= 0 || birthYear <= 0 ) {
      throw new NumberFormatException("cannot analyze birthdate as fields are missing");
    }

    Calendar memberBirthDayCal = new Calendar.Builder().setDate(birthYear, birthMonth, birthDay).build();

    // these are converted to milliseconds since Epoch and then compared.
    // if the memberBirthDayCal is > or == the cal, then they are 5 years old or younger.
    return memberBirthDayCal.compareTo(ECE_CUTOFF_DATE) >= 0;
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