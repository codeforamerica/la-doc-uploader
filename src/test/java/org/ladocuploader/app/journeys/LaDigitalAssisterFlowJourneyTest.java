package org.ladocuploader.app.journeys;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.utils.AbstractBasePageTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class LaDigitalAssisterFlowJourneyTest extends AbstractBasePageTest {

  @Test
  void fullDigitalAssisterFlow() {
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

    // Who's Applying
    assertThat(testPage.getTitle()).isEqualTo("Who's applying");
    testPage.clickElementById("whosApplying-CommunityPartner");
    testPage.clickContinue();

    // Applicant is not self - check that flow next page is the notice
    assertThat(testPage.getTitle()).isEqualTo("Applicant notice");
    testPage.clickContinue();

    // Personal Info
    assertThat(testPage.getTitle()).isEqualTo("Personal info");
    testPage.enter("firstName", "test");
    testPage.enter("lastName", "test2");
    testPage.clickContinue();

    // Home Address
    assertThat(testPage.getTitle()).isEqualTo("Home Address");

    // Contact Info
    testPage.navigateToFlowScreen("laDigitalAssister/contactInfo");
    assertThat(testPage.getTitle()).isEqualTo("Contact info");
    testPage.clickContinue();

    // Phone Number Nudge
    assertThat(testPage.getTitle()).isEqualTo("Phone number nudge");
    testPage.clickButton("Add a phone number");

    assertThat(testPage.getTitle()).isEqualTo("Contact info");
    testPage.enter("phoneNumber", "123-456-7891");
    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Review Contact Information");

    // Household
    testPage.navigateToFlowScreen("laDigitalAssister/multiplePersonHousehold");
    assertThat(testPage.getTitle()).isEqualTo("Multiple person household");
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Signpost");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Housemate info");
    testPage.enter("householdMemberFirstName", "roomy");
    testPage.enter("householdMemberLastName", "smith");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Household list");
    testPage.clickButton("Yes, this is everyone");

    assertThat(testPage.getTitle()).isEqualTo("SSN");
    testPage.clickLink("Learn why we ask for SSNs");
    assertThat(testPage.getTitle()).isEqualTo("SSN faqs");
    testPage.clickButton("Back");
    testPage.clickContinue();

    // Special situations
    assertThat(testPage.getTitle()).isEqualTo("Next step");
    testPage.clickContinue();

    // Job search
    testPage.navigateToFlowScreen("laDigitalAssister/householdJobSearch");
    assertThat(testPage.getTitle()).isEqualTo("Job search");
    testPage.clickButton("Yes");
    assertThat(testPage.getTitle()).isEqualTo("Job search who");

    // SNAP
    testPage.navigateToFlowScreen("laDigitalAssister/householdSeasonalFarmWorker");
    assertThat(testPage.getTitle()).isEqualTo("Seasonal farm worker");
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Citizenship");
    testPage.clickButton("Yes");

    testPage.navigateToFlowScreen("laDigitalAssister/householdHomeless");
    assertThat(testPage.getTitle()).isEqualTo("Homeless");
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Homelessness who");
    testPage.clickContinue();

    // Sensitive Questions
    testPage.navigateToFlowScreen("laDigitalAssister/sensitiveQuestions");
    assertThat(testPage.getTitle()).isEqualTo("Sensitive Questions");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Household Personal Situations");

  }
}
