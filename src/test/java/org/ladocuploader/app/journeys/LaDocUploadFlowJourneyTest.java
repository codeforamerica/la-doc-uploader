package org.ladocuploader.app.journeys;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.testutils.AbstractBasePageTest;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
public class LaDocUploadFlowJourneyTest extends AbstractBasePageTest {

  @Test
  void firstNameInputFlow() {
    testPage.navigateToFlowScreen("laDocUpload/clientInfo");

    // SSN input
    WebElement firstNameInput = testPage.findElementById("firstName");
    assertThat(firstNameInput).isNotNull();

    assert(testPage.getInputLabel("firstName").equals("What's your first name?"));
    assert(testPage.findElementTextById("firstName-help-text").equals("Legally as it appears on your ID."));

    // firstName activates when clicked
    testPage.clickElementById("firstName");
    assertThat(testPage.isInputActive("firstName")).isTrue();

    // entering ssn fewer than 9 digits results in custom error message
    testPage.clickContinue();
    assert(testPage.hasErrorText("Make sure you provide a first name"));
  }

  @Test
  void lastNameInputFlow() {
    testPage.navigateToFlowScreen("laDocUpload/clientInfo");

    // SSN input
    WebElement lastNameInput = testPage.findElementById("lastName");
    assertThat(lastNameInput).isNotNull();

    assert(testPage.getInputLabel("lastName").equals("What's your last name?"));
    assert(testPage.findElementTextById("lastName-help-text").equals("Legally as it appears on your ID."));

    // firstName activates when clicked
    testPage.clickElementById("lastName");
    assertThat(testPage.isInputActive("lastName")).isTrue();

    // entering ssn fewer than 9 digits results in custom error message
    testPage.clickContinue();
    assert(testPage.hasErrorText("Make sure you provide a last name"));
  }

  @Test
  void emailAddressInputFlow() {
    testPage.navigateToFlowScreen("laDocUpload/clientInfo");

    // SSN input
    WebElement emailAddressInput = testPage.findElementById("emailAddress");
    assertThat(emailAddressInput).isNotNull();

    assert(testPage.getInputLabel("emailAddress").equals("What's your email address?"));
    assert(testPage.findElementTextById("emailAddress-help-text").equals("Optional. This is so DCFS can contact you if they have questions."));

    // emailAddress activates when clicked
    testPage.clickElementById("emailAddress");
    assertThat(testPage.isInputActive("emailAddress")).isTrue();

    // entering an invalid email address results in an error message
    testPage.enter("emailAddress", "1234@g@.org");
    testPage.clickContinue();
    assert(testPage.hasErrorText("Make sure you entered your email address correctly"));
  }

  @Test
  void caseNumberInputFlow() {
    testPage.navigateToFlowScreen("laDocUpload/clientInfo");

    // SSN input
    WebElement caseNumberInput = testPage.findElementById("caseNumber");
    assertThat(caseNumberInput).isNotNull();

    assert(testPage.getInputLabel("caseNumber").equals("If you have it, what's your case #?"));
    assert(testPage.findElementTextById("caseNumber-help-text").equals("Optional."));

    // activates when clicked
    testPage.clickElementById("caseNumber");
    assertThat(testPage.isInputActive("caseNumber")).isTrue();

    // user can type an input into the case number field
    testPage.enter("caseNumber", "1234");
    assertThat(testPage.getInputValue("caseNumber")).isEqualTo("1234");
  }

