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
import org.ladocuploader.app.csv.converters.PhoneNumberConverter;
import org.ladocuploader.app.utils.HouseholdUtilities;

@Getter
@Setter
@Slf4j
public class ECEApplicationCsvModel extends BaseCsvModel {

    @CsvBindByName(column="cfa_reference_id", required=true)
    @CsvBindByPosition(position=0)
    private String id;
    //  *** no questions for this one *** //
    @CsvBindByName(column="Student Id {{student-id}}")
    @CsvBindByPosition(position=1)
    private String studentId;
    //  *** no questions for this one *** //
    @CsvBindByName(column="School Id {{school-id}}")
    @CsvBindByPosition(position=2)
    private String schoolId;

    @CsvBindByName(column="School Rank {{school-rank}}")
    @CsvBindByPosition(position=3)
    private String schoolRank;

    @CsvBindByName(column="Status (InProgress/Submitted) {{status}}", required=true)
    @CsvBindByPosition(position=4)
    private String status = "InProgress";  // requested default

    //  *** no questions for this one *** //
    @CsvBindByName(column="Hide Form from Parent (Yes/No) {{hide}}", required=true)
    @CsvBindByPosition(position=5)
    private String hideApplication = "No"; // required field, requested default

    //  *** no questions for this one *** //
    @CsvBindByName(column="Admin Notes on application")
    @CsvBindByPosition(position=6)
    private String adminNotes;

    //  *** no questions for this one *** //
    @CsvBindByName(column="Choose the grade your child is applying for")
    @CsvBindByPosition(position=7)
    private String chooseStudentGrade;

    // *** no questions for this one *** //
    @CsvBindByName(column="Select the option that best describes where you live.")
    @CsvBindByPosition(position=8)
    private String descriptionOfLivingEnv;

    // *** no questions for this one *** //
    @CsvBindByName(column="This questionnaire is intended to address the McKinney-Vento Act. Your child may be eligible for additional educational services.   Did the student receive McKinney Vento (Homeless) Services in a previous school district?")
    @CsvBindByPosition(position=9)
    private String hadHomelessServicesInPreviousSchool;

    // Mapped to "noHomeAddress"
    @CsvBindByName(column="Is the student’s address a temporary living arrangement?")
    @CsvBindByPosition(position=10)
    private String isStudentAddressTemporaryLiving;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is the temporary living arrangement due to loss of housing or economic hardship?")
    @CsvBindByPosition(position=11)
    private String isTempLivingDueToLossOfHousingOrEconomicHardship;

    // TODO: check if there is a question for this one
    @CsvBindByName(column="Does the student have a disability or receive any special education-related services?")
    @CsvBindByPosition(position=12)
    private String doesStudentHaveDisabilityOrSpecEdServices;

    // *** no questions for this one *** //
    @CsvBindByName(column="Where is the student currently living?")
    @CsvBindByPosition(position=13)
    private String whereDoesStudentCurrentlyLive;

    // *** no questions for this one *** //
    @CsvBindByName(column="Other specific information about where the student is currently living:")
    @CsvBindByPosition(position=14)
    private String specificsAboutWhereStudentCurrentlyLiving;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does the student exhibit any behaviors that may interfere with his or her academic performance?")
    @CsvBindByPosition(position=15)
    private String doesStudentHaveBehaviorsThatAffectAcademics;

    // *** no questions for this one *** //
    @CsvBindByName(column="Would you like assistance with uniforms, student records, school supplies, transportation, other? (Describe):")
    @CsvBindByPosition(position=16)
    private String needAssistanceWithSchoolRelatedThings;

    // *** no questions for this one *** //
    @CsvBindByName(column="Migrant – Have you moved at any time during the past three (3) years to seek temporary or seasonal work in agriculture (including Poultry processing, dairy, nursery, and timber) or fishing?")
    @CsvBindByPosition(position=17)
    private String hasFamilyMovedForAgriWork;

    // filled out
    @CsvBindByName(column="How many people, including children, are in your household?  Only include all children, parents, guardians, and/or additional adults who provide financial support to the family.")
    @CsvBindByPosition(position=18)
    private String howManyPeopleInHousehold;

