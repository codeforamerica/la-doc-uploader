package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByName;
import formflow.library.data.Submission;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.csv.converters.AddressStreetConverter;
import org.ladocuploader.app.csv.converters.AdultsProvidingSupportConverter;
import org.ladocuploader.app.csv.converters.PhoneNumberConverter;
import org.ladocuploader.app.csv.converters.UnmarriedMinorConverter;
import org.ladocuploader.app.utils.HouseholdUtilities;

@Getter
@Setter
@Slf4j
public class ECEApplicationCsvModel extends BaseCsvModel {

    @CsvBindByName(column="cfa_reference_id")
    private String id;
    //  *** no questions for this one *** //
    @CsvBindByName(column="Student Id {{student-id}}")
    private String studentId;
    //  *** no questions for this one *** //
    @CsvBindByName(column="School Id {{school-id}}")
    private String schoolId;

    //  *** no questions for this one *** //
    @CsvBindByName(column="School Rank {{school-rank}}")
    private String schoolRank;

    //  *** no questions for this one *** //
    @CsvBindByName(column="Status (InProgress/Submitted) {{status}}", required=true)
    private String status = "InProgress";  // requested default

    //  *** no questions for this one *** //
    @CsvBindByName(column="Hide Form from Parent (Yes/No) {{hide}}", required=true)
    private String hideApplication = "No"; // required field, requested default

    //  *** no questions for this one *** //
    @CsvBindByName(column="Admin Notes on application")
    private String adminNotes;

    //  *** no questions for this one *** //
    @CsvBindByName(column="Choose the grade your child is applying for")
    private String chooseStudentGrade;

    // *** no questions for this one *** //
    @CsvBindByName(column="Select the option that best describes where you live.")
    private String descriptionOfLivingEnv;

    // *** no questions for this one *** //
    @CsvBindByName(column="This questionnaire is intended to address the McKinney-Vento Act. Your child may be eligible for additional educational services.   Did the student receive McKinney Vento (Homeless) Services in a previous school district?")
    private String hadHomelessServicesInPreviousSchool;

    // Mapped to "noHomeAddress"
    @CsvBindByName(column="Is the student’s address a temporary living arrangement?")
    private String isStudentAddressTemporaryLiving;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is the temporary living arrangement due to loss of housing or economic hardship?")
    private String isTempLivingDueToLossOfHousingOrEconomicHardship;

    // TODO: check if there is a question for this one
    @CsvBindByName(column="Does the student have a disability or receive any special education-related services?")
    private String doesStudentHaveDisabilityOrSpecEdServices;

    // *** no questions for this one *** //
    @CsvBindByName(column="Where is the student currently living?")
    private String whereDoesStudentCurrentlyLive;

    // *** no questions for this one *** //
    @CsvBindByName(column="Other specific information about where the student is currently living:")
    private String specificsAboutWhereStudentCurrentlyLiving;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does the student exhibit any behaviors that may interfere with his or her academic performance?")
    private String doesStudentHaveBehaviorsThatAffectAcademics;

    // *** no questions for this one *** //
    @CsvBindByName(column="Would you like assistance with uniforms, student records, school supplies, transportation, other? (Describe):")
    private String needAssistanceWithSchoolRelatedThings;

    // *** no questions for this one *** //
    @CsvBindByName(column="Migrant – Have you moved at any time during the past three (3) years to seek temporary or seasonal work in agriculture (including Poultry processing, dairy, nursery, and timber) or fishing?")
    private String hasFamilyMovedForAgriWork;

    // filled out - however not sure if they all provide financial support? TODO: check if they have jobs?
    @CsvBindByName(column="How many people, including children, are in your household?  Only include all children, parents, guardians, and/or additional adults who provide financial support to the family.")
    private String howManyPeopleInHousehold;

    // mapped
    @CsvBindByName(column="What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.)")
    private String monthlyHouseholdIncome;

    // *** no questions for this one; don't have mapping either *** //
    @CsvBindByName(column="Select the option below that best describes your household:")
    private String householdDescription;