  @Test
  void birthDateInputFlow() {
    testPage.navigateToFlowScreen("laDocUpload/clientInfo");

    // birthdate inputs
    WebElement birthDateMonthInput = testPage.findElementById("birthDate-month");
    assertThat(birthDateMonthInput).isNotNull();
    WebElement birthDateDayInput = testPage.findElementById("birthDate-day");
    assertThat(birthDateDayInput).isNotNull();
    WebElement birthDateYearInput = testPage.findElementById("birthDate-year");
    assertThat(birthDateYearInput).isNotNull();

    // birthDate label text
    assertThat(testPage.getInputLabel("birthDate", 3)).isEqualTo("mm");
    assertThat(testPage.getInputLabel("birthDate", 2)).isEqualTo("dd");
    assertThat(testPage.getInputLabel("birthDate", 1)).isEqualTo("yyyy");

    // birthDate activates when clicked
    testPage.clickElementById("birthDate-month");
    assertThat(testPage.isInputActive("birthDate-month")).isTrue();
    testPage.clickElementById("birthDate-year");
    assertThat(testPage.isInputActive("birthDate-year")).isTrue();
    testPage.clickElementById("birthDate-day");
    assertThat(testPage.isInputActive("birthDate-day")).isTrue();

    // Continue with blank birthdate results in error
    testPage.clickContinue();
    assertThat(testPage.hasErrorText("Please enter your birthdate in this format: mm/dd/yyyy")).isTrue();

    // enter text and special characters results in error
    testPage.enter("birthDate", "*3/AB/DD");
    testPage.clickContinue();
    assertThat(testPage.hasErrorText("Please enter your birthdate in this format: mm/dd/yyyy")).isTrue();

    // custom error in case of birthdate before 1/1/1900
    testPage.enter("birthDate", "1/1/1899");
    testPage.clickContinue();
    assertThat(testPage.hasErrorText("Please make sure that the date of birth is between 1/1/1900 and today.")).isTrue();

    // custom error in case of birthdate is after today
    LocalDate sourceDate = LocalDate.now();  // Source Date
    LocalDate destDate = sourceDate.plusDays(1); // Adding a day to source date.

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // Setting date format

    String tomorrow = destDate.format(formatter);
    System.out.println(tomorrow);
    testPage.enter("birthDate", tomorrow);
    testPage.clickContinue();
    assertThat(testPage.hasErrorText("Please make sure that the date of birth is between 1/1/1900 and today.")).isTrue();

    // User can type a birthDate
    testPage.enter("birthDate", "12/02/1981");
//    assertThat(testPage.getInputValue("birthDate")).isEqualTo("12/2/1981"); TODO: figure out why this only stores month

  }

  @Test
  void ssnInputFlow() {
    testPage.navigateToFlowScreen("laDocUpload/clientInfo");

    // SSN input
    WebElement ssnInput = testPage.findElementById("ssn");
    assertThat(ssnInput).isNotNull();

    // SSN field should be displayed
    assert (ssnInput.getAttribute("class").contains("ssn-input"));
    assert (ssnInput.getAttribute("inputmode").equals("numeric"));

    // SSN label text
    assert (testPage.getInputLabel("ssn").equals("What's your social security number?"));
    assert (testPage.findElementTextById("ssn-help-text").equals("Optional."));

    // SSN activates when clicked
    testPage.clickElementById("ssn");
    assertThat(testPage.isInputActive("ssn")).isTrue();

    // Copy and paste into field is working - not possible in headless mode

    // entering letters does not display anything in the input
    testPage.enter("ssn", "ABCD");
    assertThat(testPage.getInputValue("ssn")).isEqualTo("");

    // entering special characters does not display anything in the input
    testPage.enter("ssn", "*&#^!");
    assertThat(testPage.getInputValue("ssn")).isEqualTo("");

    // entering digits displays digits from keyboard
    testPage.enter("ssn", "1234");
    assertThat(testPage.getInputValue("ssn")).isEqualTo("123-4");

    // entering ssn fewer than 9 digits results in custom error message
    testPage.clickContinue();
    assert (testPage.hasErrorText("Make sure your SSN has 9 digits."));

  }

  @Test
  void fullUploadDocumentFlow() {
    testPage.navigateToFlowScreen("laDocUpload/clientInfo");

    // Client Info
    assertThat(testPage.getTitle()).isEqualTo("Match Info");
    testPage.enter("firstName", "Britney");
    testPage.enter("lastName", "Spears");
    testPage.enter("birthDate", "12/2/1981");
    testPage.enter("ssn", "000-00-1111");
    testPage.clickContinue();

    // Go back and ensure that SSN value shown in input is decrypted
    testPage.goBack();
    assertEquals("000-00-1111", testPage.getInputValue("ssn"));
    testPage.clickContinue();

    // How to add docs
    assertThat(testPage.getTitle()).isEqualTo("How to add documents");
    testPage.clickContinue();

    // Upload documents
    assertThat(testPage.getTitle()).isEqualTo("Upload documents");
    assertThat(testPage.findElementById("form-submit-button").getAttribute("class").contains("display-none")).isTrue();
    uploadJpgFile();
    // give the system time to remove the "display-none" class.
    await().atMost(5, TimeUnit.SECONDS).until(
        () -> !(testPage.findElementById("form-submit-button").getAttribute("class").contains("display-none"))
    );

    testPage.clickButton("I'm finished uploading");

    // Confirm submit
    assertThat(testPage.getTitle()).isEqualTo("Doc submit confirmation");
    testPage.clickButton("No, add more documents");
    assertThat(testPage.getTitle()).isEqualTo("Upload documents");
    testPage.clickButton("I'm finished uploading");
    testPage.clickButton("Yes, submit and finish");

    // Confirmation page
    assertThat(testPage.getTitle()).isEqualTo("Documents sent");
  }
}