    // filled out
    @CsvBindByName(column="What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.)")
    @CsvBindByPosition(position=19)
    private String monthlyHouseholdIncome;

    // *** no questions for this one; don't have mapping either *** //
    @CsvBindByName(column="Select the option below that best describes your household:")
    @CsvBindByPosition(position=20)
    private String householdDescription;

    @CsvBindByName(column="Provide 1 of 4 forms of verification documents listed below:")
    @CsvBindByPosition(position=21)
    private String verificationDocumentOneType;

    @CsvBindByName(column="Verification document upload:")
    @CsvBindByPosition(position=22)
    private String verificationDocumentOne;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is the child you are applying for a twin and/or triplet?")
    @CsvBindByPosition(position=23)
    private String twinOrTriplet;

    // *** no questions for this one *** //
    @CsvBindByName(column="Please list the name(s) of the child’s twin/triplets.")
    @CsvBindByPosition(position=23)
    private String twinOrTripletName;

    @CsvBindByName(column="Are you interested in taking a seat now (2023-24 school year)? The list of available programs is linked below. We’ll make offers as seats become available. There is limited availability.")
    @CsvBindByPosition(position=24)
    private String takeASeatCurrentSchoolYear;

    @CsvBindByName(column="Upload your child's birth certificate. (For unborn children, provide a doctor’s note with the anticipated birth date. After birth, upload Official Birth certificate before enrollment.)")
    @CsvBindByPosition(position=25)
    private String studentsBirthCertificateDocument;

    @CsvBindByName(column="Is your name on the birth certificate?")
    @CsvBindByPosition(position=26)
    private String isParentNameOnBirthCertificate;

    @CsvBindByName(column="If your name is not on the birth certificate, then you will also need to provide proof of custody. If you are unable to provide one of these documents right now, you will need to provide proof of custody before your child receives a center/school placement.   Please select the document you are uploading.")
    @CsvBindByPosition(position=27)
    private String custodyProofDocumentType;

    // *** no questions for this one *** //
    @CsvBindByName(column="Please upload documentation of proof of custody.")
    @CsvBindByPosition(position=28)
    private String custodyProofDocument;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload the ID of the parent/guardian completing the application.")
    @CsvBindByPosition(position=29)
    private String parentIdDocumentation;

    // TODO: build converter
    @CsvBindByName(column="List below each adult living in the household who provides financial support to the family, their age, and their relationship to the child applicant.  (Example: Mother - 35 YEARS, Father - 35 YEARS, Aunt - 24 YEARS, Grandmother - 68 YEARS)")
    @CsvBindByPosition(position=30)
    List<String> adultsProvidingFinancialSupport;

    // TODO: build converter
    @CsvBindByName(column="List below each minor living in the household, their age, and their relationship to the child applicant.  (Example: Child Applicant - 3 YEARS, Brother - 10 YEARS, Sister - 7 YEARS, Cousin - 7 YEARS)")
    @CsvBindByPosition(position=31)
    List<String> minorsInHousehold;

    @CsvBindByName(column="Upload the birth certificate (state issued or foreign) or passport or visa or hospital record or state-issued ID for each minor listed as a sibling of the applicant child.  Upload one of these required documents for ALL dependent children listed in the household.")
    @CsvBindByPosition(position=32)
    String siblingProofData;

    @CsvBindByName(column="Upload additional sibling birth certificates, if needed.")
    @CsvBindByPosition(position=33)
    String siblingAdditionalProofData;

    @CsvBindByName(column="Does your child have a sibling attending any of the centers/schools you ranked?")
    @CsvBindByPosition(position=34)
    String siblingAtSchoolCenterRanked;
    @CsvBindByName(column="You can list up to 3 siblings. Sibling name #1:")
    @CsvBindByPosition(position=35)
    String siblingOneName;
    @CsvBindByName(column="Which center/school does sibling #1 attend?")
    @CsvBindByPosition(position=36)
    String siblingOneSchoolCenter;
    @CsvBindByName(column="Sibling name #2:")
    @CsvBindByPosition(position=37)
    String siblingTwoName;
    @CsvBindByName(column="Which center/school does sibling #2 attend?")
    @CsvBindByPosition(position=38)
    String siblingTwoSchoolCenter;
    @CsvBindByName(column="Sibling name #3:")
    @CsvBindByPosition(position=39)
    String siblingThreeName;
    @CsvBindByName(column="Which center/school does sibling #3 attend?")
    @CsvBindByPosition(position=40)
    String siblingThreeSchoolCenter;

