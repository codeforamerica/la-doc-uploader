package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import formflow.library.data.Submission;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.csv.converters.AddressStreetConverter;
import org.ladocuploader.app.csv.converters.PhoneNumberConverter;
import org.ladocuploader.app.utils.HouseholdUtilities;

@Getter
@Setter
@Slf4j
public class ECEApplicationCsvModel extends BaseCsvModel {

    @CsvBindByName(column="cfa_reference_id", required=true)
    private String id;

    // they will populate this field, we put it in for convenience
    @CsvBindByName(column="Student Id {{student-id}}")
    private String studentId;

    // *** no questions for this one *** //
    @CsvBindByName(column="School Id {{school-id}}")
    private String schoolId;

    //  *** no questions for this one *** //
    @CsvBindByName(column="School Rank {{school-rank}}")
    private String schoolRank;

    //  AVELA required field
    @CsvBindByName(column="Status (InProgress/Submitted) {{status}}", required=true)
    private String status = "InProgress";  // requested default

    //  AVELA required field
    @CsvBindByName(column="Hide Form from Parent (Yes/No) {{hide}}", required=true)
    private String hideApplication = "No"; // required field, requested default

    @CsvBindByName(column="Admin notes on application")
    private String adminNotesOnApplication;

    @CsvBindByName(column="Choose the grade your child is applying for")
    private String gradeChildApplyingFor;

    @CsvBindByName(column="Select the option that best describes where you live.")
    private String descriptionOfLivingEnv;

    @CsvBindByName(column="This questionnaire is intended to address the McKinney-Vento Act. Your child may be eligible for additional educational services.   Did the student receive McKinney Vento (Homeless) Services in a previous school district?")
    private String hadHomelessServicesInPreviousSchool;

    @CsvBindByName(column="Is the student’s address a temporary living arrangement?")
    private String isStudentAddressTemporaryLiving;

    @CsvBindByName(column="Is the temporary living arrangement due to loss of housing or economic hardship?")
    private String isTempLivingDueToLossOfHousingOrEconomicHardship;

    @CsvBindByName(column="Does the student have a disability or receive any special education-related services?")
    private String doesStudentHaveDisabilityOrSpecEdServices;

    @CsvBindByName(column="Where is the student currently living?")
    private String whereDoesStudentCurrentlyLive;

    @CsvBindByName(column="Other specific information about where the student is currently living:")
    private String specificsAboutWhereStudentCurrentlyLiving;

    @CsvBindByName(column="Does the student exhibit any behaviors that may interfere with his or her academic performance?")
    private String doesStudentHaveBehaviorsThatAffectAcademics;

    @CsvBindByName(column="Would you like assistance with uniforms, student records, school supplies, transportation, other? (Describe):")
    private String needAssistanceWithSchoolRelatedThings;

    @CsvBindByName(column="Migrant – Have you moved at any time during the past three (3) years to seek temporary or seasonal work in agriculture (including Poultry processing, dairy, nursery, and timber) or fishing?")
    private String hasFamilyMovedForAgriWork;

    @CsvBindByName(column="How many people, including children, are in your household?  Only include all children, parents, guardians, and/or additional adults who provide financial support to the family.")
    private String howManyPeopleInHousehold;

    @CsvBindByName(column="What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.)")
    private String monthlyHouseholdIncome;

    @CsvBindByName(column="Select the option below that best describes your household:")
    private String householdDescription;

    @CsvBindByName(column="Provide 1 of 4 forms of verification documents listed below:")
    private String verificationDocumentOneType;

    @CsvBindByName(column="Verification document upload:")
    private String verificationDocumentOne;

    @CsvBindByName(column="Is the child you are applying for a twin and/or triplet?")
    private String isApplicantChildTwin;

    @CsvBindByName(column="Please list the name(s) of the child’s twin/triplets.")
    private String applicantsTwinTripletSiblingNames;

    @CsvBindByName(column="Are you interested in taking a seat now (2023-24 school year)? The list of available programs is linked below. We’ll make offers as seats become available. There is limited availability.")
    private String takeASeatCurrentSchoolYear;

