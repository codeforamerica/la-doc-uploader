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
    testPage.selectRadio("needInterpreter", "Yes");
    testPage.clickContinue();

    // Choose programs
    assertThat(testPage.getTitle()).isEqualTo("Choose programs");
    // Choose SNAP program
    testPage.clickElementById("programs-SNAP");
    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Expedited notice");

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
    assertThat(testPage.getTitle()).isEqualTo("Mailing address");
    testPage.goBack();
    testPage.clickElementById("noHomeAddress-true");
    testPage.clickContinue();

    // Where to send mail
    assertThat(testPage.getTitle()).isEqualTo("Where to send mail");
    testPage.clickButton("Add a mailing address");

    // Mailing Address
    assertThat(testPage.getTitle()).isEqualTo("Mailing address");
    testPage.clickContinue();

    // Contact Info
    assertThat(testPage.getTitle()).isEqualTo("Contact info");
    testPage.clickContinue();

    // Phone Number Nudge
    assertThat(testPage.getTitle()).isEqualTo("Phone number nudge");
    testPage.clickButton("Add a phone number");

    assertThat(testPage.getTitle()).isEqualTo("Contact info");
    testPage.enter("phoneNumber", "123-456-7891");
    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Review contact information");

    testPage.clickButton("This looks correct");


    // Household
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
    assertThat(testPage.getTitle()).isEqualTo("Prepare food");
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Out of state benefits who");
    testPage.clickElementById("outOfStateBenefitsRecipients-you");
    testPage.clickContinue();

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

    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Criminal Justice Involvement Warning");

    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Criminal Justice");
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Signpost");
    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Job search");
    testPage.clickButton("Yes");
    assertThat(testPage.getTitle()).isEqualTo("Job search who");
    testPage.clickElementById("jobSearch-you");
    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Work disqualifications");
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo("Employment status");
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Income by job");
    testPage.clickLink("I already know my monthly household pre-tax income - I prefer to enter it directly.");
    testPage.enter("monthlyHouseholdIncome", "200");
    testPage.clickContinue();
    testPage.clickButton("Yes, add income by job");
    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Income who");
    testPage.clickElementById("householdMemberJobAdd-you");
    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Employer name");
    testPage.enter("employerName", "job1");
    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Self-employment");
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo("Paid by the hour");
    testPage.clickButton("Yes");
    assertThat(testPage.getTitle()).isEqualTo("Hourly wage");
    testPage.enter("hourlyWage", "15");
    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Hours a week");
    testPage.enter("hoursPerWeek", "10");
    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Income confirmation");
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo("Income list");
    testPage.clickButton("I'm done adding jobs");
    assertThat(testPage.getTitle()).isEqualTo("Additional income");
    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Money on hand");
    testPage.clickContinue();

  //    Expenses SignPost
    assertThat(testPage.getTitle()).isEqualTo("Expenses Signpost");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Home Expenses");;
    testPage.clickElementById("householdHomeExpenses-Rent-label");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Rent");
    testPage.goBack();

    testPage.clickElementById("householdHomeExpenses-Other-label");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Rent");
    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Other");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Utilities");

    testPage.clickElementById("householdUtilitiesExpenses-Water-label");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Water");
    testPage.goBack();

    testPage.clickElementById("householdUtilitiesExpenses-Water-label"); // none selected
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Dependent Care");
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Dependent Care Amount");
    testPage.goBack();
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo("Insurance Expenses");
    testPage.clickElementById("householdInsuranceExpenses-Dental insurance premiums-label"); // none selected
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Dental insurance premiums");
    testPage.goBack();

    testPage.clickElementById("none__checkbox-label"); // none selected
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Elderly Care");
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Elderly Care Amount");

    testPage.goBack();
    testPage.clickButton("No");

    //    Final SignPost
    assertThat(testPage.getTitle()).isEqualTo("Final Signpost");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Authorized Representative");
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo("Medicaid");

    testPage.navigateToFlowScreen("laDigitalAssister/authorizedRepAuthorization");
    assertThat(testPage.getTitle()).isEqualTo("Authorized Representative");
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Authorized Representative Communication Authorization");
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Authorized Representative Mail");
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Authorized Representative Spending");
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo("Authorized Representative Contact Information");
    testPage.enter("authorizedRepFirstName", "test");
    testPage.enter("authorizedRepLastName", "test");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Medicaid");
    testPage.clickButton("Yes, please share my info");


    assertThat(testPage.getTitle()).isEqualTo("Register to Vote");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Help Registering to Vote");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Race and Ethnicity");
    testPage.clickButton("No, skip this question");

    assertThat(testPage.getTitle()).isEqualTo("Legal Stuff");

    testPage.navigateToFlowScreen("laDigitalAssister/raceEthnicityAsk");

    assertThat(testPage.getTitle()).isEqualTo("Race and Ethnicity");
    testPage.clickButton("Yes, continue");

    assertThat(testPage.getTitle()).isEqualTo("Ethnicity Selection");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Race Selection");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Legal Stuff");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Signature");
    testPage.clickButton("Submit Application");

    assertThat(testPage.getTitle()).isEqualTo("Confirmation");
  }
}
