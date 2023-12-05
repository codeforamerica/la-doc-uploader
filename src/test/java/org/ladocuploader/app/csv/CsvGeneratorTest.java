package org.ladocuploader.app.csv;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import formflow.library.data.Submission;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.csv.CsvService.CsvType;

public class CsvGeneratorTest {

  private static int NUM_RECORDS = 20;
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


  private String expectedParentGuardianCsvData = """
    "active","city","email_address","first_name","last_name","phone_number","reference_id","state","street_address","zip_code"
    "true","San Francisco","Ethen@mailinator.com","Ethen","Stafford","(555) 555-5500","9b05db12-d53e-4be6-8d6a-aa8a3b940000","CA","1000 Main Street Apartment #10","12300"
    "true","San Francisco","Ruben@mailinator.com","Ruben","Mathis","(555) 555-5501","9b05db12-d53e-4be6-8d6a-aa8a3b940001","CA","1001 Main Street Apartment #11","12301"
    "true","San Francisco","Julius@mailinator.com","Julius","Valenzuela","(555) 555-5502","9b05db12-d53e-4be6-8d6a-aa8a3b940002","CA","1002 Main Street Apartment #12","12302"
    "true","San Francisco","Jace@mailinator.com","Jace","Morse","(555) 555-5503","9b05db12-d53e-4be6-8d6a-aa8a3b940003","CA","1003 Main Street Apartment #13","12303"
    "true","San Francisco","Jagger@mailinator.com","Jagger","Knight","(555) 555-5504","9b05db12-d53e-4be6-8d6a-aa8a3b940004","CA","1004 Main Street Apartment #14","12304"
    "true","San Francisco","Stephanie@mailinator.com","Stephanie","Mcfarland","(555) 555-5505","9b05db12-d53e-4be6-8d6a-aa8a3b940005","CA","1005 Main Street Apartment #15","12305"
    "true","San Francisco","Abraham@mailinator.com","Abraham","Walter","(555) 555-5506","9b05db12-d53e-4be6-8d6a-aa8a3b940006","CA","1006 Main Street Apartment #16","12306"
    "true","San Francisco","Jaidyn@mailinator.com","Jaidyn","Miranda","(555) 555-5507","9b05db12-d53e-4be6-8d6a-aa8a3b940007","CA","1007 Main Street Apartment #17","12307"
    "true","San Francisco","Erik@mailinator.com","Erik","Gomez","(555) 555-5508","9b05db12-d53e-4be6-8d6a-aa8a3b940008","CA","1008 Main Street Apartment #18","12308"
    "true","San Francisco","Amari@mailinator.com","Amari","Lucas","(555) 555-5509","9b05db12-d53e-4be6-8d6a-aa8a3b940009","CA","1009 Main Street Apartment #19","12309"
    "true","San Francisco","Cristofer@mailinator.com","Cristofer","Miles","(555) 555-5510","9b05db12-d53e-4be6-8d6a-aa8a3b940010","CA","1010 Main Street Apartment #110","12310"
    "true","San Francisco","Sasha@mailinator.com","Sasha","Hunt","(555) 555-5511","9b05db12-d53e-4be6-8d6a-aa8a3b940011","CA","1011 Main Street Apartment #111","12311"
    "true","San Francisco","Ernesto@mailinator.com","Ernesto","Dudley","(555) 555-5512","9b05db12-d53e-4be6-8d6a-aa8a3b940012","CA","1012 Main Street Apartment #112","12312"
    "true","San Francisco","Kassandra@mailinator.com","Kassandra","Ball","(555) 555-5513","9b05db12-d53e-4be6-8d6a-aa8a3b940013","CA","1013 Main Street Apartment #113","12313"
    "true","San Francisco","Jay@mailinator.com","Jay","Jones","(555) 555-5514","9b05db12-d53e-4be6-8d6a-aa8a3b940014","CA","1014 Main Street Apartment #114","12314"
    "true","San Francisco","Camryn@mailinator.com","Camryn","Gamble","(555) 555-5515","9b05db12-d53e-4be6-8d6a-aa8a3b940015","CA","1015 Main Street Apartment #115","12315"
    "true","San Francisco","Tianna@mailinator.com","Tianna","Gilmore","(555) 555-5516","9b05db12-d53e-4be6-8d6a-aa8a3b940016","CA","1016 Main Street Apartment #116","12316"
    "true","San Francisco","Lance@mailinator.com","Lance","Palmer","(555) 555-5517","9b05db12-d53e-4be6-8d6a-aa8a3b940017","CA","1017 Main Street Apartment #117","12317"
    "true","San Francisco","Hayden@mailinator.com","Hayden","Vega","(555) 555-5518","9b05db12-d53e-4be6-8d6a-aa8a3b940018","CA","1018 Main Street Apartment #118","12318"
    "true","San Francisco","Ximina@mailinator.com","Ximina","Kent","(555) 555-5519","9b05db12-d53e-4be6-8d6a-aa8a3b940019","CA","1019 Main Street Apartment #119","12319"
    """;
  private String expectedRelationshipCsvData = """
    "first_person","second_person"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940000","2aeefa18-d866-4b45-abe8-f90100000000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940000","2aeefa18-d866-4b45-abe8-f90100000001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940000","2aeefa18-d866-4b45-abe8-f90100000002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940000","2aeefa18-d866-4b45-abe8-f90100000003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940001","2aeefa18-d866-4b45-abe8-f90100010000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940001","2aeefa18-d866-4b45-abe8-f90100010001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940001","2aeefa18-d866-4b45-abe8-f90100010002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940001","2aeefa18-d866-4b45-abe8-f90100010003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940002","2aeefa18-d866-4b45-abe8-f90100020000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940002","2aeefa18-d866-4b45-abe8-f90100020001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940002","2aeefa18-d866-4b45-abe8-f90100020002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940002","2aeefa18-d866-4b45-abe8-f90100020003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940003","2aeefa18-d866-4b45-abe8-f90100030000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940003","2aeefa18-d866-4b45-abe8-f90100030001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940003","2aeefa18-d866-4b45-abe8-f90100030002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940003","2aeefa18-d866-4b45-abe8-f90100030003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940004","2aeefa18-d866-4b45-abe8-f90100040000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940004","2aeefa18-d866-4b45-abe8-f90100040001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940004","2aeefa18-d866-4b45-abe8-f90100040002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940004","2aeefa18-d866-4b45-abe8-f90100040003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940005","2aeefa18-d866-4b45-abe8-f90100050000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940005","2aeefa18-d866-4b45-abe8-f90100050001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940005","2aeefa18-d866-4b45-abe8-f90100050002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940005","2aeefa18-d866-4b45-abe8-f90100050003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940006","2aeefa18-d866-4b45-abe8-f90100060000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940006","2aeefa18-d866-4b45-abe8-f90100060001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940006","2aeefa18-d866-4b45-abe8-f90100060002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940006","2aeefa18-d866-4b45-abe8-f90100060003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940007","2aeefa18-d866-4b45-abe8-f90100070000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940007","2aeefa18-d866-4b45-abe8-f90100070001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940007","2aeefa18-d866-4b45-abe8-f90100070002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940007","2aeefa18-d866-4b45-abe8-f90100070003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940008","2aeefa18-d866-4b45-abe8-f90100080000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940008","2aeefa18-d866-4b45-abe8-f90100080001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940008","2aeefa18-d866-4b45-abe8-f90100080002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940008","2aeefa18-d866-4b45-abe8-f90100080003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940009","2aeefa18-d866-4b45-abe8-f90100090000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940009","2aeefa18-d866-4b45-abe8-f90100090001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940009","2aeefa18-d866-4b45-abe8-f90100090002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940009","2aeefa18-d866-4b45-abe8-f90100090003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940010","2aeefa18-d866-4b45-abe8-f90100100000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940010","2aeefa18-d866-4b45-abe8-f90100100001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940010","2aeefa18-d866-4b45-abe8-f90100100002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940010","2aeefa18-d866-4b45-abe8-f90100100003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940011","2aeefa18-d866-4b45-abe8-f90100110000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940011","2aeefa18-d866-4b45-abe8-f90100110001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940011","2aeefa18-d866-4b45-abe8-f90100110002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940011","2aeefa18-d866-4b45-abe8-f90100110003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940012","2aeefa18-d866-4b45-abe8-f90100120000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940012","2aeefa18-d866-4b45-abe8-f90100120001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940012","2aeefa18-d866-4b45-abe8-f90100120002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940012","2aeefa18-d866-4b45-abe8-f90100120003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940013","2aeefa18-d866-4b45-abe8-f90100130000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940013","2aeefa18-d866-4b45-abe8-f90100130001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940013","2aeefa18-d866-4b45-abe8-f90100130002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940013","2aeefa18-d866-4b45-abe8-f90100130003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940014","2aeefa18-d866-4b45-abe8-f90100140000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940014","2aeefa18-d866-4b45-abe8-f90100140001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940014","2aeefa18-d866-4b45-abe8-f90100140002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940014","2aeefa18-d866-4b45-abe8-f90100140003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940015","2aeefa18-d866-4b45-abe8-f90100150000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940015","2aeefa18-d866-4b45-abe8-f90100150001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940015","2aeefa18-d866-4b45-abe8-f90100150002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940015","2aeefa18-d866-4b45-abe8-f90100150003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940016","2aeefa18-d866-4b45-abe8-f90100160000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940016","2aeefa18-d866-4b45-abe8-f90100160001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940016","2aeefa18-d866-4b45-abe8-f90100160002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940016","2aeefa18-d866-4b45-abe8-f90100160003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940017","2aeefa18-d866-4b45-abe8-f90100170000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940017","2aeefa18-d866-4b45-abe8-f90100170001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940017","2aeefa18-d866-4b45-abe8-f90100170002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940017","2aeefa18-d866-4b45-abe8-f90100170003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940018","2aeefa18-d866-4b45-abe8-f90100180000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940018","2aeefa18-d866-4b45-abe8-f90100180001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940018","2aeefa18-d866-4b45-abe8-f90100180002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940018","2aeefa18-d866-4b45-abe8-f90100180003"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940019","2aeefa18-d866-4b45-abe8-f90100190000"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940019","2aeefa18-d866-4b45-abe8-f90100190001"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940019","2aeefa18-d866-4b45-abe8-f90100190002"
    "9b05db12-d53e-4be6-8d6a-aa8a3b940019","2aeefa18-d866-4b45-abe8-f90100190003"
    """;
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
      inputData.put("homeAddressStreet", List.of(String.format("10%02d Main Street", i),
              String.format("Apartment #1%s", i)));
      inputData.put("homeAddressCity", "San Francisco");
      inputData.put("homeAddressState", "CA");
      inputData.put("homeAddressZipCode", String.format("123%02d", i));