    @CsvBindByName(column="Upload your child's birth certificate. (For unborn children, provide a doctor’s note with the anticipated birth date. After birth, upload Official Birth certificate before enrollment.)")
    private String studentsBirthCertificateDocument;

    @CsvBindByName(column="Is your name on the birth certificate?")
    private String isParentNameOnBirthCertificate;

    @CsvBindByName(column="If your name is not on the birth certificate, then you will also need to provide proof of custody. If you are unable to provide one of these documents right now, you will need to provide proof of custody before your child receives a center/school placement.   Please select the document you are uploading.")
    private String custodyProofDocumentType;

    @CsvBindByName(column="Please upload documentation of proof of custody.")
    private String custodyProofDocument;

    @CsvBindByName(column="Upload the ID of the parent/guardian completing the application.")
    private String applicantsID;

    @CsvBindByName(column="List below each adult living in the household who provides financial support to the family, their age, and their relationship to the child applicant.  (Example: Mother - 35 YEARS, Father - 35 YEARS, Aunt - 24 YEARS, Grandmother - 68 YEARS)")
    private String householdFamilyAndAge;

    @CsvBindByName(column="What is the number of minors (below 18 years old) living in the household, INCLUDING THE CHILD APPLICANT?")
    private String numberOfMinorsInHousehold;

    @CsvBindByName(column="List below each minor living in the household, their age, and their relationship to the child applicant.  (Example: Child Applicant - 3 YEARS, Brother - 10 YEARS, Sister - 7 YEARS, Cousin - 7 YEARS)")
    private String minorsInHousehold;

    @CsvBindByName(column="Upload the birth certificate (state issued or foreign) or passport or visa or hospital record or state-issued ID for each minor listed as a sibling of the applicant child.  Upload one of these required documents for ALL dependent children listed in the household.")
    private String siblingProofDocuments;

    @CsvBindByName(column="Upload additional sibling birth certificates, if needed.")
    private String siblingAdditionalProofDocuments;

    @CsvBindByName(column="Does your child have a sibling attending any of the centers/schools you ranked?")
    private String siblingAtSchoolCenterRanked;

    @CsvBindByName(column="You can list up to 3 siblings. Sibling name #1:")
    private String siblingOneName;

    @CsvBindByName(column="Which center/school does sibling #1 attend?")
    private String siblingOneSchoolCenter;

    @CsvBindByName(column="Sibling name #2:")
    private String siblingTwoName;

    @CsvBindByName(column="Which center/school does sibling #2 attend?")
    private String siblingTwoSchoolCenter;

    @CsvBindByName(column="Sibling name #3:")
    private String siblingThreeName;

    @CsvBindByName(column="Which center/school does sibling #3 attend?")
    private String siblingThreeSchoolCenter;

    @CsvBindByName(column="Do you work at one of the centers/schools you ranked?")
    private String doesApplicantWorksAtSchoolCenterTheyRanked;

    @CsvBindByName(column="Which center/school do you work at?")
    private String applicantWorksAt;

    @CsvBindByName(column="Is the parent applicant an unmarried minor (under age 18)?")
    private String isParentApplicantUnmarriedMinor;

    @CsvBindByName(column="Does your child have an Individualized Family Service Plans (IFSP), or are they being evaluated for special education services?")
    private String doesApplicantChildHaveIFSPOrEvaluationHappening;

    @CsvBindByName(column="Is your name listed on the residency documents that you will be providing?")
    private String isApplicantsNameOnResidencyDocuments;

    @CsvBindByName(column="Proof of residency #1.")
    private String proofOfResidencyDocumentOne;

    @CsvBindByName(column="Verified residency document #1 type:")
    private String proofOfResidencyDocumentOneType;
    @CsvBindByName(column="Proof of residency #2.")
    private String proofOfResidencyDocumentTwo;

    @CsvBindByName(column="Verified residency document #2 type:")
    private String proofOfResidencyDocumentTwoType;

