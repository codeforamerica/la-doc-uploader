package org.ladocuploader.app.inputs;

import formflow.library.data.FlowInputs;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

  private String goingToSchool;

  private String isPregnant;

  private String receivedOutOfStateBenefits;

  private String migrantOrSeasonalFarmWorkerInd;

  private String citizenshipInd;

  private String nonCitizens;

}

