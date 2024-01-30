package org.ladocuploader.app.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class HouseholdUtilitiesTest {
  
  // Because we manually set the date here, and Calendar uses 0-based months, this is January 10, 2024
  // Note that in the actual code, this is set to Calendar.getInstance() and LocalDate handles the conversion to
  // non 0 based months
  private Calendar pretendCurrentDate = new Calendar.Builder().setDate(2024, 0, 10).build();

  @ParameterizedTest
  @CsvSource({
    // year, month, day, expected response
    "2006, 1, 10, true",  // 18th birthday
    "2006, 1, 11, false", // one day too young! hmmm.... weird boundary case
    "2005, 1, 10, true",  // older than 18
    "2019, 1, 10, false", // def not 18+
    "1999, 1, 10, true",  // adult
  })
  void test18orOlderDates(int year, int month, int day, boolean expectedResponse) {
    assertThat(HouseholdUtilities.isMember18orOlder(year, month, day, pretendCurrentDate)).isEqualTo(expectedResponse);
  }

  @ParameterizedTest
  @CsvSource({
      // year, month, day, pregnant, expected response
      "2019, 9, 30, false, true",  // 5th birthday
      "2019, 9, 29, false, false", // just outside of range
      "2024, 10, 1, false, true",  // under 5
      "2005, 10, 1, false, false",  // way over 5
      "2005, 10, 1, true, false"  // way over 5 but pregnant
  })
  void testEceEligibility(String year, String month, String day, boolean pregnant, boolean expectedResponse) {
    UUID uuid = UUID.randomUUID();
    Map<String, Object> inputData = new HashMap<>();
    Map<String, Object> member = Map.of(
        "uuid", uuid,
        "householdMemberBirthYear", year,
        "householdMemberBirthMonth", month,
        "householdMemberBirthDay", day);
    if (pregnant) {
        inputData.put("pregnancies[]", List.of(uuid.toString()));
    }

    assertThat(HouseholdUtilities.isMemberEceEligible(member, inputData)).isEqualTo(expectedResponse);
  }
}
