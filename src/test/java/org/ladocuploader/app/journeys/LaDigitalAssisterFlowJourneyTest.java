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
    testPage.clickContinue();

    // Mailing Address
    assertThat(testPage.getTitle()).isEqualTo("Mailing address");
    testPage.clickContinue();

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
    assertThat(testPage.getTitle()).isEqualTo("Review contact information");

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

    assertThat(testPage.getTitle()).isEqualTo("School");
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo("Pregnancy");
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("School who");
    testPage.clickElementById("students-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Pregnancy");
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo("Out of state benefits");
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Pregnancy who");
    testPage.clickElementById("pregnancies-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Out of state benefits");
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo("Sensitive Questions");
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Out of state benefits who");
    testPage.clickElementById("outOfStateBenefitsRecipients-you");
    testPage.clickContinue();

    // Job search
    testPage.navigateToFlowScreen("laDigitalAssister/householdJobSearch");
    assertThat(testPage.getTitle()).isEqualTo("Job search");
    testPage.clickButton("Yes");
    assertThat(testPage.getTitle()).isEqualTo("Job search who");

    // SNAP
    testPage.navigateToFlowScreen("laDigitalAssister/householdPrepareFood");
    assertThat(testPage.getTitle()).isEqualTo("Prepare food");
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo("Prepare food who");
    testPage.clickElementById("preparesFood-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Seasonal farm worker");
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Citizenship");
    testPage.clickButton("Yes");
    assertThat(testPage.getTitle()).isEqualTo("Veteran");
    testPage.goBack();
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo("Non-citizen");
    testPage.clickElementById("nonCitizens-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Citizenship number");
    testPage.selectFromDropdown("citizenshipNumber", "1 people");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Veteran");
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo("Foster");
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Veteran who");
    testPage.clickElementById("veterans-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Foster");
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo("Foster aged out");
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Foster who");
    testPage.clickElementById("fosters-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Foster aged out");
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo("Homeless");
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Foster aged out who");
    testPage.clickElementById("fostersAgedOut-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Homeless");
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo("Room rental");
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Homelessness who");
    testPage.clickElementById("homeless-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Room rental");
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo("Meals");
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Room rental who");
    testPage.clickElementById("roomRentals-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Meals");
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo("Sensitive Questions");
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Meals who");
    testPage.clickElementById("meals-you");
    testPage.clickContinue();

    // Sensitive Questions
    assertThat(testPage.getTitle()).isEqualTo("Sensitive Questions");
    testPage.clickContinue();

    //    Case when no personal situations apply
    assertThat(testPage.getTitle()).isEqualTo("Personal Situations");
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo("Domestic Violence Victim");


    testPage.navigateToFlowScreen("laDigitalAssister/householdPersonalSituations");
    assertThat(testPage.getTitle()).isEqualTo("Personal Situations");

    testPage.clickButton("Yes");
    assertThat(testPage.getTitle()).isEqualTo("Personal Situations Who");

    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Which Personal Situations");

    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Domestic Violence Victim");

    testPage.clickButton("Yes");
    assertThat(testPage.getTitle()).isEqualTo("Criminal Justice Involvement Warning");

    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Criminal Justice");

    testPage.navigateToFlowScreen("laDigitalAssister/householdVictimOfDomesticViolence");
    assertThat(testPage.getTitle()).isEqualTo("Domestic Violence Victim");
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo("Criminal Justice Involvement Warning");
  }
}
