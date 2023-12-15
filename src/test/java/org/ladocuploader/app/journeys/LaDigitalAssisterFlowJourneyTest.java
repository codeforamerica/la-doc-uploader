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
  void socialSecurityFlow(){
    loadUserPersonalData();

    testPage.navigateToFlowScreen("laDigitalAssister/ssnForm");

    assertThat(testPage.getTitle()).isEqualTo(message("ssn-form.title"));

    testPage.enter("ssn", "1234");
    testPage.clickContinue();

    assert(testPage.hasErrorText(message("error.invalid-ssn")));
    testPage.enter("ssn", "");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("special-situations.title"));
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

    assertThat(testPage.getTitle()).isEqualTo(message("household-info.title"));
    testPage.enter("householdMemberFirstName", "roomy");
    testPage.enter("householdMemberLastName", "smith");
    testPage.selectRadio("householdMemberSex", "F");
    testPage.enter("householdMemberBirthMonth", "1");
    testPage.enter("householdMemberBirthDay", "25");
    testPage.enter("householdMemberBirthYear", "1991");
    testPage.selectFromDropdown("householdMemberRelationship", message("household-info.relationship.step-child"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("household-list.title"));
    testPage.clickButton("Yes, this is everyone");

    assertThat(testPage.getTitle()).isEqualTo(message("ssn-form.title"));
    testPage.clickLink("Learn why we ask for SSNs");

    assertThat(testPage.getTitle()).isEqualTo(message("ssn-faqs.title"));
    testPage.goBack();

    testPage.clickContinue();

    // Special situations
    assertThat(testPage.getTitle()).isEqualTo(message("special-situations.title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("school.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("pregnancy.title"));
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("school-who.title"));
    testPage.clickElementById("students-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("schooldetails.title"));
    testPage.enter("schoolName_wildcard_you", "School Name");
    testPage.clickElementById("schoolEnrollmentLevel_wildcard_you-Half-time");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("pregnancy.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("out-of-state-benefits.title"));
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("pregnancy-who.title"));
    testPage.clickElementById("pregnancies-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("pregnantduedate.title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("out-of-state-benefits.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("household-prepare-food.title"));
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("out-of-state-benefits-who.title"));
    testPage.clickElementById("outOfStateBenefitsRecipients-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("household-prepare-food.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("household-prepare-food-who.title"));
    testPage.clickElementById("preparesFood-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("seasonal-farmworker.title"));
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("citizenship.title"));
    testPage.clickButton("Yes");
    assertThat(testPage.getTitle()).isEqualTo(message("veteran.title"));
    testPage.goBack();
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("non-citizen.title"));
    testPage.clickElementById("nonCitizens-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("citizenship-number.title"));
    testPage.selectFromDropdown("citizenshipNumber", "1 people");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("veteran.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("foster.title"));
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("veteran-who.title"));
    testPage.clickElementById("veterans-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("foster.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("foster-aged-out.title"));
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("foster-who.title"));
    testPage.clickElementById("fosters-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("foster-aged-out.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("homeless.title"));
    testPage.goBack();
    testPage.clickButton("Yes");


    assertThat(testPage.getTitle()).isEqualTo(message("foster-aged-out-who.title"));
    testPage.clickElementById("fostersAgedOut-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("homeless.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("household-room-rental.title"));
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("homeless-who.title"));
    testPage.clickElementById("homeless-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("household-room-rental.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("household-meals.title"));
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("household-room-rental-who.title"));
    testPage.clickElementById("roomRentals-you");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("household-meals.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("sensitive-questions.title"));
    testPage.goBack();
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("household-meals-who.title"));
    testPage.clickElementById("meals-you");
    testPage.clickContinue();

    // Sensitive Questions
    assertThat(testPage.getTitle()).isEqualTo(message("sensitive-questions.title"));
    testPage.clickContinue();

    //    Case when no personal situations apply
    assertThat(testPage.getTitle()).isEqualTo(message("personal-situations.title"));
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("domestic-violence.title"));
    testPage.goBack();

    assertThat(testPage.getTitle()).isEqualTo(message("personal-situations.title"));
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("personal-situations-who.title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("personal-situations-which.title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("domestic-violence.title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("criminal-justice-warning.title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("criminal-justice.title"));
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

    assertThat(testPage.getTitle()).isEqualTo(message("moneyonhand-types.title"));
    testPage.clickContinue();

    //    Expenses SignPost
    assertThat(testPage.getTitle()).isEqualTo(message("expenses-signpost.title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("home-expenses.title"));;
    testPage.clickElementById("householdHomeExpenses-Rent-label");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("home-expenses.rent"));
    testPage.goBack();

    testPage.clickElementById("householdHomeExpenses-Other-label");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("home-expenses.rent"));
    testPage.enter("expensesRent", "500");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("home-expenses.other"));
    testPage.enter("expensesOther", "15");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("utilities.title"));

    testPage.clickElementById("householdUtilitiesExpenses-Water-label");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("utilities.water"));
    testPage.goBack();

    testPage.clickElementById("none__checkbox-label"); // none selected
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("energy-assistance-title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("liheap-title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("dependentcare.title"));
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("dependentcare-expenses.title"));
    testPage.goBack();
    testPage.clickButton("No");

    assertThat(testPage.getTitle()).isEqualTo(message("insurance-expenses.title"));
    testPage.clickElementById("householdInsuranceExpenses-Dental bills-label");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("insurance-expenses-amount.title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("elderlycare.title"));
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("elderlycare-amount.title"));

    testPage.goBack();
    testPage.clickButton("No");

    //    Final SignPost
    assertThat(testPage.getTitle()).isEqualTo(message("final-signpost.title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("ebtcard-title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("authorized-rep.title"));
    testPage.clickButton("No");
    assertThat(testPage.getTitle()).isEqualTo(message("medicaid.title"));

    testPage.goBack();
    assertThat(testPage.getTitle()).isEqualTo(message("authorized-rep.title"));
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("authorized-rep-communication.title"));
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("authorized-rep-mail.title"));
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("authorized-rep-spending.title"));
    testPage.clickButton("Yes");

    assertThat(testPage.getTitle()).isEqualTo(message("authorized-rep-contact-info.title"));
    testPage.enter("authorizedRepFirstName", "test");
    testPage.enter("authorizedRepLastName", "test");
    testPage.enter("authorizedRepStreetAddress1", "test5");
    testPage.enter("authorizedRepCity", "test2");
    testPage.enter("authorizedRepZipCode", "12345");
    testPage.selectFromDropdown("authorizedRepState", "CO - Colorado");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("medicaid.title"));
    testPage.clickButton(message("medicaid.yes"));

    assertThat(testPage.getTitle()).isEqualTo(message("voter-registration.title"));
    testPage.selectRadio("votingRegistrationRequested", "Yes");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("voter-registration-help.title"));
    testPage.selectRadio("votingRegistrationHelpRequested", "No");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("race-ethnicity.title"));
    testPage.clickButton("No, skip this question");

    assertThat(testPage.getTitle()).isEqualTo(message("legal-title"));
    testPage.goBack();

    assertThat(testPage.getTitle()).isEqualTo(message("race-ethnicity.title"));
    testPage.clickButton("Yes, continue");

    assertThat(testPage.getTitle()).isEqualTo(message("ethnicity-selection.title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("race-selection.title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("legal-title"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo(message("signature-title"));
    testPage.enter("signature", "My signature");
    testPage.clickButton(message("signature-submit"));

    assertThat(testPage.getTitle()).isEqualTo(message("confirmation.title"));
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
    testPage.selectFromDropdown("householdMemberRelationship", message("household-info.relationship.step-child"));
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