    @CsvBindByName(column="Provide 1 of 4 forms of verification documents listed below:")
    private String verificationDocumentOneType;

    @CsvBindByName(column="Verification document upload:")
    private String verificationDocumentOne;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is the child you are applying for a twin and/or triplet?")
    private String twinOrTriplet;

    // *** no questions for this one *** //
    @CsvBindByName(column="Please list the name(s) of the child’s twin/triplets.")
    private String twinOrTripletName;

    @CsvBindByName(column="Are you interested in taking a seat now (2023-24 school year)? The list of available programs is linked below. We’ll make offers as seats become available. There is limited availability.")
    private String takeASeatCurrentSchoolYear;

    @CsvBindByName(column="Upload your child's birth certificate. (For unborn children, provide a doctor’s note with the anticipated birth date. After birth, upload Official Birth certificate before enrollment.)")
    private String studentsBirthCertificateDocument;

    @CsvBindByName(column="Is your name on the birth certificate?")
    private String isParentNameOnBirthCertificate;

    @CsvBindByName(column="If your name is not on the birth certificate, then you will also need to provide proof of custody. If you are unable to provide one of these documents right now, you will need to provide proof of custody before your child receives a center/school placement.   Please select the document you are uploading.")
    private String custodyProofDocumentType;

    // *** no questions for this one *** //
    @CsvBindByName(column="Please upload documentation of proof of custody.")
    private String custodyProofDocument;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload the ID of the parent/guardian completing the application.")
    private String parentIdDocumentation;

    // TODO: build converter if we have adults providing financial support?
    @CsvCustomBindByName(column="List below each adult living in the household who provides financial support to the family, their age, and their relationship to the child applicant.  (Example: Mother - 35 YEARS, Father - 35 YEARS, Aunt - 24 YEARS, Grandmother - 68 YEARS)", converter= AdultsProvidingSupportConverter.class)
    private List<Map<String, Object>> household;

    // TODO: build converter for minor list
    @CsvBindByName(column="List below each minor living in the household, their age, and their relationship to the child applicant.  (Example: Child Applicant - 3 YEARS, Brother - 10 YEARS, Sister - 7 YEARS, Cousin - 7 YEARS)")
    List<String> minorsInHousehold;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload the birth certificate (state issued or foreign) or passport or visa or hospital record or state-issued ID for each minor listed as a sibling of the applicant child.  Upload one of these required documents for ALL dependent children listed in the household.")
    String siblingProofData;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload additional sibling birth certificates, if needed.")
    String siblingAdditionalProofData;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does your child have a sibling attending any of the centers/schools you ranked?")
    String siblingAtSchoolCenterRanked;

    // *** no questions for this one *** //
    @CsvBindByName(column="You can list up to 3 siblings. Sibling name #1:")
    String siblingOneName;

    // *** no questions for this one *** //
    @CsvBindByName(column="Which center/school does sibling #1 attend?")
    String siblingOneSchoolCenter;

    // *** no questions for this one *** //
    @CsvBindByName(column="Sibling name #2:")
    String siblingTwoName;

    // *** no questions for this one *** //
    @CsvBindByName(column="Which center/school does sibling #2 attend?")
    String siblingTwoSchoolCenter;

    // *** no questions for this one *** //
    @CsvBindByName(column="Sibling name #3:")
    String siblingThreeName;

    // *** no questions for this one *** //
    @CsvBindByName(column="Which center/school does sibling #3 attend?")
    String siblingThreeSchoolCenter;

    // *** no questions for this one *** //
    @CsvBindByName(column="Do you work at one of the centers/schools you ranked?")
    String doesApplicantWorksAtSchoolCenterTheyRanked;

    // *** no questions for this one *** //
    @CsvBindByName(column="Which center/school do you work at?")
    String applicantWorksAt;

    // TODO: come back to this and figure out how to get the setters working
    @CsvCustomBindByName(column="Is the parent applicant an unmarried minor (under age 18)?", converter=UnmarriedMinorConverter.class)
    private Map<String, String> isParentApplicantUnmarriedMinor = new HashMap<>();

