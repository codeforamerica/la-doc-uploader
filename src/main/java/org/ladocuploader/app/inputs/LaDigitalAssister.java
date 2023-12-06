package org.ladocuploader.app.inputs;

import formflow.library.data.FlowInputs;
import formflow.library.data.annotations.Money;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class LaDigitalAssister extends FlowInputs {

  // Language
  private String languageRead;
  private String languageSpeak;
  private String needInterpreter;

  // Choose programs
  @NotEmpty(message="{error.missing-general}")
  private List<String> programs;

  // Who is Applying
  @NotBlank(message="{error.missing-general}")
  private String whosApplying;

  // Personal Information
  @NotBlank(message="{error.missing-firstname}")
  private String firstName;

  @NotBlank(message="{error.missing-lastname}")
  private String lastName;

  private String otherNames;

  private String birthDay;
  private String birthMonth;
  private String birthYear;

  @NotBlank(message="{error.missing-general}")
  private String sex;

  private String maritalStatus;

  private String highestEducation;

  // home address
  private String noHomeAddress;

  private String homeAddressStreetAddress1;

  private String homeAddressStreetAddress2;

  private String homeAddressCity;

  private String homeAddressState;

  private String homeAddressZipCode;

  //Mailing Address
  private String sameAsHomeAddress;

  private String mailingAddressStreetAddress1;

  private String mailingAddressStreetAddress2;

  private String mailingAddressCity;

  private String mailingAddressState;

  private String mailingAddressZipCode;

  //Contact Info
  private String phoneNumber;

  private String identifiesAsDeaf;

  private String preferredCommsMethod;

  private String emailAddress;

  private List<String> remindersMethod;

  // Household
  private String multiplePersonHousehold;

  private String householdMemberFirstName;

  private String householdMemberLastName;

  private String householdMemberOtherNames;

  private String householdMemberBirthDay;

  private String householdMemberBirthMonth;

  private String householdMemberBirthYear;

  private String householdMemberRelationship;

  private String householdMemberSex;

  private String householdMemberMaritalStatus;

  private String householdMemberHighestEducation;

  private List<String> ssns;

  private String schoolInd;

  private String students;

  private String pregnancyInd;

  private String pregnancies;

  private String outOfStateBenefitsInd;

  private String outOfStateBenefitsRecipients;

  private String receivedOutOfStateBenefits;

  // SNAP
  private String buyPrepareMealsSeparateIndicator;

  private String preparesFood;

  private String migrantOrSeasonalFarmWorkerInd;

  private String citizenshipInd;

  private String nonCitizens;

  private String citizenshipNumber;

  private String veteranInd;

  private String veterans;

  private String fosterInd;

  private String fosters;

  private String fosterAgedOutInd;

  private String fostersAgedOut;

  private String homelessInd;

  private String homeless;

  private String roomRentalInd;

  private String roomRentals;

  private String mealInd;

  private String meals;


  //  Sensitive Questions
  private String householdHasPersonalSituations;
  
  private String personalSituationsHouseholdUUID;
  
  private List<String> personalSituationsListed;
  
  private String householdHasDomesticViolenceSituation;
  
  private String householdHasCriminalJusticeSituation;
  
  private String personalSituationHomelessSwitch;
  
  private String personalSituationOtherIssue;
  
  private String personalSituationStrugglingDrugsAlcohol;
  
  private String personalSituationDomesticViolenceFlag;
  
  private String personalSituationDisability;

  // Income
  private String householdSearchingForJob;

  @NotEmpty(message="{error.missing-general}")
  private List<String> jobSearch;

  private String workDisqualificationInd;

  private String selfEmploymentIncome;

  @NotBlank(message="{error.missing-general}")
  private String householdMemberJobAdd;

  @NotBlank(message="{error.missing-general}")
  private String employerName;

  private String selfEmployed;

  private String jobPaidByHour;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String hourlyWage;

  @Range(message="{error.invalid-range}", min=1, max=100)
  @NotBlank(message="{error.missing-general}")
  private String hoursPerWeek;

  @NotBlank(message="{error.missing-pay-period}")
  private String payPeriod;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String payPeriodAmount;

  @NotEmpty
  private List<String> additionalIncome;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String moneyOnHand;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String monthlyHouseholdIncome;
  
  private String switchToIncomeByJob;

  @NotEmpty
  private List<String> householdHomeExpenses;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesRent;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesMortgage;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesHomeownerInsurance;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesPropertyTax;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesCondoFees;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesLotRental;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesFloodInsurance;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesOther;

  @NotEmpty
  private List<String> householdUtilitiesExpenses;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesHeating;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesCooling;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesElectricity;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesWater;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesPhone;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesGarbage;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesSewer;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesCookingFuel;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesOther;

  private String hasDependentCareExpenses;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesDependentCare;

  @NotEmpty
  private List<String> householdInsuranceExpenses;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesMedicalInsurance;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesDentalInsurance;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesVisionInsurance;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesMedicalBills;

  private String hasElderlyCareExpenses;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesElderlyCare;

  // Final Screen
  private String authorizedRepresentative;

  private String authorizedRepCommsAuthorization;

  private String authorizedRepMailAuthorization;

  private String authorizedRepSpendingAuthorization;

//  @NotBlank(message="{error.missing-firstname}")
  private String authorizedRepFirstName;

//  @NotBlank(message="{error.missing-lastname}")
  private String authorizedRepLastName;

  private String authorizedRepOtherNames;

  private String authorizedRepStreetAddress1;

  private String authorizedRepStreetAddress2;

  private String authorizedRepZipCode;

  private String authorizedRepState;

  private String authorizedRepCity;

  private String authorizedRepPhoneNumber;

  private String needsMedicaid;

  private String votingRegistrationRequested;

  private String votingRegistrationHelpRequested;

  private String permissionToAskAboutRace;

  private String ethnicitySelected;

  private String raceSelected;

  private String rightsAndResponsibilitiesAgree;

  private String noIncorrectInformationAgree;

  private String programsSharingDataAccessAgree;

  private String nonDiscriminationStatementAgree;

  private String signature;

  @NotBlank(message = "{final-confirmation.answer-feedback-question}")
  private String digitalAssisterFeedback;

  private String digitalAssisterFeedbackDetail;
}

