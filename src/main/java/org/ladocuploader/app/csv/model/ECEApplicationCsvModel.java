package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.bean.CsvBindByName;
import formflow.library.data.Submission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.utils.HouseholdUtilities;
import org.ladocuploader.app.utils.IncomeCalculator;

import static org.ladocuploader.app.utils.HouseholdUtilities.isMemberEceEligible;

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

    // *** no questions for this one *** //
    @CsvBindByName(column="Admin notes on application")
    private String adminNotesOnApplication;

    // *** no questions for this one *** //
    @CsvBindByName(column="Choose the grade your child is applying for")
    private String gradeChildApplyingFor;

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
    private String howManyPeopleInHouseholdProvideFinancialSupport;

    // filled out
    @CsvBindByName(column="What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.)")
    private String householdMonthlyIncome;

    // *** no questions for this one; don't have mapping either *** //
    @CsvBindByName(column="Select the option below that best describes your household:")
    private String householdDescription;

    // TODO not sure we have this
    @CsvBindByName(column="Provide 1 of 4 forms of verification documents listed below:")
    private String verificationDocumentOneType;

    // TODO not sure we have this
    @CsvBindByName(column="Verification document upload:")
    private String verificationDocumentOne;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is the child you are applying for a twin and/or triplet?")
    private String isApplicantChildTwin;

    // *** no questions for this one *** //
    @CsvBindByName(column="Please list the name(s) of the child’s twin/triplets.")
    private String applicantsTwinTripletSiblingNames;

    // *** no questions for this one *** //
    @CsvBindByName(column="Are you interested in taking a seat now (2023-24 school year)? The list of available programs is linked below. We’ll make offers as seats become available. There is limited availability.")
    private String takeASeatCurrentSchoolYear;

    @CsvBindByName(column="Upload your child's birth certificate. (For unborn children, provide a doctor’s note with the anticipated birth date. After birth, upload Official Birth certificate before enrollment.)")
    private String studentsBirthCertificateDocument;


    // *** no questions for this one *** //
    @CsvBindByName(column="Is your name on the birth certificate?")
    private String isParentNameOnBirthCertificate;

    // *** no questions for this one *** //
    @CsvBindByName(column="If your name is not on the birth certificate, then you will also need to provide proof of custody. If you are unable to provide one of these documents right now, you will need to provide proof of custody before your child receives a center/school placement.   Please select the document you are uploading.")
    private String custodyProofDocumentType;

    // not clear if we have this
    @CsvBindByName(column="Please upload documentation of proof of custody.")
    private String custodyProofDocument;

    // we probably collect this, but how to tell if we grab the right document?
    @CsvBindByName(column="Upload the ID of the parent/guardian completing the application.")
    private String applicantsID;

    // CONVERTER NEEDED HERE  ?
    @CsvBindByName(column="List below each adult living in the household who provides financial support to the family, their age, and their relationship to the child applicant.  (Example: Mother - 35 YEARS, Father - 35 YEARS, Aunt - 24 YEARS, Grandmother - 68 YEARS)")
    private String householdFamilyAndAge;

    @CsvBindByName(column="What is the number of minors (below 18 years old) living in the household, INCLUDING THE CHILD APPLICANT?")
    private String numberOfMinorsInHousehold;

    // skipping - don't have relationship data
    @CsvBindByName(column="List below each minor living in the household, their age, and their relationship to the child applicant.  (Example: Child Applicant - 3 YEARS, Brother - 10 YEARS, Sister - 7 YEARS, Cousin - 7 YEARS)")
    private String minorsInHousehold;

    // we may have birth cert's but we won't know who they are for
    @CsvBindByName(column="Upload the birth certificate (state issued or foreign) or passport or visa or hospital record or state-issued ID for each minor listed as a sibling of the applicant child.  Upload one of these required documents for ALL dependent children listed in the household.")
    private String siblingProofDocuments;

    @CsvBindByName(column="Upload additional sibling birth certificates, if needed.")
    private String siblingAdditionalProofDocuments;

    // not asked about
    @CsvBindByName(column="Does your child have a sibling attending any of the centers/schools you ranked?")
    private String siblingAtSchoolCenterRanked;

    // not asked about
    @CsvBindByName(column="You can list up to 3 siblings. Sibling name #1:")
    private String siblingOneName;

    // not asked about
    @CsvBindByName(column="Which center/school does sibling #1 attend?")
    private String siblingOneSchoolCenter;

    // not asked about
    @CsvBindByName(column="Sibling name #2:")
    private String siblingTwoName;

    // not asked about
    @CsvBindByName(column="Which center/school does sibling #2 attend?")
    private String siblingTwoSchoolCenter;

    // not asked about
    @CsvBindByName(column="Sibling name #3:")
    private String siblingThreeName;

    // not asked about
    @CsvBindByName(column="Which center/school does sibling #3 attend?")
    private String siblingThreeSchoolCenter;

    // not asked about
    @CsvBindByName(column="Do you work at one of the centers/schools you ranked?")
    private String doesApplicantWorksAtSchoolCenterTheyRanked;

    // not asked about
    @CsvBindByName(column="Which center/school do you work at?")
    private String applicantWorksAt;

    // TODO start here
    @CsvBindByName(column="Is the parent applicant an unmarried minor (under age 18)?")
    private String isParentApplicantUnmarriedMinor;

    // not asked about
    @CsvBindByName(column="Does your child have an Individualized Family Service Plans (IFSP), or are they being evaluated for special education services?")
    private String doesApplicantChildHaveIFSPOrEvaluationHappening;

    // not asked about
    @CsvBindByName(column="Is your name listed on the residency documents that you will be providing?")
    private String isApplicantsNameOnResidencyDocuments;

    // TODO: what goes here?  filename?
    // maybe use converter
    @CsvBindByName(column="Proof of residency #1.")
    private String proofOfResidencyDocumentOne;

    @CsvBindByName(column="Verified residency document #1 type:")
    private String proofOfResidencyDocumentOneType;

    @CsvBindByName(column="Proof of residency #2.")
    private String proofOfResidencyDocumentTwo;

    @CsvBindByName(column="Verified residency document #2 type:")
    private String proofOfResidencyDocumentTwoType;

    // not asked about
    @CsvBindByName(column="Either the parent/guardian name must be on the residency documents or if the parent/guardian lives with another adult who is named on the residency documents, the parent/guardian must upload a signed letter from the person named on the residency documents stating that the parent/guardian lives at that same address.   If uploading a letter, parent/guardian must also upload acceptable proofs of residency in resident’s name and a photo of the resident's ID.")
    private String residencyNoticeParentId;

    // could we calculate this?
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
    public static List<BaseCsvModel> generateModel(Submission submission) throws JsonProcessingException {
        Map<String, Object> inputData = submission.getInputData();
        List<BaseCsvModel> eceApps = new ArrayList<>();
        List<Map<String, Object>> householdList = (List) inputData.getOrDefault("household", List.of());
        List<Map<String, Object>> incomeList = (List) inputData.getOrDefault("income", List.of());

        // Note: we check for if the applicant indicated interest in applying in the CsvGenerator code.  If we got this far
        // they are interested in ECE

        // Now fill out a row for each family member who qualifies (ie, was born after 9/30/2019)


        int householdMemberCountProvidingSupport = (int) incomeList.stream().map(map -> map.get("householdMemberJobAdd")).distinct().count();
        IncomeCalculator incomeCalculator = new IncomeCalculator(submission);
        double totalHouseholdMonthlyIncome = incomeCalculator.totalFutureEarnedIncome();

        int totalMinorsInHousehold = (int) householdList.stream()
            .filter(m -> {
                return HouseholdUtilities.isMember18orOlder(
                    Integer.parseInt((String)m.get("birthYear")),
                    Integer.parseInt((String)m.get("birthMonth")),
                    Integer.parseInt((String)m.get("birthDay"))
               );
            }).count();

        // each pass here will create another object (row) for any eligible household member (pregnant or young enough)
        for (Map<String, Object> member : householdList) {
            try {
                // skip family members who are not eligible
                if (!isMemberEceEligible(member, inputData)) {
                    continue;
                }

                // eligible, fill out an object for this person.A
                // TODO - is there something different we should put for a pregnancy?

                // this is the data that jackson will map into the EceModel, not inputData
                // we are mapping data from the submission into the field names in the above ECEApplicationCsvModel object.
                Map<String, Object> eceDataMap = new HashMap<>();

                // this uses the household members uuid. It's somewhat arbitrary, but needs to map to the same one in the
                // student csv and relationship csv.
                eceDataMap.put("id", member.get("uuid"));
                eceDataMap.put("howManyPeopleInHouseholdProvideFinancialSupport", householdMemberCountProvidingSupport);
                eceDataMap.put("householdMonthlyIncome", totalHouseholdMonthlyIncome);

                // TODO: we may or may not have this, how to tell?
                //studentsBirthCertificateDocument

                // TODO: not sure if good mapping.
                eceDataMap.put("isStudentAddressTemporaryLiving", inputData.getOrDefault("noHomeAddress", "false"));
                // doesStudentHaveDisabilityOrSpecEdServices <-- we don't directly ask about this but we do ask about why someone doesn't work. Probably not related

                eceDataMap.put("numberOfMinorsInHousehold", totalMinorsInHousehold);

                // TODO: keep adding relevant fields from the above object here
                ECEApplicationCsvModel app = mapper.convertValue(eceDataMap, ECEApplicationCsvModel.class);
                app.setSubmissionId(submission.getId());
                eceApps.add(app);
            } catch (NumberFormatException e) {
                // TODO what to do if this does fail?? ignore and keep going? probably
                log.error("Unable to work with household member {}'s birthday ({}/{}/{}): {}",
                    member.get("householdMemberFirstName"),
                    (String) member.get("householdMemberBirthDay"),
                    (String) member.get("householdMemberBirthMonth"),
                    (String) member.get("householdMemberBirthYear"),
                    e.getMessage()
                );
            }
        }
        return eceApps;
    }
}