    @JsonSetter(value="birthDay")
    private void setBirthDay(final String day) {
        try {
            if (day != null) {
                isParentApplicantUnmarriedMinor.put("day", day);
            }
        } catch (NumberFormatException e) {
            log.error("JSON Mapping: Unable to set member birth day, as value '{}' is bad.", day);
        }
    }

    @JsonSetter(value="birthMonth")
    private void setBirthMonth(final String month) {
        try {
            if (month != null) {
                isParentApplicantUnmarriedMinor.put("month", month);
            }
        } catch (NumberFormatException e) {
            log.error("JSON Mapping: Unable to set member birth month, as value '{}' is bad.", month);
        }
    }

    @JsonSetter(value="birthYear")
    private void setBirthYear(final String year) {
        try {
            if (year != null) {
                isParentApplicantUnmarriedMinor.put("year", year);
            }
        } catch (NumberFormatException e) {
            log.error("JSON Mapping: Unable to set member birth year, as value '{}' is bad.", year);
        }
    }

    @JsonSetter(value="maritalStatus")
    private void setMaritalStatus(final String maritalStatus) {
        if (maritalStatus != null) {
            isParentApplicantUnmarriedMinor.put("maritalStatus", maritalStatus);
        }
    }

    // *** no questions for this one *** //
    @CsvBindByName(column="Does your child have an Individualized Family Service Plans (IFSP), or are they being evaluated for special education services?")
    String doesChildHaveIFSPOrBeingEvaluated;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is your name listed on the residency documents that you will be providing?")
    private String isYourNameOrSpouseNameOnResidencyDocuments;

    // *** no questions for this one *** //
    @CsvBindByName(column="Proof of residency #1.")
    private String proofOfResidencyDocumentOne;

    // *** no questions for this one *** //
    @CsvBindByName(column="Verified residency document #1 type:")
    private String proofOfResidencyDocumentOneType;

    // *** no questions for this one *** //
    @CsvBindByName(column="Proof of residency #2.")
    private String proofOfResidencyDocumentTwo;

    // *** no questions for this one *** //
    @CsvBindByName(column="Verified residency document #2 type:")
    private String proofOfResidencyDocumentTwoType;

    // *** no questions for this one *** //
    @CsvBindByName(column="Either the parent/guardian name must be on the residency documents or if the parent/guardian lives with another adult who is named on the residency documents, the parent/guardian must upload a signed letter from the person named on the residency documents stating that the parent/guardian lives at that same address.   If uploading a letter, parent/guardian must also upload acceptable proofs of residency in resident’s name and a photo of the resident's ID.")
    String residencyNotice;


    // TODO: applicant is child or parent in this case (Adult 1)?
    @CsvBindByName(column="Is the applicant a child of a parent or guardian in active Military service?")
    String isChildsParentGuardianInMilitaryService;

    // TODO: check for employer name or self employed
    @CsvBindByName(column="Is Adult 1 (yourself) working?")
    String isAdultOneWorking;

    // mapped
    @CsvBindByName(column="Please select the gender that best matches your SNAP application choice:")
    private String sex;

    // mapped
    @CsvBindByName(column="Please select the ethnicity that best matches your SNAP application choice:")
    private String ethnicitySelected;

    // mapped
    @CsvBindByName(column="Please select the race that best matches your SNAP application choice:")
    private String raceSelected;

    // *** no questions for this one *** //
    @CsvBindByName(column="Pay statement upload #1 and #2 (dated within 45-60 days of filling out this application)")
    String adultOnePayStatementOneAndTwo;

    // *** no questions for this one *** //
    @CsvBindByName(column="Pay statement upload #3 and #4, if needed (dated within 45-60 days of filling out this application)")
    String adultOnePayStatementThreeAndFour;

    // *** no questions for this one *** //
    @CsvBindByName(column="Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed)")
    String adultOneEmployerLetter;

    // TODO: come back to this one
    @CsvBindByName(column="I state that my income or support comes from:")
    String adultOneIncomeSupportComesFrom;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload your most recent IRS Form 1099")
    String adultOneSelfEmployment1099;

