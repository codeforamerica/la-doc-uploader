package org.ladocuploader.app.journeys;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.utils.AbstractBasePageTest;

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
    testPage.selectRadio("needInterpreter", "Yes");
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
  }
}