    @CsvBindByName(column="Do you work at one of the centers/schools you ranked?")
    @CsvBindByPosition(position=41)
    String doesApplicantWorksAtSchoolCenterTheyRanked;
    @CsvBindByName(column="Which center/school do you work at?")
    @CsvBindByPosition(position=42)
    String applicantWorksAt;

    // TODO: see if we can calculate this
    @CsvBindByName(column="Is the parent applicant an unmarried minor (under age 18)?")
    @CsvBindByPosition(position=43)
    String isParentApplicantUnmarriedMinor;

    @CsvBindByName(column="Does your child have an Individualized Family Service Plans (IFSP), or are they being evaluated for special education services?")
    @CsvBindByPosition(position=44)
    String doesChildHaveIFSPOrBeingEvaluated;

    @CsvBindByName(column="Is your name listed on the residency documents that you will be providing?")
    @CsvBindByPosition(position=45)
    private String isYourNameOrSpouseNameOnResidencyDocuments;

    // *** no questions for this one *** //
    @CsvBindByName(column="Proof of residency #1.")
    @CsvBindByPosition(position=46)
    private String proofOfResidencyDocumentOne;

    // *** no questions for this one *** //
    @CsvBindByName(column="Verified residency document #1 type:")
    @CsvBindByPosition(position=47)
    private String proofOfResidencyDocumentOneType;

    // *** no questions for this one *** //
    @CsvBindByName(column="Proof of residency #2.")
    @CsvBindByPosition(position=48)
    private String proofOfResidencyDocumentTwo;

    // *** no questions for this one *** //
    @CsvBindByName(column="Verified residency document #2 type:")
    @CsvBindByPosition(position=49)
    private String proofOfResidencyDocumentTwoType;

    @CsvBindByName(column="Either the parent/guardian name must be on the residency documents or if the parent/guardian lives with another adult who is named on the residency documents, the parent/guardian must upload a signed letter from the person named on the residency documents stating that the parent/guardian lives at that same address.   If uploading a letter, parent/guardian must also upload acceptable proofs of residency in resident’s name and a photo of the resident's ID.")
    @CsvBindByPosition(position=50)
    String residencyNotice;

    @CsvBindByName(column="Is the applicant a child of a parent or guardian in active Military service?")
    @CsvBindByPosition(position=51)
    String isChildsParentGuardianInMilitaryService;

    @CsvBindByName(column="Is Adult 1 (yourself) working?")
    @CsvBindByPosition(position=52)
    String isAdultOneWorking;

    @CsvBindByName(column="Please select the gender that best matches your SNAP application choice:")
    @CsvBindByPosition(position=53)
    private String applicantGender;

    @CsvBindByName(column="Please select the ethnicity that best matches your SNAP application choice:")
    @CsvBindByPosition(position=54)
    private String applicantEthnicity;

    // TODO: map this to applicant answers?
    @CsvBindByName(column="Please select the race that best matches your SNAP application choice:")
    @CsvBindByPosition(position=55)
    private String snapApplicationChoice;

    @CsvBindByName(column="Pay statement upload #1 and #2 (dated within 45-60 days of filling out this application)")
    @CsvBindByPosition(position=56)
    String adultOnePayStatementOneAndTwo;
    @CsvBindByName(column="Pay statement upload #3 and #4, if needed (dated within 45-60 days of filling out this application)")
    @CsvBindByPosition(position=57)
    String adultOnePayStatementThreeAndFour;
    @CsvBindByName(column="Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed)")
    @CsvBindByPosition(position=58)
    String adultOneEmployerLetter;

    @CsvBindByName(column="I state that my income or support comes from:")
    @CsvBindByPosition(position=59)
    String adultOneIncomeSupportComesFrom;