    // *** no questions for this one *** //
    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support")
    String adultOneSupportFromParentFamilyStatementDocument;
    @CsvBindByName(column="Describe your source of income")
    String adultOneDescribeSourceOfIncome;
    @CsvBindByName(column="Gross Income January:")
    String adultOneGrossIncomeJanuary;
    @CsvBindByName(column="Gross Income February:")
    String adultOneGrossIncomeFebruary;
    @CsvBindByName(column="Gross Income March:")
    String adultOneGrossIncomeMarch;
    @CsvBindByName(column="Gross Income April:")
    String adultOneGrossIncomeApril;
    @CsvBindByName(column="Gross Income May:")
    String adultOneGrossIncomeMay;
    @CsvBindByName(column="Gross Income June:")
    String adultOneGrossIncomeJune;
    @CsvBindByName(column="Gross Income July:")
    String adultOneGrossIncomeJuly;
    @CsvBindByName(column="Gross Income August:")
    String adultOneGrossIncomeAugust;
    @CsvBindByName(column="Gross Income September:")
    String adultOneGrossIncomeSeptember;
    @CsvBindByName(column="Gross Income October:")
    String adultOneGrossIncomeOctober;
    @CsvBindByName(column="Gross Income November:")
    String adultOneGrossIncomeNovember;
    @CsvBindByName(column="Gross Income December:")
    String adultOneGrossIncomeDecember;

    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by:")
    String adultOneHouseholdThingsPaidForByIrregularIncome; // TODO assumption it means irregular, not sure

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission")
    String adultOneUnemploymentBenefitsDocumentation;

    // *** no questions for this one *** //
    @CsvBindByName(column="Enter the number of months you have been without income:")
    String adultOneNumberMonthsWithoutIncome;

    // *** no questions for this one *** //
    @CsvBindByName(column="I am (check all that apply)")
    String adultOneIAmOptions;

    // *** no questions for this one *** //
    @CsvBindByName(column="If 'Other' please describe your employment status")
    String adultOneIAmOtherEmploymentStatusDescription;

    // TODO: compute in generate model
    @CsvBindByName(column="Is Adult 1 (yourself) in school, in a training program, or seeking work?")
    String isAdultOneSchoolTrainingSeekingWork;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement")
    String adultOneHIRERegistrationProof;

    // *** no questions for this one *** //
    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar")
    String adultOneProofOfSchoolEnrollment;
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead")
    String adultOneHoursAttendingTrainingCoursesDocument;

    // TODO: compute in generate model
    @CsvBindByName(column="Is Adult 2 working?")
    String isAdultTwoWorking;

