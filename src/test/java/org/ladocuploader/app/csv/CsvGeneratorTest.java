package org.ladocuploader.app.csv;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.data.Submission;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.csv.enums.CsvType;

@Slf4j
public class CsvGeneratorTest {

  private static final int NUM_RECORDS = 20;
  private static final int NUM_KIDS = 4;
  private List<String> firstNames = List.of(
      "Ethen", "Ruben", "Julius", "Jace", "Jagger",
      "Stephanie", "Abraham", "Jaidyn", "Erik", "Amari",
      "Cristofer", "Sasha", "Ernesto", "Kassandra", "Jay",
      "Camryn", "Tianna", "Lance", "Hayden", "Ximina");
  private List<String> lastNames = List.of(
      "Stafford", "Mathis", "Valenzuela", "Morse", "Knight",
      "Mcfarland", "Walter", "Miranda", "Gomez", "Lucas",
      "Miles", "Hunt", "Dudley", "Ball", "Jones",
      "Gamble", "Gilmore", "Palmer", "Vega", "Kent");

  private List<String> kidsNames = List.of(
      "Monica", "Gael", "Mangal", "Tommy"
  );

  private final String EXPECTED_PARENTGUARDIAN_CSV_FILENAME = "csvfiles/expectedParentGuardian.csv";
   
  private final String EXPECTED_RELATIONSHIP_CSV_FILENAME = "csvfiles/expectedRelationship.csv";

  private final String EXPECTED_STUDENT_CSV_FILENAME = "csvfiles/expectedStudent.csv";
 
  private CsvGenerator csvGenerator = new CsvGenerator();

  private List<Submission> submissionList = new ArrayList<>();

  @BeforeEach()
  public void setup() {

    for(int i = 0; i < NUM_RECORDS; i++) {
      Map<String, Object> inputData = new HashMap<>();
      inputData.put("firstName", firstNames.get(i));
      inputData.put("lastName", lastNames.get(i));
      inputData.put("emailAddress", firstNames.get(i) + "@mailinator.com");
      inputData.put("phoneNumber", String.format("(555) 555-55%02d",i));
      inputData.put("homeAddressStreetAddress1", String.format("10%02d Main Street", i));
      inputData.put("homeAddressStreetAddress2", String.format("Apartment #1%s", i));
      inputData.put("homeAddressCity", "San Francisco");
      inputData.put("homeAddressState", "CA");
      inputData.put("homeAddressZipCode", String.format("123%02d", i));

      UUID submissionId = UUID.fromString(String.format("9b05db12-d53e-4be6-8d6a-aa8a3b94%04d", i));

      // mimic a 4 person household for each person.
      String fakePersonUUIDPrefix = String.format("2aeefa18-d866-4b45-abe8-f901%04d", i);
      List<Map<String, Object>> householdList = new ArrayList<>();
      for (int h = 0; h < NUM_KIDS; h++) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("uuid", UUID.fromString(String.format("%s00%02d",fakePersonUUIDPrefix, h)));
        entry.put("householdMemberFirstName", kidsNames.get(h));
        entry.put("householdMemberLastName", lastNames.get(i));
        entry.put("householdBirthDay", String.format("%d", h+1));
        entry.put("householdBirthMonth", "11");
        entry.put("householdBirthYear", "2001");
        entry.put("householdRelationship", "child");
        householdList.add(entry);
      }

      // relationship model
      inputData.put("household", householdList);

      Submission submission = Submission.builder()
          .id(submissionId)
          .inputData(inputData)
          .flow("test-flow")
          .build();

      submissionList.add(submission);
    }
  }

  private String getExpectedData(String filePath) throws IOException {
    try(InputStream resource = CsvGeneratorTest.class.getClassLoader().getResourceAsStream(filePath)) {
      if (resource != null) {
        return new String(resource.readAllBytes());
      }
    } catch (Exception e) {
      log.error("unable to open expected csv file '{}' : {}", filePath, e.getMessage());
      throw e;
    }
    return "";
  }

  @Test
  void generateRelationshipCsvData() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
    CsvDocument csv = csvGenerator.generateRelationshipCsvData(submissionList);
    assertThat(csv.getCsvType()).isEqualTo(CsvType.RELATIONSHIP);
    String theData = new String(csv.getCsvData());
    String expectedData = getExpectedData(EXPECTED_RELATIONSHIP_CSV_FILENAME);
    assertThat(theData).isEqualTo(expectedData);
  }

  @Test
  void generateParentGuardianCsvData() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
    CsvDocument csv = csvGenerator.generateParentGuardianCsvData(submissionList);
    assertThat(csv.getCsvType()).isEqualTo(CsvType.PARENT_GUARDIAN);
    String theData = new String(csv.getCsvData());
    String expectedData = getExpectedData(EXPECTED_PARENTGUARDIAN_CSV_FILENAME);
    assertThat(theData).isEqualTo(expectedData);
  }

  @Test
  void generateStudentCsvData() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
    CsvDocument csv = csvGenerator.generateStudentCsvData(submissionList);
    assertThat(csv.getCsvType()).isEqualTo(CsvType.STUDENT);
    String theData = new String(csv.getCsvData());
    String expectedData = getExpectedData(EXPECTED_STUDENT_CSV_FILENAME);
    assertThat(theData).isEqualTo(expectedData);
  }
  @Test
  void generateWicAppCsvData() {}
  @Test
  void generateECEAppCsvData() {}


}