      UUID submissionId = UUID.fromString(String.format("9b05db12-d53e-4be6-8d6a-aa8a3b94%04d", i));

      // mimic a 4 person household for each person.
      String fakePersonUUIDPrefix = String.format("2aeefa18-d866-4b45-abe8-f901%04d", i);
      List<Map<String, Object>> householdList = new ArrayList<>();
      for (int h = 0; h < 4; h++ ) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("uuid", UUID.fromString(String.format("%s00%02d",fakePersonUUIDPrefix, h)));
        entry.put("householdBirthDay", String.format("%d", h));
        entry.put("householdBirthMonth", "11");
        entry.put("householdBirthYear", "2001");
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

  @Test
  void generateRelationshipCsvData() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
    CsvDocument csv = csvGenerator.generateRelationshipCsvData(submissionList);
    assertThat(csv.getCsvType()).isEqualTo(CsvType.RELATIONSHIP);
    String theData = new String(csv.getCsvData());
    assertThat(theData).isEqualTo(expectedRelationshipCsvData);
  }
  @Test
  void generateParentGuardianCsvData() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
    CsvDocument csv = csvGenerator.generateParentGuardianCsvData(submissionList);
    assertThat(csv.getCsvType()).isEqualTo(CsvType.PARENT_GUARDIAN);
    String theData = new String(csv.getCsvData());
    assertThat(theData).isEqualTo(expectedParentGuardianCsvData);
  }
  @Test
  void generateStudentCsvData() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
    CsvDocument csv = csvGenerator.generateStudentCsvData(submissionList);
    assertThat(csv.getCsvType()).isEqualTo(CsvType.STUDENT);
    String theData = new String(csv.getCsvData());
    //assertThat(theData).isEqualTo(expectedStudentCsvData);
  }
  @Test
  void generateWicAppCsvData() {}
  @Test
  void generateECEAppCsvData() {}


}
