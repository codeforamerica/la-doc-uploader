package org.ladocuploader.app.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class HouseholdUtilitiesTest {

  private Calendar pretendCurrentDate = new Calendar.Builder().setDate(2024, 1, 10).build();

  @Test
  @CsvSource({
    // month, day year
    "1, 10, 2006, true", // equals current date
    "1, 11, 2006, false", // one day too young! hmmm.... weird boundary case
    "1, 10, 2005, true", // older than 18
    "1, 10, 2019, false", // def not 18+
    "1, 10, 1999, true", // adult
  })
  @ParameterizedTest()
  void test18OrOlderDates(int month, int day, int year, boolean expectedResponse) {
    assertThat(HouseholdUtilities.isMember18orOlder(day, month, year, pretendCurrentDate)).isEqualTo(expectedResponse);
  }
}