    // *** no questions for this one *** //
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 2) (dated within 45-60 days of filling out this application)")
    String adultTwoPayStatementOneAndTwo;
    @CsvBindByName(column="I state that Adult 2's income or support comes from:")
    String adultTwoIncomeSupportComesFrom;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload Adult 2's most recent IRS Form 1099")
    String adultTwoSelfEmployment1099;
    @CsvBindByName(column="Describe Adult 2's source of income")
    String adultTwoDescribeSourceOfIncome;

    // TODO: we have buy and prepare food but nothing around utilities and transportation
    @CsvBindByName(column="Adult 2's rent/house payments, utilities, food, and transportation expenses are being paid for by:")
    String adultTwoHouseholdThingsPaidForByRegularIncome;

    // *** no questions for this one *** //
    @CsvBindByName(column="Enter the number of months Adult 2 has been without income:")
    String adultTwoNumberMonthsWithoutIncome;

    // *** no questions for this one *** //
    @CsvBindByName(column="Adult 2 is (check all that apply)")
    String adultTwoIAmOptions;

    // *** no questions for this one *** //
    @CsvBindByName(column="If 'Other' please describe Adult 2's employment status")
    String adultTwoIAmOtherEmploymentStatusDescription;

    // TODO: compute schoolName || jobSearch
    @CsvBindByName(column="Is Adult 2 in school, in a training program, or seeking work?")
    String isAdultTwoSchoolTrainingSeekingWork;



    // TODO: check for a job added or self-employment and map to Yes/No?
    @CsvBindByName(column="Is Adult 3 working?")
    String isAdultThreeWorking;

    // *** no questions for this one *** //
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 3) (dated within 45-60 days of filling out this application)")
    String adultThreePayStatementOneAndTwo;

    // *** no questions for this one *** //
    @CsvBindByName(column="I state that Adult 3's income or support comes from:")
    String adultThreeIncomeSupportComesFrom;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload Adult 3's most recent IRS Form 1099")
    String adultThreeSelfEmployment1099;

    // TODO: self employment or employer name?
    @CsvBindByName(column="Describe Adult 3's source of income")
    String adultThreeDescribeSourceOfIncome;

    // TODO: we have a question asking who is sharing in payment - see if we can map that to this?
    @CsvBindByName(column="Adult 3's rent/house payments, utilities, food, and transportation expenses are being paid for by:")
    String adultThreeHouseholdThingsPaidForByIrregularIncome; // TODO assumption it means irregular, not sure

    // *** no questions for this one *** //
    @CsvBindByName(column="Enter the number of months Adult 3 has been without income:")
    String adultThreeNumberMonthsWithoutIncome;

    // *** no questions for this one *** //
    @CsvBindByName(column="Adult 3 is (check all that apply)")
    String adultThreeIAmOptions;

    // *** no questions for this one *** //
    @CsvBindByName(column="If 'Other' please describe Adult 3's employment status")
    String adultThreeIAmOtherEmploymentStatusDescription;

    // TODO: compute schoolName || jobSearch (same as Adult 2)
    @CsvBindByName(column="Is Adult 3 in school, in a training program, or seeking work?")
    String isAdultThreeSchoolTrainingSeekingWork;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does your child receive SSI Benefits?")
    String doesChildReceiveSSI;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload a statement from the Social Security Administration verifying that the child listed on the application is a recipient of SSI benefits.")
    String ssiVerificationDocument;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does your child receive Family Independence Temporary Assistance (FITAP) or Temporary Assistance to Needy Families (TANF) benefits?")
    String doesChildReceiveFITAPorTANF;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload proof of benefits.")
    String benefitsDocumentation;

    // TODO: we have guardiansHaveDisabilityInd but doesn't account for 70%. verify this
    @CsvBindByName(column="Does the parent/guardian receive Social Security Administration disability benefits, supplemental security income, or Veterans Administration disability benefits for a disability of at least 70 percent?")
    String doesParentGuardianReceiveSSDorSupplementalSecurityIncomeOrVADisability;

    // *** no questions for this one *** //
    @CsvBindByName(column="Are any adults included in your household count caring for any children with disabilities in the household?")
    String areAnyAdultsInHouseholdCaregiversForChildWithDisabilitiesInHousehold;

    // *** no questions for this one *** //
    @CsvBindByName(column="(Adult 1) Verified hours in school, training, or work")
    String adultOneVerifiedHoursInSchoolTrainingWork;

    // *** no questions for this one *** //
    @CsvBindByName(column="(Adult 2) Verified hours in school, training, or work")
    String adultTwoVerifiedHoursInSchoolTrainingWork;

    // *** no questions for this one *** //
    @CsvBindByName(column="(Adult 3) Verified hours in school, training, or work")
    String adultThreeVerifiedHoursInSchoolTrainingWork;

    // TODO: we don't have any verification but do have monthly income?
    @CsvBindByName(column="Verified income (Use only numbers, no words)")
    String verifiedIncome;

    // *** no questions for this one *** //
    @CsvBindByName(column="Which language did your child learn first?")
    String childsFirstLanguage;

    // *** no questions for this one *** //
    @CsvBindByName(column="Which language does your child use most often at home?")
    String childsPreferredLangAtHome;

    // *** no questions for this one *** //
    @CsvBindByName(column="In what language do you most often speak to your child?")
    String languageSpeakToChild;

    // mapped
    @CsvBindByName(column="Current phone number:")
    private String phoneNumber;

    // *** no questions for this one *** //
    @CsvBindByName(column="Do you want to receive text communication from NOLA Public Schools?")
    private String allowTextCommunicationFromNolaPS;

    // mapped
    @CsvBindByName(column="Current email address:")
    private String emailAddress;

    // *** no questions for this one *** //
    @CsvBindByName(column="A Gifted IEP is required for your child to attend Hynes Charter School. Do you have a Gifted and Talented evaluation or Gifted and Talented IEP approved by OPSB's Office of Child Search?")
    String hasGiftedIEP;

    // *** no questions for this one *** //
    @CsvBindByName(column="If yes, click the link below to request an administrative review")
    String requestingAdministrativeReview;

    // *** no questions for this one *** //
    @CsvBindByName(column="If no, click the link below to schedule an evaluation.")
    String requestingScheduleOfEvaluation;

    // *** no questions for this one *** //
    @CsvBindByName(column="Please rate your application experience on a scale of 1-5.")
    String applicationFeedbackExperienceRating;

    // *** no questions for this one *** //
    @CsvBindByName(column="Provide any additional feedback on your application experience below.")
    String applicationFeedbackAdditionalInfo;

    // *** no questions for this one *** //
    @CsvBindByName(column="Do you want to be contacted about jobs in early childhood? (either for yourself or someone you know)")
    String okayToContactAboutJobsInEarlyChildhood;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does the child have social service needs?")
    String doesChildHaveSocialServiceNeeds;

    // *** no questions for this one *** //
    @CsvBindByName(column="Have Headstart services been provided to this family in the past?")
    String hasFamilyHadHeadStartServicesPreviously;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does the parent participate in Parents As Educators Kingsley House program?")
    String doesParentParticipateKingslyHouseProgram;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is applicant a resident of Columbia Park in Gentilly?")
    String isApplicantResidentOfColumbiaParkGentilly;

    // *** no questions for this one *** //
    @CsvBindByName(column="Please select if any of the following intra-agency transfer requests apply")
    String interAgencyTransferRequests;

    // *** no questions for this one *** //
    @CsvBindByName(column="Transfer center:")
    String transferCenter;

    public static BaseCsvModel generateModel(Submission submission) throws JsonProcessingException {
        Map<String, Object> inputData = submission.getInputData();
        inputData.put("id", submission.getId());
        List<Map<String, Object>> householdList = (List)inputData.get("household");
        // TODO: also compute household member adult things here?
        // this is the data that jackson will map into the EceModel, not inputData
        Map<String, Object> eceDataMap = new HashMap<>();

        int numberOfAdultsInHousehold = 0;

        if (householdList != null){

            for (Map<String, Object> member : householdList) {
                int birthDay = 0;
                int birthMonth = 0;
                int birthYear = 0;
                boolean is18orOlder = false;

                try {
                    birthDay = Integer.parseInt((String)member.get("householdMemberBirthDay"));
                    birthMonth = Integer.parseInt((String)member.get("householdMemberBirthMonth"));
                    birthYear = Integer.parseInt((String)member.get("householdMemberBirthYear"));

                    is18orOlder = HouseholdUtilities.isMember18orOlder(birthDay, birthMonth, birthYear);
                } catch (NumberFormatException e) {
                    // TODO what to do if this does fail?? ignore and keep going? probably
                    log.error("Unable to work with household member {}'s birthday ({}/{}/{}): {}",
                            member.get("householdMemberFirstName"),
                            member.get("householdMemberBirthDay"),
                            member.get("householdMemberBirthMonth"),
                            member.get("householdMemberBirthYear"),
                            e.getMessage()
                    );
                }

                if (is18orOlder) {
                    // TODO: add/ compute adult 1, 2, and 3 questions here?
                    numberOfAdultsInHousehold++;
                }
            }
        }
        eceDataMap.put("numberOfAdultsInHousehold", numberOfAdultsInHousehold);



        ECEApplicationCsvModel eceApp = mapper.convertValue(eceDataMap, ECEApplicationCsvModel.class);
        eceApp.setSubmissionId(submission.getId());
        return eceApp;
    }
}