    @CsvBindByName(column="Either the parent/guardian name must be on the residency documents or if the parent/guardian lives with another adult who is named on the residency documents, the parent/guardian must upload a signed letter from the person named on the residency documents stating that the parent/guardian lives at that same address.   If uploading a letter, parent/guardian must also upload acceptable proofs of residency in resident’s name and a photo of the resident's ID.")
    private String residencyNoticeParentId;

    @CsvBindByName(column="Is the applicant a child of a parent or guardian in active Military service?")
    private String isChildsParentGuardianInMilitaryService;

    /* ---- START Adult 1 (applicant)  ---- */
    @CsvBindByName(column="Is Adult 1 (yourself) working?")
    private String isAdultOneWorking;

    @CsvBindByName(column="Please select the gender that best matches your SNAP application choice:")
    private String adultOneGender;

    @CsvBindByName(column="Please select the ethnicity that best matches your SNAP application choice:")
    private String adultOneEthnicity;

    @CsvBindByName(column="Please select the race that best matches your SNAP application choice:")
    private String adultOneRace;

    @CsvBindByName(column="Pay statement upload #1 and #2 (dated within 45-60 days of filling out this application)")
    private String adultOnePayStatementOneAndTwo;

    @CsvBindByName(column="Pay statement upload #3 and #4, if needed (dated within 45-60 days of filling out this application)")
    private String adultOnePayStatementThreeAndFour;

    @CsvBindByName(column="Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed)")
    private String adultOneEmployerLetter;

    @CsvBindByName(column="I state that my income or support comes from:")
    private String adultOneIncomeSupportComesFrom;

    @CsvBindByName(column="Upload your most recent IRS Form 1099")
    private String adultOneSelfEmployment1099;

    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support")
    private String adultOneSupportFromParentFamilyStatementDocument;

    @CsvBindByName(column="Describe your source of income")
    private String adultOneDescribeSourceOfIncome;
    @CsvBindByName(column="Gross Income January:")
    private String adultOneGrossIncomeJanuary;
    @CsvBindByName(column="Gross Income February:")
    private String adultOneGrossIncomeFebruary;
    @CsvBindByName(column="Gross Income March:")
    private String adultOneGrossIncomeMarch;
    @CsvBindByName(column="Gross Income April:")
    private String adultOneGrossIncomeApril;
    @CsvBindByName(column="Gross Income May:")
    private String adultOneGrossIncomeMay;
    @CsvBindByName(column="Gross Income June:")
    private String adultOneGrossIncomeJune;
    @CsvBindByName(column="Gross Income July:")
    private String adultOneGrossIncomeJuly;
    @CsvBindByName(column="Gross Income August:")
    private String adultOneGrossIncomeAugust;
    @CsvBindByName(column="Gross Income September:")
    private String adultOneGrossIncomeSeptember;
    @CsvBindByName(column="Gross Income October:")
    private String adultOneGrossIncomeOctober;
    @CsvBindByName(column="Gross Income November:")
    private String adultOneGrossIncomeNovember;
    @CsvBindByName(column="Gross Income December:")
    private String adultOneGrossIncomeDecember;
    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by:")
    private String adultOneHouseholdThingsPaidForBy;
    @CsvBindByName(column="Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission")
    private String adultOneUnemploymentBenefitsDocumentation;
    @CsvBindByName(column="Enter the number of months you have been without income:")
    private String adultOneNumberMonthsWithoutIncome;
    @CsvBindByName(column="I am (check all that apply)")
    private String adultOneIAmOptions;
    @CsvBindByName(column="If 'Other' please describe your employment status")
    private String adultOneIAmOtherEmploymentStatusDescription;
    @CsvBindByName(column="Is Adult 1 (yourself) in school, in a training program, or seeking work?")
    private String isAdultOneSchoolTrainingSeekingWork;
    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement")
    private String adultOneHIRERegistrationProof;
    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar")
    private String adultOneProofOfSchoolEnrollment;
    // they ask this question twice, not sure why.
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead")
    private String adultOneHoursAttendingTrainingCoursesDocument;
    @CsvBindByName(column="Provide hours attending and training courses (or hours worked) on organization’s letterhead")
    private String adultOneHoursAttendingTrainingCoursesDocument2;

