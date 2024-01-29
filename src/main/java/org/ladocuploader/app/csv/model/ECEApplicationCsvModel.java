package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import formflow.library.data.Submission;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.csv.CsvBindByNameOrder;
import org.ladocuploader.app.csv.converters.UnmarriedMinorConverter;
import org.ladocuploader.app.utils.HouseholdUtilities;
import org.ladocuploader.app.utils.IncomeCalculator;

import javax.print.DocFlavor;

@Getter
@Setter
@Slf4j
@CsvBindByNameOrder({
        "cfa_reference_id",
        "Student Id {{student-id}}",
        "School Id {{school-id}}",
        "School Rank {{school-rank}}",
        "Status (InProgress/Submitted) {{status}}",
        "Hide Form from Parent (Yes/No) {{hide}}",
        "Admin notes on application",
        "Choose the grade your child is applying for",
        "Select the option that best describes where you live.",
        "This questionnaire is intended to address the McKinney-Vento Act. Your child may be eligible for additional educational services.   Did the student receive McKinney Vento (Homeless) Services in a previous school district?",
        "Is the student’s address a temporary living arrangement?",
        "Is the temporary living arrangement due to loss of housing or economic hardship?",
        "Does the student have a disability or receive any special education-related services?",
        "Where is the student currently living?",
        "Other specific information about where the student is currently living:",
        "Does the student exhibit any behaviors that may interfere with his or her academic performance?",
        "Would you like assistance with uniforms, student records, school supplies, transportation, other? (Describe):",
        "Migrant – Have you moved at any time during the past three (3) years to seek temporary or seasonal work in agriculture (including Poultry processing, dairy, nursery, and timber) or fishing?",
        "How many people, including children, are in your household?  Only include all children, parents, guardians, and/or additional adults who provide financial support to the family.",
        "What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.)",
        "Select the option below that best describes your household:",
        "Provide 1 of 4 forms of verification documents listed below:",
        "Verification document upload:",
        "Is the child you are applying for a twin and/or triplet?",
        "Please list the name(s) of the child’s twin/triplets.",
        "Are you interested in taking a seat now (2023-24 school year)? The list of available programs is linked below. We’ll make offers as seats become available. There is limited availability.",
        "Upload your child's birth certificate. (For unborn children, provide a doctor’s note with the anticipated birth date. After birth, upload Official Birth certificate before enrollment.)",
        "Is your name on the birth certificate?",
        "If your name is not on the birth certificate, then you will also need to provide proof of custody. If you are unable to provide one of these documents right now, you will need to provide proof of custody before your child receives a center/school placement.   Please select the document you are uploading.",
        "Please upload documentation of proof of custody.",
        "Upload the ID of the parent/guardian completing the application.",
        "List below each adult living in the household who provides financial support to the family, their age, and their relationship to the child applicant.  (Example: Mother - 35 YEARS, Father - 35 YEARS, Aunt - 24 YEARS, Grandmother - 68 YEARS)",
        "What is the number of minors (below 18 years old) living in the household, INCLUDING THE CHILD APPLICANT?",
        "List below each minor living in the household, their age, and their relationship to the child applicant.  (Example: Child Applicant - 3 YEARS, Brother - 10 YEARS, Sister - 7 YEARS, Cousin - 7 YEARS)",
        "Upload the birth certificate (state issued or foreign) or passport or visa or hospital record or state-issued ID for each minor listed as a sibling of the applicant child.",
        "Upload one of these required documents for ALL dependent children listed in the household.",
        "Upload additional sibling birth certificates, if needed.",
        "Does your child have a sibling attending any of the centers/schools you ranked?",
        "You can list up to 3 siblings. Sibling name #1:",
        "Which center/school does sibling #1 attend?",
        "Sibling name #2:",
        "Which center/school does sibling #2 attend?",
        "Sibling name #3:",
        "Which center/school does sibling #3 attend?",
        "Do you work at one of the centers/schools you ranked?",
        "Which center/school do you work at?",
        "Is the parent applicant an unmarried minor (under age 18)?",
        "Does your child have an Individualized Family Service Plans (IFSP), or are they being evaluated for special education services?",
        "Is your name listed on the residency documents that you will be providing?",
        "Proof of residency #1.",
        "Verified residency document #1 type:",
        "Proof of residency #2.",
        "Verified residency document #2 type:",
        "Either the parent/guardian name must be on the residency documents or if the parent/guardian lives with another adult who is named on the residency documents, the parent/guardian must upload a signed letter from the person named on the residency documents stating that the parent/guardian lives at that same address.   If uploading a letter, parent/guardian must also upload acceptable proofs of residency in resident’s name and a photo of the resident's ID.",
        "Is the applicant a child of a parent or guardian in active Military service?",
        "Is Adult 1 (yourself) working?",
        "Please select the gender that best matches your SNAP application choice:",
        "Please select the ethnicity that best matches your SNAP application choice:",
        "Please select the race that best matches your SNAP application choice:",
        "Pay statement upload #1 and #2 (dated within 45-60 days of filling out this application)",
        "Pay statement upload #3 and #4, if needed (dated within 45-60 days of filling out this application)",
        "Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed)",
        "I state that my income or support comes from:",
        "Upload your most recent IRS Form 1099",
        "If choosing 'Parents/family', attach a statement from person providing support",
        "Describe your source of income",
        "Gross Income January:",
        "Gross Income February:",
        "Gross Income March:",
        "Gross Income April:",
        "Gross Income May:",
        "Gross Income June:",
        "Gross Income July:",
        "Gross Income August:",
        "Gross Income September:",
        "Gross Income October:",
        "Gross Income November:",
        "Gross Income December:",
        "My rent/house payments, utilities, food, and transportation expenses are being paid for by:",
        "Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission",
        "Enter the number of months you have been without income:",
        "I am (check all that apply)",
        "If 'Other' please describe your employment status",
        "Is Adult 1 (yourself) in school, in a training program, or seeking work?",
        "Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement",
        "Provide a school transcript, detailed school schedule, or letter from the registrar",
        "Provide hours attending and training courses on organization’s letterhead",
        "Provide hours attending and training courses (or hours worked) on organization’s letterhead",
        "Is Adult 2 working?",
        "Pay statement upload #1 and #2 (Adult 2) (dated within 45-60 days of filling out this application)",
        "I state that Adult 2's income or support comes from:",
        "Upload Adult 2's most recent IRS Form 1099",
        "Describe Adult 2's source of income",
        "Adult 2's rent/house payments, utilities, food, and transportation expenses are being paid for by:",
        "Enter the number of months Adult 2 has been without income:",
        "Adult 2 is (check all that apply)",
        "If 'Other' please describe Adult 2's employment status",
        "Is Adult 2 in school, in a training program, or seeking work?",
        "Is Adult 3 working?",
        "Pay statement upload #1 and #2 (Adult 3) (dated within 45-60 days of filling out this application)",
        "I state that Adult 3's income or support comes from:",
        "Upload Adult 3's most recent IRS Form 1099",
        "Describe Adult 3's source of income",
        "Adult 3's rent/house payments, utilities, food, and transportation expenses are being paid for by:",
        "Enter the number of months Adult 3 has been without income:",
        "Adult 3 is (check all that apply)",
        "If 'Other' please describe Adult 3's employment status",
        "Is Adult 3 in school, in a training program, or seeking work?",
        "Does your child receive SSI Benefits?",
        "Upload a statement from the Social Security Administration verifying that the child listed on the application is a recipient of SSI benefits.",
        "Does your child receive Family Independence Temporary Assistance (FITAP) or Temporary Assistance to Needy Families (TANF) benefits?",
        "Upload proof of benefits.",
        "Does the parent/guardian receive Social Security Administration disability benefits, supplemental security income, or Veterans Administration disability benefits for a disability of at least 70 percent?",
        "Are any adults included in your household count caring for any children with disabilities in the household?",
        "(Adult 1) Verified hours in school, training, or work",
        "(Adult 2) Verified hours in school, training, or work",
        "(Adult 3) Verified hours in school, training, or work",
        "Verified income (Use only numbers, no words)",
        "Which language did your child learn first?",
        "Which language does your child use most often at home?",
        "In what language do you most often speak to your child?",
        "Current phone number:",
        "Do you want to receive text communication from NOLA Public Schools?",
        "Current email address:",
        "A Gifted IEP is required for your child to attend Hynes Charter School. Do you have a Gifted and Talented evaluation or Gifted and Talented IEP approved by OPSB's Office of Child Search?	If yes, click the link below to request an administrative review	If no, click the link below to schedule an evaluation.",
        "Please rate your application experience on a scale of 1-5.",
        "Provide any additional feedback on your application experience below.",
        "Do you want to be contacted about jobs in early childhood? (either for yourself or someone you know)",
        "Does the child have social service needs?",
        "Have Headstart services been provided to this family in the past?",
        "Does the parent participate in Parents As Educators Kingsley House program?",
        "Is applicant a resident of Columbia Park in Gentilly?",
        "Please select if any of the following intra-agency transfer requests apply",
        "Transfer center:"
})
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

    // *** no questions for this one *** //
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

    // *** no questions for this one *** //
    @CsvBindByName(column="Are you interested in taking a seat now (2023-24 school year)? The list of available programs is linked below. We’ll make offers as seats become available. There is limited availability.")
    private String takeASeatCurrentSchoolYear;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload your child's birth certificate. (For unborn children, provide a doctor’s note with the anticipated birth date. After birth, upload Official Birth certificate before enrollment.)")
    private String studentsBirthCertificateDocument;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is your name on the birth certificate?")
    private String isParentNameOnBirthCertificate;

    // *** no questions for this one *** //
    @CsvBindByName(column="If your name is not on the birth certificate, then you will also need to provide proof of custody. If you are unable to provide one of these documents right now, you will need to provide proof of custody before your child receives a center/school placement.   Please select the document you are uploading.")
    private String custodyProofDocumentType;

    // *** no questions for this one *** //
    @CsvBindByName(column="Please upload documentation of proof of custody.")
    private String custodyProofDocument;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload the ID of the parent/guardian completing the application.")
    private String parentIdDocumentation;

    // *** cannot answer as we do not know relationship to child, only the applicant *** //
    @CsvBindByName(column="List below each adult living in the household who provides financial support to the family, their age, and their relationship to the child applicant.  (Example: Mother - 35 YEARS, Father - 35 YEARS, Aunt - 24 YEARS, Grandmother - 68 YEARS)")
    private String adultsWhoProvideSupport;

    // mapped
    @CsvBindByName(column="What is the number of minors (below 18 years old) living in the household, INCLUDING THE CHILD APPLICANT?")
    private String numberOfMinors;

    // *** cannot answer as we do not know relationship to child, only the applicant *** //
    @CsvBindByName(column="List below each minor living in the household, their age, and their relationship to the child applicant.  (Example: Child Applicant - 3 YEARS, Brother - 10 YEARS, Sister - 7 YEARS, Cousin - 7 YEARS)")
    List<String> minorsInHousehold;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload the birth certificate (state issued or foreign) or passport or visa or hospital record or state-issued ID for each minor listed as a sibling of the applicant child.")
    String siblingProofData;

    @CsvBindByName(column="Upload one of these required documents for ALL dependent children listed in the household.")
    String requiredDocAllDependents;

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

    // *** cannot answer, only have veteran *** //
    @CsvBindByName(column="Is the applicant a child of a parent or guardian in active Military service?")
    String isChildsParentGuardianInMilitaryService;

    // mapped
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

    // *** no questions for this one *** //
    @CsvBindByName(column="I state that my income or support comes from:")
    String adultOneIncomeSupportComesFrom;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload your most recent IRS Form 1099")
    String adultOneSelfEmployment1099;

    // *** no questions for this one *** //
    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support")
    String adultOneSupportFromParentFamilyStatementDocument;

    // *** no questions for this one *** //
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

    // *** no questions for this one *** //
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

    @CsvBindByName(column="If 'Other' please describe your employment status")
    String adultOneIAmOtherEmploymentStatusDescription;

    // mapped
    @CsvBindByName(column="Is Adult 1 (yourself) in school, in a training program, or seeking work?")
    String isAdultOneSchoolTrainingSeekingWork;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement")
    String adultOneHIRERegistrationProof;

    // *** no questions for this one *** //
    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar")
    String adultOneProofOfSchoolEnrollment;

    // *** no questions for this one *** //
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead")
    String adultOneHoursAttendingTrainingCoursesDocument;

    // *** no questions for this one *** //
    @CsvBindByName(column="Provide hours attending and training courses (or hours worked) on organization’s letterhead")
    String hoursAttendingOrWorked;

    // mapped
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

    // *** no questions for this one *** //
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

    // mapped
    @CsvBindByName(column="Is Adult 2 in school, in a training program, or seeking work?")
    String isAdultTwoSchoolTrainingSeekingWork;

    // mapped
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

    // *** no questions for this one *** //
    @CsvBindByName(column="Describe Adult 3's source of income")
    String adultThreeDescribeSourceOfIncome;

    // *** no questions for this one *** //
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

    // mapped
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

    // *** no questions for this one *** //
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

    // *** no questions for this one *** //
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
    @CsvBindByName(column="A Gifted IEP is required for your child to attend Hynes Charter School. Do you have a Gifted and Talented evaluation or Gifted and Talented IEP approved by OPSB's Office of Child Search?	If yes, click the link below to request an administrative review	If no, click the link below to schedule an evaluation.")
    String hasGiftedIEP;


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

    // mapped
    @CsvBindByName(column="What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.)")
    private String monthlyHouseholdIncome;

    // *** no questions for this one *** //

    /**
     * This static method will translate a submission into ECEApplicationCsvModel objects, using a Jackson ObjectMapper.
     * There could be multiple objects per one Submission if there are multiple household members under 5 or if a
     * household member is pregnant.
     *
     * The next step after this is for the CsvGenerator to "write" this object to the CSV file.
     * That will use the above mappings of method parameters to their column mapping.
     *
     * @param submission
     * @return List of ECEApplicationCsvModel objects
     * @throws JsonProcessingException
     */
    public static BaseCsvModel generateModel(Submission submission) throws JsonProcessingException {
        Map<String, Object> inputData = submission.getInputData();

        List<Map<String, Object>> householdList = (List) inputData.getOrDefault("household", List.of());
        List<Map<String, Object>> incomeList = (List) inputData.getOrDefault("income", List.of());
        List<String> students = (List) inputData.getOrDefault("students[]", List.of());
        List<String> jobSeekers = (List) inputData.getOrDefault("jobSearch[]", List.of());

        Map<String, Object> eceDataMap = new HashMap<>();

        eceDataMap.put("household", householdList);
        eceDataMap.put("phoneNumber", inputData.getOrDefault("phoneNumber", ""));
        eceDataMap.put("emailAddress", inputData.getOrDefault("emailAddress", ""));
        eceDataMap.put("id", submission.getId());

        int householdMemberCountProvidingSupport = (int) incomeList.stream().map(map -> map.get("householdMemberJobAdd")).distinct().count();
        eceDataMap.put("howManyPeopleInHousehold", householdMemberCountProvidingSupport);

        IncomeCalculator incomeCalculator = new IncomeCalculator(submission);
        double totalHouseholdMonthlyIncome = incomeCalculator.totalFutureEarnedIncome();
        eceDataMap.put("monthlyHouseholdIncome", totalHouseholdMonthlyIncome);

         // Get adults in household
        List<Map<String, Object>> adults = householdList
                .stream()
                .filter(
                        member -> HouseholdUtilities.isMember18orOlder(
                                Integer.parseInt((String) member.get("householdMemberBirthYear")),
                                Integer.parseInt((String) member.get("householdMemberBirthMonth")),
                                Integer.parseInt((String)member.get("householdMemberBirthDay"))
                        )
                )
                .toList();
        List<Map<String, Object>> finalAdults = adults;
        var numMinors = householdList
                .stream().filter(
                        member -> !finalAdults.contains(member)
                ).distinct().count();
        eceDataMap.put("numberOfMinors", numMinors);
        if (adults.size() > 2){
            adults = adults.subList(0, 2); // only consider top 2 items of list
        }
        var adultTwo = adults.size() >= 1 ?
                String.format("%s %s",
                        adults.get(0).get("householdMemberFirstName"),
                        adults.get(0).get("householdMemberLastName")) :
                "";
        var adultThree = adults.size() == 2 ?
                String.format("%s %s",
                        adults.get(1).get("householdMemberFirstName"),
                        adults.get(1).get("householdMemberLastName")) : "";


        var workingMembers = incomeList.stream().map(map -> map.get("householdMemberJobAdd")).toList();
        eceDataMap.put("isAdultOneWorking", workingMembers.contains("you"));

        eceDataMap.put("isAdultTwoWorking", workingMembers.contains(adultTwo));
        eceDataMap.put("isAdultThreeWorking", workingMembers.contains(adultThree));


        // school info

        eceDataMap.put("isAdultOneSchoolTrainingSeekingWork", students.contains("you") || jobSeekers.contains("you"));
        eceDataMap.put("isAdultTwoSchoolTrainingSeekingWork", students.contains(adultTwo) || jobSeekers.contains(adultTwo));
        eceDataMap.put("isAdultThreeSchoolTrainingSeekingWork", students.contains(adultThree) || jobSeekers.contains(adultThree));

        eceDataMap.put("birthDay", inputData.get("birthDay"));
        eceDataMap.put("birthYear", inputData.get("birthYear"));
        eceDataMap.put("birthMonth", inputData.get("birthMonth"));

        eceDataMap.put("isStudentAddressTemporaryLiving", inputData.getOrDefault("noHomeAddress", "false"));

        // create one application per submission
        ECEApplicationCsvModel app = mapper.convertValue(eceDataMap, ECEApplicationCsvModel.class);

        return app;
    }
}
