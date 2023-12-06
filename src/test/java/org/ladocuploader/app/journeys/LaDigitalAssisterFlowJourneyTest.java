package org.ladocuploader.app.journeys;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.utils.AbstractBasePageTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class LaDigitalAssisterFlowJourneyTest extends AbstractBasePageTest {

  protected static final String RANGE_ERROR_MESSAGE="Make sure to provide a value between 1 and 100.";

  @Test
  void chooseProgramsFlow() {
    testPage.navigateToFlowScreen("laDigitalAssister/choosePrograms");
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.missing-general")));
    testPage.clickElementById("programs-SNAP");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("expedited-snap.title"));
  }
  @Test
  void whosApplyingFlow() {
    testPage.navigateToFlowScreen("laDigitalAssister/whosApplying");
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.missing-general")));
    testPage.clickElementById("whosApplying-Self");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("personal-info.title"));
  }

  @Test
  void personalInformationFlow() {
    testPage.navigateToFlowScreen("laDigitalAssister/personalInfo");
    testPage.clickContinue();

    testPage.enter("birthMonth", "01");
    testPage.enter("birthDay", "25");
    testPage.enter("birthYear", "1985");

    assert(testPage.hasErrorText(message("error.missing-firstname")));
    assert(testPage.hasErrorText(message("error.missing-lastname")));
    assert(testPage.hasErrorText(message("error.missing-general")));

    testPage.enter("firstName", "test");
    testPage.enter("lastName", "test2");
    testPage.selectRadio("sex", "F");

    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo(message("home-address.title"));

  }

  @Test
  void birthDateFlow() {
    testPage.navigateToFlowScreen("laDigitalAssister/personalInfo");
    testPage.enter("firstName", "test");
    testPage.enter("lastName", "test2");
    testPage.selectRadio("sex", "F");
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.format-dob")));

    testPage.enter("birthMonth", "12");
    testPage.enter("birthDay", "09");
    testPage.enter("birthYear", "85");
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.format-dob")));

    testPage.enter("birthMonth", "12");
    testPage.enter("birthDay", "25");
    testPage.enter("birthYear", "1885");
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.invalid-dob")));

    testPage.enter("birthMonth", "12");
    testPage.enter("birthDay", "25");
    testPage.enter("birthYear", "2025");

    testPage.clickContinue();
    assert(testPage.hasErrorText(message("error.invalid-dob")));

    testPage.enter("birthMonth", "2");
    testPage.enter("birthDay", "9");
    testPage.enter("birthYear", "1985");

    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo(message("home-address.title"));
  }

  @Test
  void noHomeAddressFlow(){
    testPage.navigateToFlowScreen("laDigitalAssister/homeAddress");
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.missing-address")));

    testPage.clickElementById("noHomeAddress-true");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("where-to-send-mail.title"));

    testPage.clickButton("Add a mailing address");

    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.missing-general")));
    assert(testPage.hasErrorText(message("error.format-zip")));

    testPage.enter("mailingAddressStreetAddress1", "test");
    testPage.enter("mailingAddressCity", "test2");
    testPage.enter("mailingAddressZipCode", "12");
    testPage.selectFromDropdown("mailingAddressState", "CO - Colorado");

    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.format-zip")));

    testPage.enter("mailingAddressZipCode", "12526");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("contact-info.title"));
  }

  @Test
  void homeAddressFlow(){
    testPage.navigateToFlowScreen("laDigitalAssister/homeAddress");
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.missing-address")));

    testPage.enter("homeAddressStreetAddress1", "123 Main St");
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.missing-general")));
    assert(testPage.hasErrorText(message("error.format-zip")));

    testPage.enter("homeAddressCity", "test2");
    testPage.selectFromDropdown("homeAddressState", "CO - Colorado");
    testPage.enter("homeAddressZipCode", "12526");

    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("mailing-address.title"));

    testPage.clickElementById("sameAsHomeAddress-true");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("contact-info.title"));
    testPage.goBack();

    assertThat(testPage.getTitle()).isEqualTo(message("mailing-address.title"));
    testPage.clickElementById("sameAsHomeAddress-true");

    testPage.enter("mailingAddressStreetAddress1", "test");
    testPage.selectFromDropdown("mailingAddressState", "CO - Colorado");
    testPage.enter("mailingAddressCity", "");
    testPage.enter("mailingAddressZipCode", "1245");

    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.missing-general")));
    assert(testPage.hasErrorText(message("error.format-zip")));

    testPage.enter("mailingAddressCity", "Test 5");
    testPage.enter("mailingAddressZipCode", "12526");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("contact-info.title"));
  }

  @Test
  void contactInformationFlow(){
    testPage.navigateToFlowScreen("laDigitalAssister/contactInfo");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("phone-number-nudge.title"));
    testPage.goBack();

    testPage.enter("emailAddress", "test");
    testPage.enter("phoneNumber", "123-456-789");

    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.invalid-email")));
    assert(testPage.hasErrorText(message("error.invalid-phone")));

    testPage.enter("emailAddress", "");
    testPage.enter("phoneNumber", "");

    testPage.clickElementById("remindersMethod-By Text-label");

    testPage.clickContinue();
    assert(testPage.hasErrorText(message("error.invalid-phone")));

    testPage.enter("phoneNumber", "123-456-7891");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("review-contact-info.title"));
    testPage.goBack();

    testPage.enter("phoneNumber", "123-456-789");
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.invalid-phone")));
    testPage.clickElementById("remindersMethod-By Text-label");
    testPage.clickElementById("remindersMethod-By Email-label");
    testPage.enter("emailAddress", "test@mail.com");
    testPage.enter("phoneNumber", "");


    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("phone-number-nudge.title"));
    testPage.goBack();

    testPage.enter("phoneNumber", "123-456-7891");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("review-contact-info.title"));

    testPage.goBack();

    testPage.enter("emailAddress", "test");
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.invalid-email")));

    testPage.enter("emailAddress", "test@mail.com");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("review-contact-info.title"));
  }

  @Test
  void hourlyIncomeFlow(){
    loadUserPersonalData();
    loadHouseHoldData("First", "User", "12", "22", "1991");
    loadHouseHoldData("Second", "User", "01", "23", "1997");
    preloadIncomeScreen();

    assertThat(testPage.getTitle()).isEqualTo(message("income-by-job.title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("income-who.title"));
    testPage.clickContinue();
    assert(testPage.hasErrorText(message("error.missing-general")));

    testPage.clickElementById("householdMemberJobAdd-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("employer-name.title"));
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.missing-general")));
    testPage.enter("employerName", "job1");

    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("self-employment.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("job-paid-by-hour.title"));
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("job-hourly-wage.title"));
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.missing-dollar-amount")));
    testPage.enter("hourlyWage", "a");
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.invalid-money")));
    testPage.enter("hourlyWage", ".99");

    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("job-hours-per-week.title"));
    testPage.clickContinue();

    assert(testPage.hasErrorText(RANGE_ERROR_MESSAGE));
    assert(testPage.hasErrorText(message("error.missing-general")));

    testPage.enter("hoursPerWeek", "100000");
    testPage.clickContinue();

    assert(testPage.hasErrorText(RANGE_ERROR_MESSAGE));

    testPage.enter("hoursPerWeek", "10");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("income-confirmation.title"));
  }

  @Test
  void monthlyIncomeFlow(){
    loadUserPersonalData();
    loadHouseHoldData("Third", "User", "12", "22", "1991");
    loadHouseHoldData("Fourth", "User", "01", "23", "1997");
    preloadIncomeScreen();

    assertThat(testPage.getTitle()).isEqualTo(message("income-by-job.title"));
    testPage.clickLink(message("income-by-job.enter-directly"));

    assertThat(testPage.getTitle()).isEqualTo(message("household-annual-income.title"));
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.missing-dollar-amount")));
    testPage.enter("monthlyHouseholdIncome", "abc");
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.invalid-money")));
    testPage.enter("monthlyHouseholdIncome", "1286.55");

    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("income-list.title"));
  }

  @Test
  void otherIncomeFlow(){
    loadUserPersonalData();
    loadHouseHoldData("Third", "User", "12", "22", "1991");
    loadHouseHoldData("Fourth", "User", "01", "23", "1997");
    preloadIncomeScreen();

    assertThat(testPage.getTitle()).isEqualTo(message("income-by-job.title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("income-who.title"));
    testPage.clickContinue();
    assert(testPage.hasErrorText(message("error.missing-general")));

    testPage.clickElementById("householdMemberJobAdd-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("employer-name.title"));
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.missing-general")));
    testPage.enter("employerName", "job1");

    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("self-employment.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("job-paid-by-hour.title"));
    testPage.clickButton("No");

    testPage.clickContinue();
    assert(testPage.hasErrorText(message("error.missing-pay-period")));

    testPage.selectRadio("payPeriod", "Every month");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("job-pay-amount.title"));

    testPage.enter("payPeriodAmount", "a");
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.invalid-money")));
    testPage.enter("payPeriodAmount", "282.99");

    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("income-confirmation.title"));
  }


  @Test
  void fullDigitalAssisterFlow() {
    // How this works
    testPage.navigateToFlowScreen("laDigitalAssister/howThisWorks");
    testPage.clickContinue();

    // Timeout notice
    assertThat(testPage.getTitle()).isEqualTo(message("timeout-notice.title"));
    testPage.clickContinue();

    // Language preference
    assertThat(testPage.getTitle()).isEqualTo(message("language-preference.title"));
    testPage.selectFromDropdown("languageRead", "Spanish");
    testPage.selectRadio("needInterpreter", "Yes");
    testPage.clickContinue();

    // Choose programs
    assertThat(testPage.getTitle()).isEqualTo(message("choose-programs.title"));

    // Choose SNAP program
    testPage.clickElementById("programs-SNAP");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("expedited-snap.title"));
    testPage.clickContinue();

    // Signpost
    assertThat(testPage.getTitle()).isEqualTo(message("signpost.title"));
    testPage.clickContinue();

    // Who's Applying
    assertThat(testPage.getTitle()).isEqualTo(message("whos-applying.title"));
    testPage.clickElementById("whosApplying-CommunityPartner");
    testPage.clickContinue();

    // Applicant is not self - check that flow next page is the notice
    assertThat(testPage.getTitle()).isEqualTo(message("applicant-notice.title"));
    testPage.clickContinue();

    // Personal Info
    assertThat(testPage.getTitle()).isEqualTo(message("personal-info.title"));
    testPage.enter("firstName", "test");
    testPage.enter("lastName", "test2");
    testPage.enter("birthMonth", "12");
    testPage.enter("birthDay", "25");
    testPage.enter("birthYear", "1985");
    testPage.selectRadio("sex", "F");
    testPage.clickContinue();

    // Home Address
    assertThat(testPage.getTitle()).isEqualTo(message("home-address.title"));

    testPage.clickElementById("noHomeAddress-true");
    testPage.clickContinue();

    // Where to send mail
    assertThat(testPage.getTitle()).isEqualTo(message("where-to-send-mail.title"));

    testPage.goBack();
    testPage.clickElementById("noHomeAddress-true");
    testPage.enter("homeAddressStreetAddress1", "test");
    testPage.enter("homeAddressCity", "test2");
    testPage.enter("homeAddressZipCode", "12");
    testPage.selectFromDropdown("homeAddressState", "CO - Colorado");
    testPage.enter("homeAddressZipCode", "12526");

    testPage.clickContinue();

    // Mailing Address
    assertThat(testPage.getTitle()).isEqualTo(message("mailing-address.title"));

    testPage.enter("mailingAddressStreetAddress1", "test");
    testPage.enter("mailingAddressCity", "test 2");
    testPage.selectFromDropdown("mailingAddressState", "CO - Colorado");
    testPage.enter("mailingAddressZipCode", "12455");

    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("contact-info.title"));

    assertThat(testPage.getTitle()).isEqualTo(message("contact-info.title"));
    testPage.clickElementById("remindersMethod-By Email-label");
    testPage.enter("emailAddress", "mail@mail.com");


    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("phone-number-nudge.title"));

    testPage.clickButton("Add a phone number");

    assertThat(testPage.getTitle()).isEqualTo(message("contact-info.title"));
    testPage.enter("phoneNumber", "123-456-7891");

    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("review-contact-info.title"));

    testPage.clickButton("This looks correct");

    assertThat(testPage.getTitle()).isEqualTo(message("multiple-person-household.title"));
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("household-signpost.title"));

    testPage.clickContinue();
    // Household

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

    assertThat(testPage.getTitle()).isEqualTo(message("income-signpost.title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("job-search.title"));
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("job-search-who.title"));
    testPage.clickElementById("jobSearch-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("work-disqualifications.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("employment-status.title"));
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("income-by-job.title"));

    testPage.clickLink(message("income-by-job.enter-directly"));

    assertThat(testPage.getTitle()).isEqualTo(message("household-annual-income.title"));

    testPage.enter("monthlyHouseholdIncome", "200");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("income-list.title"));

    testPage.clickButton(message("household-income-total.yes"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("income-who.title"));
    testPage.clickElementById("householdMemberJobAdd-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("employer-name.title"));
    testPage.enter("employerName", "job1");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("self-employment.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("job-paid-by-hour.title"));
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("job-hourly-wage.title"));
    testPage.enter("hourlyWage", "15");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("job-hours-per-week.title"));
    testPage.enter("hoursPerWeek", "10");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("income-confirmation.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("income-list.title"));
    testPage.clickButton(message("income-list.continue"));

    assertThat(testPage.getTitle()).isEqualTo(message("additional-income.title"));
    testPage.clickElementById("none__checkbox");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("money-on-hand.title"));
    testPage.enter("moneyOnHand", "0");
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

  void loadUserPersonalData() {
    testPage.navigateToFlowScreen("laDigitalAssister/personalInfo");

    testPage.enter("firstName", "test");
    testPage.enter("lastName", "test2");
    testPage.enter("birthMonth", "12");
    testPage.enter("birthDay", "25");
    testPage.enter("birthYear", "1985");
    testPage.selectRadio("sex", "F");
    testPage.clickContinue();
  }

  void loadHouseHoldData(String firstName, String lastName, String month, String day, String year) {
    testPage.navigateToFlowScreen("laDigitalAssister/householdInfo");
    testPage.enter("householdMemberFirstName", firstName);
    testPage.enter("householdMemberLastName", lastName);
    testPage.enter("householdMemberBirthMonth", month);
    testPage.enter("householdMemberBirthDay", day);
    testPage.enter("householdMemberBirthYear", year);
    testPage.selectRadio("householdMemberSex", "F");
    testPage.clickContinue();
  }

  void preloadIncomeScreen(){
    testPage.navigateToFlowScreen("laDigitalAssister/incomeSignPost");
    testPage.clickContinue();

    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("job-search-who.title"));
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.missing-general")));

    testPage.clickElementById("jobSearch-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("work-disqualifications.title"));
    testPage.clickButton("No");

    testPage.clickButton("No");
  }
}