    /* ---- START Adult 2  ---- */
    @CsvBindByName(column="Is Adult 2 working?")
    private String isAdultTwoWorking;
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 2) (dated within 45-60 days of filling out this application)")
    private String adultTwoPayStatementOneAndTwo;
    @CsvBindByName(column="I state that Adult 2's income or support comes from:")
    private String adultTwoIncomeSupportComesFrom;
    @CsvBindByName(column="Upload Adult 2's most recent IRS Form 1099")
    private String adultTwoSelfEmployment1099;
    @CsvBindByName(column="Describe Adult 2's source of income")
    private String adultTwoDescribeSourceOfIncome;
    @CsvBindByName(column="Adult 2's rent/house payments, utilities, food, and transportation expenses are being paid for by:")
    private String adultTwoHouseholdThingsPaidForBy;
    @CsvBindByName(column="Enter the number of months Adult 2 has been without income:")
    private String adultTwoNumberMonthsWithoutIncome;
    @CsvBindByName(column="Adult 2 is (check all that apply)")
    private String adultTwoIAmOptions;
    @CsvBindByName(column="If 'Other' please describe Adult 2's employment status")
    private String adultTwoIAmOtherEmploymentStatusDescription;
    @CsvBindByName(column="Is Adult 2 in school, in a training program, or seeking work?")
    private String isAdultTwoSchoolTrainingSeekingWork;

    /* ---- START Adult 3  ---- */
    @CsvBindByName(column="Is Adult 3 working?")
    private String isAdultThreeWorking;
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 3) (dated within 45-60 days of filling out this application)")
    private String adultThreePayStatementOneAndTwo;
    @CsvBindByName(column="I state that Adult 3's income or support comes from:")
    private String adultThreeIncomeSupportComesFrom;
    @CsvBindByName(column="Upload Adult 3's most recent IRS Form 1099")
    private String adultThreeSelfEmployment1099;
    @CsvBindByName(column="Describe Adult 3's source of income")
    private String adultThreeDescribeSourceOfIncome;
    @CsvBindByName(column="Adult 3's rent/house payments, utilities, food, and transportation expenses are being paid for by:")
    private String adultThreeHouseholdThingsPaidForBy;
    @CsvBindByName(column="Enter the number of months Adult 3 has been without income:")
    private String adultThreeNumberMonthsWithoutIncome;
    @CsvBindByName(column="Adult 3 is (check all that apply)")
    private String adultThreeIAmOptions;
    @CsvBindByName(column="If 'Other' please describe Adult 3's employment status")
    private String adultThreeIAmOtherEmploymentStatusDescription;
    @CsvBindByName(column="Is Adult 3 in school, in a training program, or seeking work?")
    private String isAdultThreeSchoolTrainingSeekingWork;

    /* ---- End adults in household ---- */

    @CsvBindByName(column="Does your child receive SSI Benefits?")
    private String doesChildReceiveSSI;
    @CsvBindByName(column="Upload a statement from the Social Security Administration verifying that the child listed on the application is a recipient of SSI benefits.")
    private String ssiVerificationDocument;
    @CsvBindByName(column="Does your child receive Family Independence Temporary Assistance (FITAP) or Temporary Assistance to Needy Families (TANF) benefits?")
    private String doesChildReceiveFITAPorTANF;
    @CsvBindByName(column="Upload proof of benefits.")
    private String benefitsDocumentation;
    @CsvBindByName(column="Does the parent/guardian receive Social Security Administration disability benefits, supplemental security income, or Veterans Administration disability benefits for a disability of at least 70 percent?")
    private String doesParentGuardianReceiveSSDorSupplementalSecurityIncomeOrVADisability;
    @CsvBindByName(column="Are any adults included in your household count caring for any children with disabilities in the household?")
    private String areAnyAdultsInHouseholdCaregiversForChildWithDisabilitiesInHousehold;
    @CsvBindByName(column="(Adult 1) Verified hours in school, training, or work")
    private String adultOneVerifiedHoursSchoolTrainingWork;
    @CsvBindByName(column="(Adult 2) Verified hours in school, training, or work")
    private String adultTwoVerifiedHoursSchoolTrainingWork;
    @CsvBindByName(column="(Adult 3) Verified hours in school, training, or work")
    private String adultThreeVerifiedHoursSchoolTrainingWork;
    @CsvBindByName(column="Verified income (Use only numbers, no words)")
    private String verifiedIncomeNumber;
    @CsvBindByName(column="Which language did your child learn first?")
    private String childsFirstLanguage;
    @CsvBindByName(column="Which language does your child use most often at home?")
    private String childsPreferredLangAtHome;
    @CsvBindByName(column="In what language do you most often speak to your child?")
    private String applicantLanguageToChild;
    @CsvBindByName(column="Current phone number:")
    private String phoneNumber;
    @CsvBindByName(column="Do you want to receive text communication from NOLA Public Schools?")
    private String allowTextCommunicationFromNolaPS;
    @CsvBindByName(column="Current email address:")
    private String emailAddress;
    @CsvBindByName(column="A Gifted IEP is required for your child to attend Hynes Charter School. Do you have a Gifted and Talented evaluation or Gifted and Talented IEP approved by OPSB's Office of Child Search?")
    private String hasGiftedIEP;
    @CsvBindByName(column="If yes, click the link below to request an administrative review")
    private String requestingAdministrativeReview;
    @CsvBindByName(column="If no, click the link below to schedule an evaluation.")
    private String requestingScheduleOfEvaluation;
    @CsvBindByName(column="Please rate your application experience on a scale of 1-5.")
    private String applicationFeedbackExperienceRating;
    @CsvBindByName(column="Provide any additional feedback on your application experience below.")
    private String applicationFeedbackAdditionalInfo;
    @CsvBindByName(column="Do you want to be contacted about jobs in early childhood? (either for yourself or someone you know)")
    private String okayToContactAboutJobsInEarlyChildhood;
    @CsvBindByName(column="Does the child have social service needs?")
    private String doesChildHaveSocialServiceNeeds;
    @CsvBindByName(column="Have Headstart services been provided to this family in the past?")
    private String hasFamilyHadHeadStartServicesPreviously;
    @CsvBindByName(column="Does the parent participate in Parents As Educators Kingsley House program?")
    private String doesParentParticipateKingslyHouseProgram;
    @CsvBindByName(column="Is applicant a resident of Columbia Park in Gentilly?")
    private String isApplicantResidentOfColumbiaParkGentilly;
    @CsvBindByName(column="Please select if any of the following intra-agency transfer requests apply")
    private String interAgencyTransferRequests;
    @CsvBindByName(column="Transfer center:")
    private String transferCenter;


    public static BaseCsvModel generateModel(Submission submission) throws JsonProcessingException {
        Map<String, Object> inputData = submission.getInputData();
        inputData.put("id", submission.getId());

        List<Map<String, Object>> householdList = (List)inputData.get("household");

        // this is the data that jackson will map into the EceModel, not inputData
        Map<String, Object> eceDataMap = new HashMap<>();

        int numberOfAdultsInHousehold = 0;

        for (Map<String, Object> member : householdList) {
            int birthDay = 0;
            int birthMonth = 0;
            int birthYear = 0;
            boolean is18orOlder = false;
            boolean is5orYounger = false;

            try {
                birthDay = Integer.parseInt((String)member.get("householdMemberBirthDay"));
                birthMonth = Integer.parseInt((String)member.get("householdMemberBirthMonth"));
                birthYear = Integer.parseInt((String)member.get("householdMemberBirthYear"));

                is18orOlder = HouseholdUtilities.isMember18orOlder(birthDay, birthMonth, birthYear);
                is5orYounger = HouseholdUtilities.isMember5orYounger(birthDay, birthMonth, birthYear);
            } catch (NumberFormatException e) {
                // TODO what to do if this does fail?? ignore and keep going? probably
                log.error("Unable to work with household member {}'s birthday ({}/{}/{}): {}",
                    member.get("householdMemberFirstName"),
                    (String)member.get("householdMemberBirthDay"),
                    (String)member.get("householdMemberBirthMonth"),
                    (String)member.get("householdMemberBirthYear"),
                    e.getMessage()
                );
            }

            if (is18orOlder) {
                numberOfAdultsInHousehold++;
            }
        }
        eceDataMap.put("numberOfAdultsInHousehold", numberOfAdultsInHousehold);



        ECEApplicationCsvModel eceApp = mapper.convertValue(eceDataMap, ECEApplicationCsvModel.class);
        eceApp.setSubmissionId(submission.getId());
        return eceApp;

/*
 // Parish (WIC/ECE)
  @NotEmpty(message="{error.missing-general}")
  private String parish;

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

  @Pattern(regexp = "^\\(\\d{3}\\) \\d{3}-\\d{4}$", message="{error.invalid-phone}")
  private String cellPhoneNumber;

  @Pattern(regexp = "^\\(\\d{3}\\) \\d{3}-\\d{4}$", message="{error.invalid-phone}")
  private String workPhoneNumber;

  private String wantsReminders;

  private String identifiesAsDeaf;

  private String preferredCommsMethod;

  private String emailAddress;

  private List<String> remindersMethod;

  // Household
  private String multiplePersonHousehold;

  @NotBlank(message="{error.missing-firstname}")
  private String householdMemberFirstName;

  @NotBlank(message="{error.missing-lastname}")
  private String householdMemberLastName;

  private String householdMemberOtherNames;

  private String householdMemberBirthDay;

  private String householdMemberBirthMonth;

  private String householdMemberBirthYear;

  @NotBlank(message="{error.missing-general}")
  private String householdMemberRelationship;

  @NotBlank(message="{error.missing-general}")
  private String householdMemberSex;

  private String householdMemberMaritalStatus;

  private String householdMemberHighestEducation;

  @Size(min=11, max=11, message="{error.invalid-ssn}")
  private String ssn;

  @Size(min=11, max=11, message="{error.invalid-ssn}")
  @DynamicField
  private String householdMemberSsn;

  private String schoolInd;

  @NotEmpty(message="{error.missing-general}")
  private List<String> students;

  @NotBlank(message="{error.missing-general}")
  @DynamicField
  private String schoolName;

  @NotBlank(message="{error.missing-general}")
  @DynamicField
  private String schoolEnrollmentLevel;

  private String pregnancyInd;

  @NotEmpty(message="{error.missing-general}")
  private List<String> pregnancies;

//  With dynamic fields and the date template, the data is stored as pregnancyDueDate<wildcard><uuid>Day so changed the ordering to start with the date piece
  @NotBlank(message="{error.missing-general}")
  @DynamicField
  private String dayPregnancyDueDate;

  @NotBlank(message="{error.missing-general}")
  @DynamicField
  private String monthPregnancyDueDate;

  @NotBlank(message="{error.missing-general}")
  @Pattern(regexp = "^(2024|2025)$", message="{error.invalid-dob}")
  @DynamicField
  private String yearPregnancyDueDate;

  private String outOfStateBenefitsInd;

  private String outOfStateBenefitsRecipients;

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

  @NotEmpty(message="{error.missing-general}")
  @DynamicField
  private List<String> personalSituations;

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

  @NotEmpty(message="{error.missing-general}")
  private List<String> additionalIncome;

  @NotEmpty
  private List<String> moneyOnHandTypes;

  @DynamicField
  private String moneyOnHandOwner;

  @Money
  @DynamicField
  private String moneyOnHandValue;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String monthlyHouseholdIncome;

  private String switchToIncomeByJob;

  @NotEmpty(message="{error.missing-general}")
  private List<String> householdHomeExpenses;

  @NotBlank(message="{error.missing-dollar-amount}")
  @Money(message="{error.invalid-money}")
  @DynamicField
  private String householdHomeExpenseAmount;

  @NotEmpty(message="{error.missing-general}")
  private List<String> householdUtilitiesExpenses;

  @NotBlank(message="{error.missing-dollar-amount}")
  @Money(message="{error.invalid-money}")
  @DynamicField
  private String householdUtilitiesExpenseAmount;

  private String receivesEnergyAssistance;

  private String assistanceThroughLiheap;

  private String hasDependentCareExpenses;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesDependentCare;

  private String hasChildSupportExpenses;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesChildSupport;

  @NotEmpty(message="{error.missing-general}")
  private List<String> householdMedicalExpenses;

  @NotBlank(message="{error.missing-dollar-amount}")
  @Money(message="{error.invalid-money}")
  @DynamicField
  private String householdMedicalExpenseAmount;

  private String hasElderlyCareExpenses;

  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expensesElderlyCare;

  // WIC / ECE
  private String interestedInEceInd;
  private String interestedInWicInd;
  private String adultsWorking;
  private String guardiansHaveDisabilityInd;

  // Final Screen
  private String needsNewEbtCard;

  private String authorizedRepresentative;

  private String authorizedRepCommsAuthorization;

  private String authorizedRepMailAuthorization;

  private String authorizedRepSpendingAuthorization;

  @NotBlank(message="{error.missing-firstname}")
  private String authorizedRepFirstName;

  @NotBlank(message="{error.missing-lastname}")
  private String authorizedRepLastName;

  private String authorizedRepOtherNames;

  @NotBlank(message="{error.missing-general}")
  private String authorizedRepStreetAddress1;

  private String authorizedRepStreetAddress2;

  @NotBlank(message="{error.missing-general}")
  private String authorizedRepCity;

  @NotBlank(message="{error.missing-general}")
  private String authorizedRepState;

  @NotBlank(message="{error.missing-general}")
  @Pattern(regexp = "\\d{5}", message = "{error.format-zip}")
  private String authorizedRepZipCode;

  @Pattern(regexp = "^\\(\\d{3}\\) \\d{3}-\\d{4}$", message="{error.invalid-phone}")
  private String authorizedRepPhoneNumber;

  private String needsMedicaid;

  @NotBlank(message="{error.missing-general}")
  private String votingRegistrationRequested;

  @NotBlank(message="{error.missing-general}")
  private String votingRegistrationHelpRequested;

  private String permissionToAskAboutRace;

  private String ethnicitySelected;

  @DynamicField
  private String householdMemberEthnicity;

  private List<String> raceSelected;

  @DynamicField
  private List<String> householdMemberRace;

  @NotEmpty(message="{error.missing-checkbox}")
  private List<String> rightsAndResponsibilitiesAgree;

  @NotEmpty(message="{error.missing-checkbox}")
  private List<String> noIncorrectInformationAgree;

  @NotEmpty(message="{error.missing-checkbox}")
  private List<String> programsSharingDataAccessAgree;

  @NotEmpty(message="{error.missing-checkbox}")
  private List<String> nonDiscriminationStatementAgree;

  @NotBlank(message="{error.missing-general}")
  private String signature;

  @NotBlank(message = "{final-confirmation.answer-feedback-question}")
  private String digitalAssisterFeedback;

  private String digitalAssisterFeedbackDetail;

  // Expedited Snap Start
  private String isApplyingForExpeditedSnap;

  // Household 30 Day Income
  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String householdIncomeLast30Days;

  // Household Money on Hand
  private String householdMoneyOnHand;

  // Expedited Money on Hand Amount
  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String expeditedMoneyOnHandAmount;

  // Household Rent
  private String householdPaysRent;

  // Household Rent Amount
  @Money(message="{error.invalid-money}")
  @NotBlank(message="{error.missing-dollar-amount}")
  private String householdRentAmount;

  private String addDocuments;

  @NotBlank(message = "{doc-type.select-a-type}")
  @DynamicField
  private String docType;
 */
    }
}
