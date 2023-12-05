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

  private String selfEmploymentIncome;

  @NotBlank
  private String householdMemberJobAdd;

  @NotBlank
  private String employerName;

  private String selfEmployed;

  private String jobPaidByHour;

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

  // Income
  private String householdSearchingForJob;

  private String jobSearch;

  private String workDisqualificationInd;

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


  @Money
  @NotBlank
  private String hourlyWage;

  @Range(min=1, max=100)
  @NotBlank
  private String hoursPerWeek;

//  @NotBlank(message="{error.missing-pay-period}")
  private String payPeriod;

  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String payPeriodAmount;

//  @NotEmpty
  private List<String> additionalIncome;

  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String moneyOnHand;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String monthlyHouseholdIncome;
  
  private String switchToIncomeByJob;

  private List<String> householdHomeExpenses;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesRent;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesMortgage;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesHomeownerInsurance;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesPropertyTax;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesCondoFees;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesLotRental;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesFloodInsurance;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesOther;

  private List<String> householdUtilitiesExpenses;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesHeating;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesCooling;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesElectricity;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesWater;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesPhone;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesGarbage;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesSewer;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesCookingFuel;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesUtilitiesOther;

  private String hasDependentCareExpenses;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesDependentCare;

//  @NotEmpty
  private List<String> householdInsuranceExpenses;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesMedicalInsurance;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesDentalInsurance;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesVisionInsurance;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesMedicalBills;

  private String hasElderlyCareExpenses;

//  @Money
//  @NotBlank(message="{error.missing-dollar-amount}")
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

