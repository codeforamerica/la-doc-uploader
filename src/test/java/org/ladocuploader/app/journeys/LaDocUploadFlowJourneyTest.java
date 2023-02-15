package org.ladocuploader.app.journeys;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.ladocuploader.app.utils.AbstractBasePageTest;
import org.junit.jupiter.api.Test;

public class LaDocUploadFlowJourneyTest extends AbstractBasePageTest {

  @Test
  void fullUploadDocumentFlow() {
    // Landing screen
    assertThat(testPage.getTitle()).isEqualTo("Louisiana Document Uploader");
    testPage.clickButton("Upload documents");

    // Client Info
    assertThat(testPage.getTitle()).isEqualTo("Match Info");
    testPage.enter("firstName", "Britney");
    testPage.enter("lastName", "Spears");
    testPage.clickContinue();

    // How to add docs
    assertThat(testPage.getTitle()).isEqualTo("How to add documents");
    testPage.clickContinue();

    // Upload documents
    assertThat(testPage.getTitle()).isEqualTo("Upload documents");
    assertThat(testPage.findElementById("form-submit-button").getAttribute("class").contains("disabled")).isTrue();
    uploadJpgFile();
    assertFalse(testPage.findElementById("form-submit-button").getAttribute("class").contains("disabled"));
    testPage.clickButton("I'm finished uploading");

    // Confirm submit
    takeSnapShot("snapshot.png");
    assertThat(testPage.getTitle()).isEqualTo("Doc submit confirmation");
    testPage.clickButton("Yes, submit and finish");

    // Confirmation page
    assertThat(testPage.getTitle()).isEqualTo("documents sent");
  }
}
