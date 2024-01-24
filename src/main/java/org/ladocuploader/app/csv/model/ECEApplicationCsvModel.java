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
    //  *** no questions for this one *** //
    @CsvBindByName(column="Student Id {{student-id}}")
    private String studentId;
    //  *** no questions for this one *** //
    @CsvBindByName(column="School Id {{school-id}}")
    private String schoolId;

    @CsvBindByName(column="School Rank {{school-rank}}")
    private String schoolRank;

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

    // filled out
    @CsvBindByName(column="How many people, including children, are in your household?  Only include all children, parents, guardians, and/or additional adults who provide financial support to the family.")
    private String howManyPeopleInHousehold;

    // filled out
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

    @CsvBindByName(column="Upload the ID of the parent/guardian completing the application.")
    private String parentIdDocumentation;

    // TODO: build converter
    @CsvBindByName(column="List below each adult living in the household who provides financial support to the family, their age, and their relationship to the child applicant.  (Example: Mother - 35 YEARS, Father - 35 YEARS, Aunt - 24 YEARS, Grandmother - 68 YEARS)")
    List<String> adultsProvidingFinancialSupport;

    // TODO: see if we can calculate this
    @CsvBindByName(column="List below each minor living in the household, their age, and their relationship to the child applicant.  (Example: Child Applicant - 3 YEARS, Brother - 10 YEARS, Sister - 7 YEARS, Cousin - 7 YEARS)")
    List<String> minorsInHousehold;

    @CsvBindByName(column="Upload the birth certificate (state issued or foreign) or passport or visa or hospital record or state-issued ID for each minor listed as a sibling of the applicant child.  Upload one of these required documents for ALL dependent children listed in the household.")
    String siblingProofData;

    @CsvBindByName(column="Upload additional sibling birth certificates, if needed.")
    String siblingAdditionalProofData;

    @CsvBindByName(column="Does your child have a sibling attending any of the centers/schools you ranked?")
    String siblingAtSchoolCenterRanked;
    @CsvBindByName(column="You can list up to 3 siblings. Sibling name #1:")
    String siblingOneName;
    @CsvBindByName(column="Which center/school does sibling #1 attend?")
    String siblingOneSchoolCenter;
    @CsvBindByName(column="Sibling name #2:")
    String siblingTwoName;
    @CsvBindByName(column="Which center/school does sibling #2 attend?")
    String siblingTwoSchoolCenter;
    @CsvBindByName(column="Sibling name #3:")
    String siblingThreeName;
    @CsvBindByName(column="Which center/school does sibling #3 attend?")
    String siblingThreeSchoolCenter;

    @CsvBindByName(column="Do you work at one of the centers/schools you ranked?")
    String doesApplicantWorksAtSchoolCenterTheyRanked;
    @CsvBindByName(column="Which center/school do you work at?")
    String applicantWorksAt;

    // TODO: see if we can calculate this
    @CsvBindByName(column="Is the parent applicant an unmarried minor (under age 18)?")
    String isParentApplicantUnmarriedMinor;

    @CsvBindByName(column="Does your child have an Individualized Family Service Plans (IFSP), or are they being evaluated for special education services?")
    String doesChildHaveIFSPOrBeingEvaluated;

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

    @CsvBindByName(column="Either the parent/guardian name must be on the residency documents or if the parent/guardian lives with another adult who is named on the residency documents, the parent/guardian must upload a signed letter from the person named on the residency documents stating that the parent/guardian lives at that same address.   If uploading a letter, parent/guardian must also upload acceptable proofs of residency in resident’s name and a photo of the resident's ID.")
    String residencyNotice;

    @CsvBindByName(column="Is the applicant a child of a parent or guardian in active Military service?")
    String isChildsParentGuardianInMilitaryService;

    @CsvBindByName(column="Is Adult 1 (yourself) working?")
    String isAdultOneWorking;

    @CsvBindByName(column="Please select the gender that best matches your SNAP application choice:")
    private String applicantGender;

    @CsvBindByName(column="Please select the ethnicity that best matches your SNAP application choice:")
    private String applicantEthnicity;

    // TODO: what does this mean
    @CsvBindByName(column="Please select the race that best matches your SNAP application choice:")
    private String snapApplicationChoice;

    @CsvBindByName(column="Pay statement upload #1 and #2 (dated within 45-60 days of filling out this application)")
    String adultOnePayStatementOneAndTwo;
    @CsvBindByName(column="Pay statement upload #3 and #4, if needed (dated within 45-60 days of filling out this application)")
    String adultOnePayStatementThreeAndFour;
    @CsvBindByName(column="Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed)")
    String adultOneEmployerLetter;

    @CsvBindByName(column="I state that my income or support comes from:")
    String adultOneIncomeSupportComesFrom;

    @CsvBindByName(column="Upload your most recent IRS Form 1099")
    String adultOneSelfEmployment1099;
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

    @CsvBindByName(column="Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission")
    String adultOneUnemploymentBenefitsDocumentation;

    @CsvBindByName(column="Enter the number of months you have been without income:")
    String adultOneNumberMonthsWithoutIncome;
    @CsvBindByName(column="I am (check all that apply)")
    String adultOneIAmOptions;

    @CsvBindByName(column="If 'Other' please describe your employment status")
    String adultOneIAmOtherEmploymentStatusDescription;
    @CsvBindByName(column="Is Adult 1 (yourself) in school, in a training program, or seeking work?")
    String isAdultOneSchoolTrainingSeekingWork;

    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement")
    String adultOneHIRERegistrationProof;
    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar")
    String adultOneProofOfSchoolEnrollment;
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead")
    String adultOneHoursAttendingTrainingCoursesDocument;

    @CsvBindByName(column="Is Adult 2 working?")
    String isAdultTwoWorking;
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 2) (dated within 45-60 days of filling out this application)")
    String adultTwoPayStatementOneAndTwo;
    @CsvBindByName(column="I state that Adult 2's income or support comes from:")
    String adultTwoIncomeSupportComesFrom;
    @CsvBindByName(column="Upload Adult 2's most recent IRS Form 1099")
    String adultTwoSelfEmployment1099;
    @CsvBindByName(column="Describe Adult 2's source of income")
    String adultTwoDescribeSourceOfIncome;

    @CsvBindByName(column="Adult 2's rent/house payments, utilities, food, and transportation expenses are being paid for by:")
    String adultTwoHouseholdThingsPaidForByRegularIncome;

    @CsvBindByName(column="Enter the number of months Adult 2 has been without income:")
    String adultTwoNumberMonthsWithoutIncome;

    @CsvBindByName(column="Adult 2 is (check all that apply)")
    String adultTwoIAmOptions;

    @CsvBindByName(column="If 'Other' please describe Adult 2's employment status")
    String adultTwoIAmOtherEmploymentStatusDescription;

    @CsvBindByName(column="Is Adult 2 in school, in a training program, or seeking work?")
    String isAdultTwoSchoolTrainingSeekingWork;

    @CsvBindByName(column="Is Adult 3 working?")
    String isAdultThreeWorking;
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 3) (dated within 45-60 days of filling out this application)")
    String adultThreePayStatementOneAndTwo;
    @CsvBindByName(column="I state that Adult 3's income or support comes from:")
    String adultThreeIncomeSupportComesFrom;
    @CsvBindByName(column="Upload Adult 3's most recent IRS Form 1099")
    String adultThreeSelfEmployment1099;

    @CsvBindByName(column="Describe Adult 3's source of income")
    String adultThreeDescribeSourceOfIncome;

    @CsvBindByName(column="Adult 3's rent/house payments, utilities, food, and transportation expenses are being paid for by:")
    String adultThreeHouseholdThingsPaidForByIrregularIncome; // TODO assumption it means irregular, not sure

    @CsvBindByName(column="Enter the number of months Adult 3 has been without income:")
    String adultThreeNumberMonthsWithoutIncome;

    @CsvBindByName(column="Adult 3 is (check all that apply)")
    String adultThreeIAmOptions;

    @CsvBindByName(column="If 'Other' please describe Adult 3's employment status")
    String adultThreeIAmOtherEmploymentStatusDescription;

    @CsvBindByName(column="Is Adult 3 in school, in a training program, or seeking work?")
    String isAdultThreeSchoolTrainingSeekingWork;

    @CsvBindByName(column="Does your child receive SSI Benefits?")
    String doesChildReceiveSSI;

    @CsvBindByName(column="Upload a statement from the Social Security Administration verifying that the child listed on the application is a recipient of SSI benefits.")
    String ssiVerificationDocument;

    @CsvBindByName(column="Does your child receive Family Independence Temporary Assistance (FITAP) or Temporary Assistance to Needy Families (TANF) benefits?")
    String doesChildReceiveFITAPorTANF;

    @CsvBindByName(column="Upload proof of benefits.")
    String benefitsDocumentation;

    @CsvBindByName(column="Does the parent/guardian receive Social Security Administration disability benefits, supplemental security income, or Veterans Administration disability benefits for a disability of at least 70 percent?")
    String doesParentGuardianReceiveSSDorSupplementalSecurityIncomeOrVADisability;

    @CsvBindByName(column="Are any adults included in your household count caring for any children with disabilities in the household?")
    String areAnyAdultsInHouseholdCaregiversForChildWithDisabilitiesInHousehold;

    @CsvBindByName(column="(Adult 1) Verified hours in school, training, or work")
    String adultOneVerifiedHoursInSchoolTrainingWork;

    @CsvBindByName(column="(Adult 2) Verified hours in school, training, or work")
    String adultTwoVerifiedHoursInSchoolTrainingWork;

    @CsvBindByName(column="(Adult 3) Verified hours in school, training, or work")
    String adultThreeVerifiedHoursInSchoolTrainingWork;

    @CsvBindByName(column="Verified income (Use only numbers, no words)")
    String verifiedIncome;

    @CsvBindByName(column="Which language did your child learn first?")
    String childsFirstLanguage;

    @CsvBindByName(column="Which language does your child use most often at home?")
    String childsPreferredLangAtHome;

    @CsvBindByName(column="In what language do you most often speak to your child?")
    String languageSpeakToChild;

    @CsvBindByName(column="Current phone number:")
    private String phoneNumber;

    @CsvBindByName(column="Do you want to receive text communication from NOLA Public Schools?")
    private String allowTextCommunicationFromNolaPS;

    @CsvBindByName(column="Current email address:")
    private String emailAddress;

    @CsvBindByName(column="A Gifted IEP is required for your child to attend Hynes Charter School. Do you have a Gifted and Talented evaluation or Gifted and Talented IEP approved by OPSB's Office of Child Search?")
    String hasGiftedIEP;

    @CsvBindByName(column="If yes, click the link below to request an administrative review")
    String requestingAdministrativeReview;

    @CsvBindByName(column="If no, click the link below to schedule an evaluation.")
    String requestingScheduleOfEvaluation;

    @CsvBindByName(column="Please rate your application experience on a scale of 1-5.")
    String applicationFeedbackExperienceRating;

    @CsvBindByName(column="Provide any additional feedback on your application experience below.")
    String applicationFeedbackAdditionalInfo;

    @CsvBindByName(column="Do you want to be contacted about jobs in early childhood? (either for yourself or someone you know)")
    String okayToContactAboutJobsInEarlyChildhood;

    @CsvBindByName(column="Does the child have social service needs?")
    String doesChildHaveSocialServiceNeeds;

    @CsvBindByName(column="Have Headstart services been provided to this family in the past?")
    String hasFamilyHadHeadStartServicesPreviously;

    @CsvBindByName(column="Does the parent participate in Parents As Educators Kingsley House program?")
    String doesParentParticipateKingslyHouseProgram;

    @CsvBindByName(column="Is applicant a resident of Columbia Park in Gentilly?")
    String isApplicantResidentOfColumbiaParkGentilly;

    @CsvBindByName(column="Please select if any of the following intra-agency transfer requests apply")
    String interAgencyTransferRequests;

    @CsvBindByName(column="Transfer center:")
    String transferCenter;

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

            try {
                birthDay = Integer.parseInt((String)member.get("householdMemberBirthDay"));
                birthMonth = Integer.parseInt((String)member.get("householdMemberBirthMonth"));
                birthYear = Integer.parseInt((String)member.get("householdMemberBirthYear"));

                is18orOlder = HouseholdUtilities.isMember18orOlder(birthDay, birthMonth, birthYear);
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
    }
}
