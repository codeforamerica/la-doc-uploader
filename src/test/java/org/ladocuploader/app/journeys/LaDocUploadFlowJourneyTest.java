package org.ladocuploader.app.journeys;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.utils.AbstractBasePageTest;
import org.openqa.selenium.WebElement;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

@Slf4j
public class LaDocUploadFlowJourneyTest extends AbstractBasePageTest {

  @Test
  void clientDetailsFlow() {
    testPage.navigateToFlowScreen("clientInfo");

    WebElement ssnInput = testPage.findElementById("ssn");
    // SSN input

    // SSN field should be displayed
    assert(ssnInput.getAttribute("class").contains("ssn-input"));
    assert(ssnInput.getAttribute("inputmode").equals("numeric"));

    // SSN label text
    assert(testPage.getInputLabel("ssn").equals("What's your social security number?"));
    assert(testPage.findElementTextById("ssn-help-text").equals("Optional."));

    // SSN activates when clicked
    testPage.clickElementById("ssn");
    assertThat(String.valueOf(driver.switchTo().activeElement().getAttribute("id"))).isEqualTo("ssn");
    driver.switchTo().defaultContent();

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
    assert(testPage.hasErrorText("Make sure your SSN has 9 digits."));



  }

  @Test
  void fullUploadDocumentFlow() {
    // Landing screen (language toggled)
    assertThat(testPage.getTitle()).isEqualTo("Louisiana Document Uploader");
    testPage.clickElementById("translate-button");
    testPage.clickLink("Español");
    assertThat(testPage.getElementText("translate-button")).contains("Traducir");
    // TODO add other assert for page content
    testPage.clickElementById("translate-button");
    testPage.clickLink("English");

    testPage.clickButton("Upload documents");

    // Client Info
    assertThat(testPage.getTitle()).isEqualTo("Match Info");
    testPage.enter("firstName", "Britney");
    testPage.enter("lastName", "Spears");
    testPage.enter("birthDate", "12/2/1981");
    testPage.enter("ssn", "000-00-1111");
    testPage.clickContinue();

    // Go back and ensure that SSN value shown in input is decrypted
    testPage.clickLink("< Go Back");
    assertEquals("000-00-1111", testPage.getInputValue("ssn"));
    testPage.clickContinue();

    // How to add docs
    assertThat(testPage.getTitle()).isEqualTo("How to add documents");
    testPage.clickContinue();

    // Upload documents
    assertThat(testPage.getTitle()).isEqualTo("Upload documents");
    assertThat(testPage.findElementById("form-submit-button").getAttribute("class").contains("display-none")).isTrue();
    uploadJpgFile();
    boolean isHidden = testPage.findElementsByButtonText("I'm finished uploading").getAttribute("class").contains("display-none");
    if (isHidden) { 
      takeSnapShot("flakeyTestScreenshot.jpg");
    }
    assertFalse(isHidden);
    testPage.clickButton("I'm finished uploading");

    // Confirm submit
    assertThat(testPage.getTitle()).isEqualTo("Doc submit confirmation");
    testPage.clickButton("Yes, submit and finish");

    // Confirmation page
    assertThat(testPage.getTitle()).isEqualTo("documents sent");
  }
}
