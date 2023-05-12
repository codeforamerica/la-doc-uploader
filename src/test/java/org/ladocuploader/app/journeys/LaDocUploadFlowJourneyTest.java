package org.ladocuploader.app.journeys;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.ladocuploader.app.utils.AbstractBasePageTest;

public class LaDocUploadFlowJourneyTest extends AbstractBasePageTest {

  @Test
  void fullUploadDocumentFlow() throws InterruptedException {
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
    wait(300); // TODO-SP why does the submit take so long?
    assertThat(testPage.getTitle()).isEqualTo("documents sent");
  }
}
