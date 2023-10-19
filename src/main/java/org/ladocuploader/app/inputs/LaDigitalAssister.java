package org.ladocuploader.app.inputs;

import formflow.library.data.FlowInputs;
import formflow.library.data.validators.Money;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class LaDigitalAssister extends FlowInputs {

  // Language
  private String languageRead;
  private String languageSpeak;
  private String needInterpreter;

  // Choose programs
  private List<String> programs;
  @NotBlank
  private String whosApplying;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  private String highestEducation;

  private String maritalStatus;

  private String sex;

  private String otherNames;

  private String birthDate;

  private String phoneNumber;

  private String emailAddress;

  private String multiplePersonHousehold;

  private String householdMemberFirstName;

  private String householdMemberLastName;

  private String ssns;

  private String homeAddressZipCode;

  private String homeAddressState;

  private String homeAddressCity;

  private String noHomeAddress;

  private String homeAddressStreetAddress2;

  private String homeAddressStreetAddress1;

  private String mailingAddressZipCode;

  private String mailingAddressState;

  private String mailingAddressCity;

  private String mailingAddressStreetAddress2;

  private String mailingAddressStreetAddress1;

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

  @NotBlank
  private String payPeriod;

  @Money
  @NotBlank
  private String payPeriodAmount;

  private String additionalIncome;

  @Money
  private String moneyOnHand;
  
  private String monthlyHouseholdIncome;
  
  private String switchToIncomeByJob;

  private List<String> householdHomeExpenses;
  // Final Screen
  private String authorizedRepresentative;

  private String authorizedRepCommsAuthorization;

  private String authorizedRepMailAuthorization;

  private String authorizedRepSpendingAuthorization;

  @NotBlank
  private String authorizedRepFirstName;

  @NotBlank
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

  private String expensesRent;

  private String expensesMortgage;

  private String expensesHomeownerInsurance;

  private String expensesPropertyTax;

  private String expensesCondoFees;

  private String expensesLotRental;

  private String expensesFloodInsurance;

  private String expensesOther;

  private String rightsAndResponsibilitiesAgree;

  private String noIncorrectInformationAgree;

  private String programsSharingDataAccessAgree;

  private String nonDiscriminationStatementAgree;

  private String signature;

}