    @CsvBindByName(column="Upload your most recent IRS Form 1099")
    @CsvBindByPosition(position=60)
    String adultOneSelfEmployment1099;
    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support")
    @CsvBindByPosition(position=61)
    String adultOneSupportFromParentFamilyStatementDocument;
    @CsvBindByName(column="Describe your source of income")
    @CsvBindByPosition(position=62)
    String adultOneDescribeSourceOfIncome;
    @CsvBindByName(column="Gross Income January:")
    @CsvBindByPosition(position=63)
    String adultOneGrossIncomeJanuary;
    @CsvBindByName(column="Gross Income February:")
    @CsvBindByPosition(position=64)
    String adultOneGrossIncomeFebruary;
    @CsvBindByName(column="Gross Income March:")
    @CsvBindByPosition(position=65)
    String adultOneGrossIncomeMarch;
    @CsvBindByName(column="Gross Income April:")
    @CsvBindByPosition(position=66)
    String adultOneGrossIncomeApril;
    @CsvBindByName(column="Gross Income May:")
    @CsvBindByPosition(position=67)
    String adultOneGrossIncomeMay;
    @CsvBindByName(column="Gross Income June:")
    @CsvBindByPosition(position=68)
    String adultOneGrossIncomeJune;
    @CsvBindByName(column="Gross Income July:")
    @CsvBindByPosition(position=69)
    String adultOneGrossIncomeJuly;
    @CsvBindByName(column="Gross Income August:")
    @CsvBindByPosition(position=70)
    String adultOneGrossIncomeAugust;
    @CsvBindByName(column="Gross Income September:")
    @CsvBindByPosition(position=71)
    String adultOneGrossIncomeSeptember;
    @CsvBindByName(column="Gross Income October:")
    @CsvBindByPosition(position=72)
    String adultOneGrossIncomeOctober;
    @CsvBindByName(column="Gross Income November:")
    @CsvBindByPosition(position=73)
    String adultOneGrossIncomeNovember;
    @CsvBindByName(column="Gross Income December:")
    @CsvBindByPosition(position=74)
    String adultOneGrossIncomeDecember;

    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by:")
    @CsvBindByPosition(position=75)
    String adultOneHouseholdThingsPaidForByIrregularIncome; // TODO assumption it means irregular, not sure

    @CsvBindByName(column="Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission")
    @CsvBindByPosition(position=76)
    String adultOneUnemploymentBenefitsDocumentation;

    @CsvBindByName(column="Enter the number of months you have been without income:")
    @CsvBindByPosition(position=77)
    String adultOneNumberMonthsWithoutIncome;
    @CsvBindByName(column="I am (check all that apply)")
    @CsvBindByPosition(position=78)
    String adultOneIAmOptions;

    @CsvBindByName(column="If 'Other' please describe your employment status")
    @CsvBindByPosition(position=79)
    String adultOneIAmOtherEmploymentStatusDescription;
    @CsvBindByName(column="Is Adult 1 (yourself) in school, in a training program, or seeking work?")
    @CsvBindByPosition(position=80)
    String isAdultOneSchoolTrainingSeekingWork;

    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement")
    @CsvBindByPosition(position=81)
    String adultOneHIRERegistrationProof;
    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar")
    @CsvBindByPosition(position=82)
    String adultOneProofOfSchoolEnrollment;
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead")
    @CsvBindByPosition(position=83)
    String adultOneHoursAttendingTrainingCoursesDocument;

