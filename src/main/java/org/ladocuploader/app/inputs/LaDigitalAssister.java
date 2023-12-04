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

  private String householdOtherNames;

  private String householdBirthDay;

  private String householdBirthMonth;

  private String householdBirthYear;

  private String householdRelationship;

  private String householdSex;

  private String householdMaritalStatus;

  private String householdHighestEducation;

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

  private String payPeriod;

  @Money
  private String payPeriodAmount;

  private List<String> additionalIncome;

  @Money
  private String moneyOnHand;

  private String monthlyHouseholdIncome;
  
  private String switchToIncomeByJob;

  private List<String> householdHomeExpenses;

  private String expensesRent;

  private String expensesMortgage;

  private String expensesHomeownerInsurance;

  private String expensesPropertyTax;

  private String expensesCondoFees;

  private String expensesLotRental;

  private String expensesFloodInsurance;

  private String expensesOther;

  private List<String> householdUtilitiesExpenses;

  private String expensesUtilitiesHeating;

  private String expensesUtilitiesCooling;

  private String expensesUtilitiesElectricity;

  private String expensesUtilitiesWater;

  private String expensesUtilitiesPhone;

  private String expensesUtilitiesGarbage;

  private String expensesUtilitiesSewer;

  private String expensesUtilitiesCookingFuel;

  private String expensesUtilitiesOther;

  private String hasDependentCareExpenses;

  private String expensesDependentCare;

  private List<String> householdInsuranceExpenses;

  private String expensesMedicalInsurance;

  private String expensesDentalInsurance;

  private String expensesVisionInsurance;

  private String expensesMedicalBills;

  private String hasElderlyCareExpenses;

  private String expensesElderlyCare;

  // Final Screen
  private String authorizedRepresentative;

  private String authorizedRepCommsAuthorization;

  private String authorizedRepMailAuthorization;

  private String authorizedRepSpendingAuthorization;

  private String authorizedRepFirstName;

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

