package org.ladocuploader.app.journeys;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.utils.AbstractBasePageTest;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class LaDigitalAssisterFlowJourneyTest extends AbstractBasePageTest {

  @Test
  void fullUploadDocumentFlow() {
    // How this works
    testPage.navigateToFlowScreen("laDigitalAssister/howThisWorks");
    testPage.clickContinue();

    // Timeout notice
    assertThat(testPage.getTitle()).isEqualTo("Timeout notice");
    testPage.clickContinue();

    // Language preference
    assertThat(testPage.getTitle()).isEqualTo("Language preference");
    testPage.selectFromDropdown("languageRead", "Spanish");
    testPage.selectFromDropdown("languageSpeak", "Spanish");
    WebElement needInterpreterYes = testPage.findElementById("needInterpreter-Yes");
    needInterpreterYes.click();
    testPage.clickContinue();

    // Choose programs
    assertThat(testPage.getTitle()).isEqualTo("Choose programs");
    // Choose SNAP program
    testPage.clickElementById("programs-SNAP");
    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Expedited notice");

    testPage.goBack();
    testPage.clickElementById("programs-SNAP"); // unselect
    testPage.clickElementById("programs-CASH");
    testPage.clickContinue();

    // Signpost
    assertThat(testPage.getTitle()).isEqualTo("Signpost");
    testPage.clickContinue();

    // Who's Applying
    assertThat(testPage.getTitle()).isEqualTo("Who's applying");
    testPage.clickElementById("whosApplying-CommunityPartner");
    testPage.clickContinue();

    // Applicant is not self - check that flow next page is the notice
    assertThat(testPage.getTitle()).isEqualTo("Applicant notice");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Personal info");
    testPage.enter("firstName", "test");
    testPage.enter("lastName", "test2");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Contact info");
  }
}