    @CsvBindByName(column="Is Adult 2 working?")
    @CsvBindByPosition(position=84)
    String isAdultTwoWorking;
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 2) (dated within 45-60 days of filling out this application)")
    @CsvBindByPosition(position=85)
    String adultTwoPayStatementOneAndTwo;
    @CsvBindByName(column="I state that Adult 2's income or support comes from:")
    @CsvBindByPosition(position=86)
    String adultTwoIncomeSupportComesFrom;
    @CsvBindByName(column="Upload Adult 2's most recent IRS Form 1099")
    @CsvBindByPosition(position=87)
    String adultTwoSelfEmployment1099;
    @CsvBindByName(column="Describe Adult 2's source of income")
    @CsvBindByPosition(position=88)
    String adultTwoDescribeSourceOfIncome;

    @CsvBindByName(column="Adult 2's rent/house payments, utilities, food, and transportation expenses are being paid for by:")
    @CsvBindByPosition(position=89)
    String adultTwoHouseholdThingsPaidForByRegularIncome;

    @CsvBindByName(column="Enter the number of months Adult 2 has been without income:")
    @CsvBindByPosition(position=90)
    String adultTwoNumberMonthsWithoutIncome;

    @CsvBindByName(column="Adult 2 is (check all that apply)")
    @CsvBindByPosition(position=91)
    String adultTwoIAmOptions;

    @CsvBindByName(column="If 'Other' please describe Adult 2's employment status")
    @CsvBindByPosition(position=92)
    String adultTwoIAmOtherEmploymentStatusDescription;

    @CsvBindByName(column="Is Adult 2 in school, in a training program, or seeking work?")
    @CsvBindByPosition(position=93)
    String isAdultTwoSchoolTrainingSeekingWork;

    @CsvBindByName(column="Is Adult 3 working?")
    @CsvBindByPosition(position=94)
    String isAdultThreeWorking;
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 3) (dated within 45-60 days of filling out this application)")
    @CsvBindByPosition(position=95)
    String adultThreePayStatementOneAndTwo;
    @CsvBindByName(column="I state that Adult 3's income or support comes from:")
    @CsvBindByPosition(position=96)
    String adultThreeIncomeSupportComesFrom;
    @CsvBindByName(column="Upload Adult 3's most recent IRS Form 1099")
    @CsvBindByPosition(position=97)
    String adultThreeSelfEmployment1099;

    @CsvBindByName(column="Describe Adult 3's source of income")
    @CsvBindByPosition(position=98)
    String adultThreeDescribeSourceOfIncome;

    @CsvBindByName(column="Adult 3's rent/house payments, utilities, food, and transportation expenses are being paid for by:")
    @CsvBindByPosition(position=99)
    String adultThreeHouseholdThingsPaidForByIrregularIncome; // TODO assumption it means irregular, not sure

    @CsvBindByName(column="Enter the number of months Adult 3 has been without income:")
    @CsvBindByPosition(position=100)
    String adultThreeNumberMonthsWithoutIncome;

    @CsvBindByName(column="Adult 3 is (check all that apply)")
    @CsvBindByPosition(position=101)
    String adultThreeIAmOptions;

    @CsvBindByName(column="If 'Other' please describe Adult 3's employment status")
    @CsvBindByPosition(position=102)
    String adultThreeIAmOtherEmploymentStatusDescription;

    @CsvBindByName(column="Is Adult 3 in school, in a training program, or seeking work?")
    @CsvBindByPosition(position=103)
    String isAdultThreeSchoolTrainingSeekingWork;

    @CsvBindByName(column="Does your child receive SSI Benefits?")
    @CsvBindByPosition(position=104)
    String doesChildReceiveSSI;

    @CsvBindByName(column="Upload a statement from the Social Security Administration verifying that the child listed on the application is a recipient of SSI benefits.")
    @CsvBindByPosition(position=105)
    String ssiVerificationDocument;

    @CsvBindByName(column="Does your child receive Family Independence Temporary Assistance (FITAP) or Temporary Assistance to Needy Families (TANF) benefits?")
    @CsvBindByPosition(position=106)
    String doesChildReceiveFITAPorTANF;

    @CsvBindByName(column="Upload proof of benefits.")
    @CsvBindByPosition(position=107)
    String benefitsDocumentation;

    @CsvBindByName(column="Does the parent/guardian receive Social Security Administration disability benefits, supplemental security income, or Veterans Administration disability benefits for a disability of at least 70 percent?")
    @CsvBindByPosition(position=108)
    String doesParentGuardianReceiveSSDorSupplementalSecurityIncomeOrVADisability;

    @CsvBindByName(column="Are any adults included in your household count caring for any children with disabilities in the household?")
    @CsvBindByPosition(position=109)
    String areAnyAdultsInHouseholdCaregiversForChildWithDisabilitiesInHousehold;

    @CsvBindByName(column="(Adult 1) Verified hours in school, training, or work")
    @CsvBindByPosition(position=110)
    String adultOneVerifiedHoursInSchoolTrainingWork;

    @CsvBindByName(column="(Adult 2) Verified hours in school, training, or work")
    @CsvBindByPosition(position=111)
    String adultTwoVerifiedHoursInSchoolTrainingWork;

    @CsvBindByName(column="(Adult 3) Verified hours in school, training, or work")
    @CsvBindByPosition(position=112)
    String adultThreeVerifiedHoursInSchoolTrainingWork;

    @CsvBindByName(column="Verified income (Use only numbers, no words)")
    @CsvBindByPosition(position=113)
    String verifiedIncome;

    @CsvBindByName(column="Which language did your child learn first?")
    @CsvBindByPosition(position=114)
    String childsFirstLanguage;

    @CsvBindByName(column="Which language does your child use most often at home?")
    @CsvBindByPosition(position=115)
    String childsPreferredLangAtHome;

    @CsvBindByName(column="In what language do you most often speak to your child?")
    @CsvBindByPosition(position=116)
    String languageSpeakToChild;

    @CsvBindByName(column="Current phone number:")
    @CsvBindByPosition(position=117)
    private String phoneNumber;

    @CsvBindByName(column="Do you want to receive text communication from NOLA Public Schools?")
    @CsvBindByPosition(position=118)
    private String allowTextCommunicationFromNolaPS;

    @CsvBindByName(column="Current email address:")
    @CsvBindByPosition(position=119)
    private String emailAddress;

    @CsvBindByName(column="A Gifted IEP is required for your child to attend Hynes Charter School. Do you have a Gifted and Talented evaluation or Gifted and Talented IEP approved by OPSB's Office of Child Search?")
    @CsvBindByPosition(position=120)
    String hasGiftedIEP;

    @CsvBindByName(column="If yes, click the link below to request an administrative review")
    @CsvBindByPosition(position=121)
    String requestingAdministrativeReview;

    @CsvBindByName(column="If no, click the link below to schedule an evaluation.")
    @CsvBindByPosition(position=122)
    String requestingScheduleOfEvaluation;

    @CsvBindByName(column="Please rate your application experience on a scale of 1-5.")
    @CsvBindByPosition(position=123)
    String applicationFeedbackExperienceRating;

    @CsvBindByName(column="Provide any additional feedback on your application experience below.")
    @CsvBindByPosition(position=124)
    String applicationFeedbackAdditionalInfo;

    @CsvBindByName(column="Do you want to be contacted about jobs in early childhood? (either for yourself or someone you know)")
    @CsvBindByPosition(position=125)
    String okayToContactAboutJobsInEarlyChildhood;

    @CsvBindByName(column="Does the child have social service needs?")
    @CsvBindByPosition(position=126)
    String doesChildHaveSocialServiceNeeds;

    @CsvBindByName(column="Have Headstart services been provided to this family in the past?")
    @CsvBindByPosition(position=127)
    String hasFamilyHadHeadStartServicesPreviously;

    @CsvBindByName(column="Does the parent participate in Parents As Educators Kingsley House program?")
    @CsvBindByPosition(position=128)
    String doesParentParticipateKingslyHouseProgram;

    @CsvBindByName(column="Is applicant a resident of Columbia Park in Gentilly?")
    @CsvBindByPosition(position=129)
    String isApplicantResidentOfColumbiaParkGentilly;

    @CsvBindByName(column="Please select if any of the following intra-agency transfer requests apply")
    @CsvBindByPosition(position=130)
    String interAgencyTransferRequests;

    @CsvBindByName(column="Transfer center:")
    @CsvBindByPosition(position=131)
    String transferCenter;

    public static BaseCsvModel generateModel(Submission submission) throws JsonProcessingException {
        Map<String, Object> inputData = submission.getInputData();
        inputData.put("id", submission.getId());
        List<Map<String, Object>> householdList = (List)inputData.get("household");

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
        }
        eceDataMap.put("numberOfAdultsInHousehold", numberOfAdultsInHousehold);



        ECEApplicationCsvModel eceApp = mapper.convertValue(eceDataMap, ECEApplicationCsvModel.class);
        eceApp.setSubmissionId(submission.getId());
        return eceApp;
    }
}
