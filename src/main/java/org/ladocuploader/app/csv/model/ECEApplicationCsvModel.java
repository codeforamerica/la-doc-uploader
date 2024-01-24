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

    // **********  BEGIN Avela required fields ************* //
    @CsvBindByName(column="Status (InProgress/Submitted) {{status}}", required=true)
    private String status = "InProgress";  // requested default

    @CsvBindByName(column="Hide Form from Parent (Yes/No) {{hide}}", required=true)
    private String hideApplication = "No"; // required field, requested default

    // **********  END Avela required fields ************* //

    // *** no questions for this one *** //

    @CsvBindByName(column="Choose the grade your child is applying for")
    private String chooseStudentGrade;

    @CsvBindByName(column="School Id {{school-id}}")
    private String schoolId;

    // *** no questions for this one *** //
    @CsvBindByName(column="School Rank {{school-rank}}")
    private String schoolRank;

    // *** no questions for this one *** //
    @CsvBindByName(column="Admin Notes {{9ddb4548-18ec-41e0-a872-1cb9036052b8}}")
    private String adminNotes;

    // *** no questions for this one *** //
    @CsvBindByName(column="Admin Last Reviewed Date {{7367c557-48b9-4bbc-af98-3dad61b6c80e}}")
    private String adminLastReviewedDate;

    // *** no questions for this one *** //
    @CsvBindByName(column="Admin Reviewer Name {{e6ad498b-dc32-4b07-a85b-51d53698531f}}")
    private String adminReviewerName;

    @CsvBindByName(column="Current phone number: {{96caa531-7196-4067-bbad-67a028b8a18c}}", required=true)
    private String phoneNumber;

    @CsvBindByName(column="Do you want to receive text communication from NOLA Public Schools? {{b7a45036-80cd-46d5-950b-890d553a99a6}}")
    private String allowTextCommunicationFromNolaPS;

    @CsvBindByName(column="Current email address: {{e89a5696-5c8a-497c-ae97-54c236c35225}}")
    private String emailAddress;

    // TODO format?
    @CsvBindByName(column="When was your child born? {{60dacb29-5950-4231-8150-5d24c3f1c76a}}")
    private String childBirthDate;

    // *** no questions for this one *** //
    @CsvBindByName(column="Select the option that best describes where you live.")
    private String descriptionOfLivingEnv;

    // *** no questions for this one *** //
    @CsvBindByName(column="This questionnaire is intended to address the McKinney-Vento Act. Your child may be eligible for additional educational services.   Did the student receive McKinney Vento (Homeless) Services in a previous school district?", required=true)
    private String hadHomelessServicesInPreviousSchool;

    @CsvBindByName(column="Is the student’s address a temporary living arrangement?")
    private String isStudentAddressTemporaryLiving;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is the temporary living arrangement due to loss of housing or economic hardship?")
    private String isTempLivingDueToLossOfHousingOrEconomicHardship;

    @CsvBindByName(column="Does the student have a disability or receive any special education-related services?")
    private String doesStudentHaveDisabilityOrSpecEdServices;

    // TODO - what format? Address or description?
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

    @CsvBindByName(column="How many people, including children, are in your household?  Only include all children, parents, guardians, and/or additional adults who provide financial support to the family.")
    private String howManyPeopleInHousehold;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload a current Foster Care Placement Agreement from DCFS. {{ec3173bd-76e0-4612-a619-1837bf38d8b0}}")
    private String placementAgreementDCFS;

    @CsvBindByName(column="Please select the gender that best matches your SNAP application choice:")
    private String applicantGender;

    @CsvBindByName(column="Please select the ethnicity that best matches your SNAP application choice:")
    private String applicantEthnicity;

    // TODO: what does this mean
    @CsvBindByName(column="Please select the race that best matches your SNAP application choice:")
    private String snapApplicationChoice;

    // TODO: probably don't need this field
    @CsvBindByName(column="Upload proof of SNAP benefits, upload must include: Child’s name, Parent’s name, and certification thru date. {{2b0fb1d7-960c-4539-bcc9-a937b57512cb}}")
    private String snapDocumentationProof;

    @CsvBindByName(column="What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.)")
    private String monthlyHouseholdIncome;

//    @CsvBindByName(column="(2 people) What is your monthly household income? {{0f5a6893-9984-43f5-bc2b-7d42961d82e8}}")
//    private String monthlyHouseholdIncomeTwoPeople;
//
//    @CsvBindByName(column="(3 people) What is your monthly household income? {{e6ed2d15-8a27-48d7-ae04-a6ea6a9cc69a}}")
//    private String monthlyHouseholdIncomeThreePeople;
//
//    @CsvBindByName(column="(4 people) What is your monthly household income? {{117f3de3-1965-40f9-95d6-e5f7ea26985d}}")
//    private String monthlyHouseholdIncomeFourPeople;
//
//    @CsvBindByName(column="(5 people) What is your monthly household income? {{62545824-4f48-4b2b-b27a-38ed11b1904e}}")
//    private String monthlyHouseholdIncomeFivePeople;
//
//    @CsvBindByName(column="(6 people) What is your monthly household income? {{cb48064f-34eb-43cb-9fb6-d0c9d642bb8f}}")
//    private String monthlyHouseholdIncomeSixPeople;
//
//    @CsvBindByName(column="(7 people) What is your monthly household income? {{f741dbf8-f125-45bd-8617-93a723a96abd}}")
//    private String monthlyHouseholdIncomeSevenPeople;
//
//    @CsvBindByName(column="(8 people) What is your monthly household income? {{414ba9f8-4820-4780-8ba9-5e4b5aaa7fd2}}")
//    private String monthlyHouseholdIncomeEightPeople;
//
//    @CsvBindByName(column="(9 people) What is your monthly household income? {{8d65d784-dd8a-46e9-9699-f5a52906417e}}")
//    private String monthlyHouseholdIncomeNinePeople;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload your child's current Individualized Education Program (IEP) {{cad80fcd-51d3-4c59-b720-2d6a79a89ea6}}")
    private String uploadedChildsIEP;

    // *** no questions for this one *** //
    @CsvBindByName(column="Verified income entered matches the monthly income range chosen on application. (Update range to match verified income). FRC Verified Income: {{d945ebd8-b8f8-4b8d-bcd3-42629c737971}}")
    private String verifiedIncomeRange;

    // *** no questions for this one *** //
    @CsvBindByName(column="EHS/HS Verified Income: {{80265075-ee90-4f63-aa92-fe6639c14c05}}")
    private String verifiedIncomeEHSHS;

    // *** no questions for this one *** //
    @CsvBindByName(column="Foster care agreement notes {{b0761781-8c9f-4ecc-b012-970c5a802c24}}")
    private String fosterCareAgreementNotes;

    // *** no questions for this one *** //
    @CsvBindByName(column="SNAP verified date {{e5807d51-0c40-423b-b7fe-f1752818affd}}")
    private String snapVerifiedDate;

    // *** no questions for this one *** //
    @CsvBindByName(column="ECS Only: IEP verification notes {{be56686a-d1d8-4f90-8bff-fab1b1c66d33}}")
    private String verificationNotesIEP;

    @CsvBindByName(column="Select the option below that best describes your household:", required=true)
    private String householdDescription;

    @CsvBindByName(column="Provide 1 of 4 forms of verification documents listed below:", required=true)
    private String verificationDocumentOneType;

    @CsvBindByName(column="Verification document upload:")
    private String verificationDocumentOne;

    @CsvBindByName(column="Is the child you are applying for a twin and/or triplet?")
    private String twinOrTriplet;

    @CsvBindByName(column="Please list the name(s) of the child’s twin/triplets.")
    private String twinOrTripletName;

    @CsvBindByName(column="Provide 1 of 4 forms of verification documents listed below: {{81f31312-ec04-4612-93e2-971be134cd0f}}")
    private String verificationDocumentTwoType;

    @CsvBindByName(column="Verification document upload: {{50a35817-c6ad-4235-b7fb-afafc56a3aa9}}")
    private String verificationDocumentTwo;

    @CsvBindByName(column="[NO LONGER ON APPLICATION] Are ALL adults in the household working, in school, in a training program, or seeking work? (Household composition does not include: aunts, uncles, adult children, grandparents, etc. Only count adults who are legal guardians.) {{d28a4562-e878-4c60-bc67-8f3786a4c40e}}")
    private String areAllAdultsInHouseholdWorking;

    @CsvBindByName(column="[NO LONGER ON APPLICATION] Verification of disability allows adults in a household to be exempt from work or actively seeking employment requirements. Do any adults in your household have a disability? {{1416efe4-4f3e-4fbe-af1e-bbc31ee77d82}}")
    private String anyAdultsInHouseholdHaveDisability;

    // *** no questions for this one *** //
    @CsvBindByName(column="Admin notes on application")
    private String adminNotesOnApplication;

    // *** no questions for this one *** //
    @CsvBindByName(column="Are you interested in taking a seat now (2023-24 school year)? The list of available programs is linked below. We’ll make offers as seats become available. There is limited availability.")
    private String takeASeatCurrentSchoolYear;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload your child's birth certificate. (For unborn children, provide a doctor’s note with the anticipated birth date. After birth, upload Official Birth certificate before enrollment.)")
    private String studentsBirthCertificateDocument;

    @CsvBindByName(column="Child's birthdate (format: MM-DD-YYYY). Birthdate on birth certificate should match DOB entered on application. (If not, edit student record to match) {{4d057d18-dcd3-4905-aa1e-298d92509101}}")
    private String studentsBirthDate;

    @CsvBindByName(column="Is your name on the birth certificate?")
    private String isParentNameOnBirthCertificate;

    // *** no questions for this one *** //
//    @CsvBindByName(column="If your name is not on the birth certificate, then you will also need to provide proof of custody. If you are unable to provide one of these documents right now, you will need to provide proof of custody before your child receives a center/school placement.   Please select the document you are uploading.")
//    private String custodyProofDocumentType;

    // *** no questions for this one *** //
    @CsvBindByName(column="Please upload documentation of proof of custody.")
    private String custodyProofDocument;

    @CsvBindByName(column="Upload the ID of the parent/guardian completing the application.")
    private String parentIdDocumentation;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does the adult on this application have the authority to complete this application for this child? Describe relationship to student (optional) {{ebafed1e-b16b-46ab-80f3-ee56b1516a64}}")
    private String applicantsRelationshipToStudent;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload the government-issued ID or Passport for adult(s) in the household, including the parent/guardian completing the application. {{473eea87-56c9-4e66-8a55-cbe979f77fb5}}")
    private String governmentIssuedIDDocument;

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





    // *** START VALIDATING HERE
    @CsvBindByName(column="Either the parent/guardian name must be on the residency documents or if the parent/guardian lives with another adult who is named on the residency documents, the parent/guardian must upload a signed letter from the person named on the residency documents stating that the parent/guardian lives at that same address.   If uploading a letter, parent/guardian must also upload acceptable proofs of residency in resident’s name and a photo of the resident's ID.")
    String residencyNotice;

    @CsvBindByName(column="What is the name of the person on the residency documents? (ID matching that name must be uploaded) {{bce6f956-52b7-40a2-b042-886051e9f5cd}}")
    String residencyDocumentPersonName;

    @CsvBindByName(column="What is the phone number of the person named on the residency documents uploaded? {{b6a5c1ce-5eb0-45ca-af5e-66dd440fe1e6}}")
    String residencyDocumentPersonPhoneNumber;

    @CsvBindByName(column="Does the named resident provide financial support to the child’s family? {{9b6bbda0-04f6-49b5-949c-ee75977d47e7}}")
    String doesResidencyDocumentPersonProvideFinancialSupport;

    @CsvBindByName(column="Date on Residency Docs (Expires 2 months after app submission date if family is not verified): {{9140ae3c-c6dc-4f67-8bde-ee87b8ef202b}}")
    String residencyDocumentDate;

    @CsvBindByName(column="Upload your applicant child's current immunization records. {{34c5e5ee-c4ca-43de-8b89-00e672f6f8c4}}")
    String childsImmunizationRecordDocument;

    @CsvBindByName(column="Notes on immunization records, if needed {{59f6d133-3da5-4c3e-bbf8-e7383bea4913}}")
    String childImmunizationRecordNotes;

    @CsvBindByName(column="How many adults are in the household? {{6c7586e5-b989-402a-b1bd-7f7474e4a7fc}}")
    String numberOfAdultsInHousehold;


    //  *** START Adult One ***  UNUSED FIELDS, Use other Adult one section fields below ***
    @CsvBindByName(column="[NO LONGER ON APPLICATION] For each adult in the household (18+ years old, up to 3 adults), provide the following information.  Is Adult 1 (yourself) working, in school, or in a training program? {{316a6e00-a7bd-49f1-9432-f58d3d401ae4}}")
    String isAdultOneSchoolWorkTraining;
    @CsvBindByName(column="Pay statement upload #1 and #2. Two consecutive pay statements if paid twice a month or 4 consecutive pay statements if paid every week {{3d81cb01-ab00-4588-abad-3a5197f2f88f}}")
    String UNUSED_adultOnePayStatementsOneAndTwo;
    @CsvBindByName(column="Pay statement upload #3 and #4 {{a0d5e1e8-f2b4-4f2c-9069-74a1ed36d9d8}}")
    String UNUSED_adultOnePayStatementsThreeAndFour;
    @CsvBindByName(column="Employer letter {{9cf66ea0-094e-4808-ad80-2cb5b01bcefe}}")
    String UNUSED_adultOneEmployerLetter;
    @CsvBindByName(column="I state that my income or support comes from: {{92230880-e90b-4c7a-a26c-d7d1bd907baf}}")
    String UNUSED_adultOneIncomeSupportComesFrom;
    @CsvBindByName(column="If choosing 'Self-Employment', upload your most recent IRS Form 1099. {{8153e62d-e053-45f0-95dc-f94b395f4443}}")
    String UNUSED_adultOneSelfEmployment1099;
    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support. {{55da4cd1-dd49-40dc-98da-d6fac94bb5f7}}")
    String UNUSED_adultOneSupportFromParentFamilyStatementDocument;
    @CsvBindByName(column="If choosing 'Other', describe your source of income. {{b6e220c0-f3f4-47e8-a372-89f4ced37e1e}}")
    String UNUSED_adultOneOtherDescription;
    @CsvBindByName(column="Gross Income January: {{f62b5e19-f768-415f-8b23-735108c0eaa8}}")
    String UNUSED_adultOneGrossIncomeJanuary;
    @CsvBindByName(column="Gross Income February: {{40da6d7a-2d29-4a21-b843-7555699da6bb}}")
    String UNUSED_adultOneGrossIncomeFebruary;
    @CsvBindByName(column="Gross Income March: {{63677019-2e4d-4fbf-8299-8a90b30f50fb}}")
    String UNUSED_adultOneGrossIncomeMarch;
    @CsvBindByName(column="Gross Income April: {{f9debaf7-fde6-45e2-bb35-6190527626c6}}")
    String UNUSED_adultOneGrossIncomeApril;
    @CsvBindByName(column="Gross Income May: {{4bee9be8-f049-4b51-8d2b-fdecacfceabe}}")
    String UNUSED_adultOneGrossIncomeMay;
    @CsvBindByName(column="Gross Income June: {{3ec3dc81-dbd8-4686-86cf-e5fb635e2aac}}")
    String UNUSED_adultOneGrossIncomeJune;
    @CsvBindByName(column="Gross Income July: {{9db3568e-1f3a-4e71-90d0-5f82cd79f8d1}}")
    String UNUSED_adultOneGrossIncomeJuly;
    @CsvBindByName(column="Gross Income August: {{5903772c-8aa5-467e-95ac-058004e08a0b}}")
    String UNUSED_adultOneGrossIncomeAugust;
    @CsvBindByName(column="Gross Income September: {{7b4b2a1d-0818-4762-8014-c46dfea1d569}}")
    String UNUSED_adultOneGrossIncomeSeptember;
    @CsvBindByName(column="Gross Income October: {{1b2301b3-ac72-4b0f-b869-f90072462816}}")
    String UNUSED_adultOneGrossIncomeOctober;
    @CsvBindByName(column="Gross Income November: {{a4a63253-0566-46c1-8c7b-27aa9571df48}}")
    String UNUSED_adultOneGrossIncomeNovember;
    @CsvBindByName(column="Gross Income December: {{8a974b5e-c05e-44d5-99dd-29937fbdee63}}")
    String UNUSED_adultOneGrossIncomeDecember;
    @CsvBindByName(column="(Irregular Income) My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{4c39f2a0-b342-4be7-8c3d-226c81ba4d0b}}")
    String UNUSED_adultOneHouseholdThingsPaidForByIrregularIncome;
    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration {{263c0713-2c95-4108-a934-a053885e0823}}")
    String UNUSED_adultOneHIRERegistrationProof;
    @CsvBindByName(column="Proof of enrollment in school {{cd112c23-0c61-4545-820d-c30d606e5c6e}}")
    String UNUSED_adultOneProofOfSchoolEnrollment;
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead {{bb48b12b-9b98-4608-ab95-5a01bfd1d0a6}}")
    String UNUSED_adultOneHoursAttendingTrainingCoursesDocument;
    @CsvBindByName(column="Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission {{764309ac-35c9-4174-a7e8-348aec0478c0}}")
    String UNUSED_adultOneUnemploymentBenefitsDocumentation;
    @CsvBindByName(column="Enter the number of months you have been without income: {{7318d2d4-3540-4003-9693-39fae21789f3}}")
    String UNUSED_adultOneNumberMonthsWithoutIncome;
    @CsvBindByName(column="I am (check all that apply) {{70d8c66e-1b33-43d2-8986-8e91d9aebb81}}")
    String UNUSED_adultOneIAmOptions;
    @CsvBindByName(column="If 'Other', describe your employment status {{5dfe978d-d70d-4660-a307-cdb02e09c0ff}}")
    String UNUSED_adultOneIAmOtherNotes;
    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{c37ef8be-0053-43ed-92b6-375bfa09d2b1}}")
    String UNUSED_adultOneHouseholdThingsPaidForByRegularIncome;
    //  *** END Adult One ***

    //  *** START Adult Two *** UNUSED FIELDS use other section of Adult two fields below
    @CsvBindByName(column="[NO LONGER ON APPLICATION] Is Adult 2 (if applicable) working, in school, or in a training program? {{07f6f02c-214b-47e8-9475-3a4ece6ff4c3}}")
    String isAdultTwoSchoolWorkTraining;
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 2) Two consecutive pay statements if paid twice a month or 4 consecutive pay statements if paid every week) {{610ce4e6-6a77-4e4c-a15e-adf2ffc080b1}}")
    String UNUSED_adultTwoPayStatementOneAndTwo;
    @CsvBindByName(column="Pay statement upload #3 and #4 (Adult 2) {{c0b7c33d-a6ae-4e8f-a221-79c388682e2c}}")
    String UNUSED_adultTwoPayStatementThreeAndFour;
    @CsvBindByName(column="I state that my income or support comes from (Adult 2): {{efd9993b-5e84-476f-af3f-d3d9dfa795fb}}")
    String UNUSED_adultTwoIncomeSupportComesFrom;
    @CsvBindByName(column="If choosing 'Self-Employment', upload your most recent IRS Form 1099. (Adult 2) {{a16298bd-4328-47f1-8853-29ff5155304d}}")
    String UNUSED_adultTwoSelfEmployment1099;
    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support. (Adult 2) {{b1aa21d8-3dcd-4a7a-83c1-8c5a49b5f0a7}}")
    String UNUSED_adultTwoSupportFromParentFamilyStatementDocument;
    @CsvBindByName(column="If choosing 'Other', describe your source of income. (Adult 2) {{737800bc-65df-4e4f-93ce-7681ce0182f9}}")
    String UNUSED_adultTwoOtherDescription;
    @CsvBindByName(column="Gross Income January (Adult 2): {{69f13f77-2223-4b9e-96a8-61d5bab01529}}")
    String UNUSED_adultTwoGrossIncomeJanuary;
    @CsvBindByName(column="Gross Income February (Adult 2): {{69c416de-58eb-4443-893b-f679e1b4f138}}")
    String UNUSED_adultTwoGrossIncomeFebruary;
    @CsvBindByName(column="Gross Income March (Adult 2): {{e7b6e9d2-2a79-457b-a60a-513c7ab67e81}}")
    String UNUSED_adultTwoGrossIncomeMarch;
    @CsvBindByName(column="Gross Income April (Adult 2): {{b9d0afad-4ca1-46b1-a162-3484bab03b26}}")
    String UNUSED_adultTwoGrossIncomeApril;
    @CsvBindByName(column="Gross Income May (Adult 2): {{359cb6dc-e6c2-4983-9c3e-f6231e8fadb0}}")
    String UNUSED_adultTwoGrossIncomeMay;
    @CsvBindByName(column="Gross Income June (Adult 2): {{5027f057-4f9b-476a-8cf3-1f54b1e686f5}}")
    String UNUSED_adultTwoGrossIncomeJune;
    @CsvBindByName(column="Gross Income July (Adult 2): {{ce4b82a2-f016-4e32-b5ca-f6266754827e}}")
    String UNUSED_adultTwoGrossIncomeJuly;
    @CsvBindByName(column="Gross Income August (Adult 2): {{7a859023-348c-40dd-93d5-b0e5b0f359ad}}")
    String UNUSED_adultTwoGrossIncomeAugust;
    @CsvBindByName(column="Gross Income September (Adult 2): {{b025340a-8d7e-4c09-9327-6db1274676ff}}")
    String UNUSED_adultTwoGrossIncomeSeptember;
    @CsvBindByName(column="Gross Income October (Adult 2): {{ce94d5ab-7039-444a-812c-564fe1faebe7}}")
    String UNUSED_adultTwoGrossIncomeOctober;
    @CsvBindByName(column="Gross Income November (Adult 2): {{f4bccfa9-e185-457b-85a3-8d0fe9f20ffb}}")
    String UNUSED_adultTwoGrossIncomeNovember;
    @CsvBindByName(column="Gross Income December (Adult 2): {{28d6bb2b-bb4f-4a2b-b762-fbfea7391f80}}")
    String UNUSED_adultTwoGrossIncomeDecember;
    @CsvBindByName(column="(Irregular Income, Adult 2) My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{bf647581-7ee2-4e76-abf8-c7c0b101d2c0}}")
    String UNUSED_adultTwoHouseholdThingsPaidForByIrregularIncome;
    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration (Adult 2) {{891df962-c957-4ffb-b7e6-e628563ff87a}}")
    String UNUSED_adultTwoHIRERegistrationProof;
    @CsvBindByName(column="Proof of enrollment in school (Adult 2) {{bf8f98d3-6c4a-4dd9-9170-b9fe5b5a3088}}")
    String UNUSED_adultTwoProofOfSchoolEnrollment;
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead (Adult 2) {{494a6953-4b97-4749-84c0-d0a1aa031660}}")
    String UNUSED_adultTwoHoursAttendingTrainingCoursesDocument;
    @CsvBindByName(column="Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission (Adult 2) {{7894fdd2-51e1-45f6-bd70-73cc4ed19662}}")
    String UNUSED_adultTwoUnemploymentBenefitsDocumentation;
    @CsvBindByName(column="I am (check all that apply, Adult 2) {{a22abf87-4808-4d2b-b2d7-3f85a95d1a1a}}")
    String UNUSED_adultTwoIAmOptions;
    @CsvBindByName(column="If 'Other', describe your employment status (Adult 2) {{590669f1-02df-47c5-a8a7-f47cfbd1ad09}}")
    String UNUSED_adultTwoIAmOtherNotes;
    @CsvBindByName(column="(Adult 2) My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{7fd9ec84-b503-45c0-a085-4e187228db7f}}")
    String UNUSED_adultTwoHouseholdThingsPaidForByRegularIncome;

    //  *** START Adult Three *** UNUSED FIELDS use other section of Adult two fields below
    @CsvBindByName(column="[NO LONGER ON APPLICATION] Is Adult 3 (if applicable) working, in school, or in a training program? {{ec07d3d6-397b-401e-9f48-571b9073afe3}}")
    String isAdultThreeSchoolWorkTraining;
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 3) Two consecutive pay statements if paid twice a month or 4 consecutive pay statements if paid every week) {{9f00c9f7-d15f-411e-90fd-3d977bfbd1d9}}")
    String UNUSED_adultThreePayStatementOneAndTwo;
    @CsvBindByName(column="Pay statement upload #3 and #4 {{b4d44aa0-d9a1-46a7-a452-e8ee7e0412fa}}")
    String UNUSED_adultThreePayStatementThreeAndFour;
    @CsvBindByName(column="I state that my income or support comes from: {{3f134540-a1a3-43cd-bdae-345151aa5377}}")
    String UNUSED_adultThreeIncomeSupportComesFrom;
    @CsvBindByName(column="If choosing 'Self-Employment', upload your most recent IRS Form 1099. {{5bd0cf83-3e8b-417d-8db8-1b61d43a44ca}}")
    String UNUSED_adultThreeSelfEmployment1099;
    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support. {{0f180ed7-983a-44fc-80d3-1950cae9e678}}")
    String UNUSED_adultThreeSupportFromParentFamilyStatementDocument;
    @CsvBindByName(column="If choosing 'Other', describe your source of income. {{62e0bbd2-c5b1-43b1-bf09-3c8054f7131e}}")
    String UNUSED_adultThreeOtherDescription;
    @CsvBindByName(column="Gross Income January: {{5267cde3-6c69-4171-9d6d-20f7865530f6}}")
    String UNUSED_adultThreeGrossIncomeJanuary;
    @CsvBindByName(column="Gross Income February: {{d4faa19a-4c4c-4019-87d6-ce4c083abe0a}}")
    String UNUSED_adultThreeGrossIncomeFebruary;
    @CsvBindByName(column="Gross Income March: {{1d80a97e-46d0-49ca-ba3a-c420087dc99f}}")
    String UNUSED_adultThreeGrossIncomeMarch;
    @CsvBindByName(column="Gross Income April: {{9eecd840-577b-4092-b6a1-a1abb48754f6}}")
    String UNUSED_adultThreeGrossIncomeApril;
    @CsvBindByName(column="Gross Income May: {{4bf5d125-9265-45f5-965b-0e52744e2233}}")
    String UNUSED_adultThreeGrossIncomeMay;
    @CsvBindByName(column="Gross Income June: {{994b737d-1a4d-43d7-b0ba-a3be8c61abae}}")
    String UNUSED_adultThreeGrossIncomeJune;
    @CsvBindByName(column="Gross Income July: {{039ecd28-8284-4080-9c7c-1f0d5447d8db}}")
    String UNUSED_adultThreeGrossIncomeJuly;
    @CsvBindByName(column="Gross Income August: {{c1464761-8176-455b-afc1-0bbd60e08b73}}")
    String UNUSED_adultThreeGrossIncomeAugust;
    @CsvBindByName(column="Gross Income September: {{7badb7f3-8ef6-4b48-af22-86f1e2f0efe8}}")
    String UNUSED_adultThreeGrossIncomeSeptember;
    @CsvBindByName(column="Gross Income October: {{e3a72121-51c2-42da-baae-b3e850b259f7}}")
    String UNUSED_adultThreeGrossIncomeOctober;
    @CsvBindByName(column="Gross Income November: {{b8d018da-0a9a-4943-b110-11a0d80e8442}}")
    String UNUSED_adultThreeGrossIncomeNovember;
    @CsvBindByName(column="Gross Income December: {{b829b0b6-0269-41de-af71-03c61b4d2b28}}")
    String UNUSED_adultThreeGrossIncomeDecember;
    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{f80bebe0-3490-4dd3-8d42-d0954d93a9cc}}")
    String UNUSED_adultThreeHouseholdThingsPaidForByIrregularIncome;
    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration {{c26a87e7-7c0c-4677-b5b1-885e242e6344}}")
    String UNUSED_adultThreeHIRERegistrationProof;
    @CsvBindByName(column="Proof of enrollment in school {{cd07ee12-4a11-43c9-a9e1-4d20c3876b95}}")
    String UNUSED_adultThreeProofOfSchoolEnrollment;
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead {{e3fdaa05-8288-455b-84e4-9d2670245aba}}")
    String UNUSED_adultThreeHoursAttendingTrainingCoursesDocument;
    @CsvBindByName(column="Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission {{2828e44b-0a1f-4587-a683-20635f9d6623}}")
    String UNUSED_adultThreeUnemploymentBenefitsDocumentation;
    @CsvBindByName(column="Enter the number of months you have been without income: {{ec6887a7-6d67-4516-b9df-18bb4de9f9d0}}")
    String UNUSED_adultThreeNumberMonthsWithoutIncome;
    @CsvBindByName(column="I am (check all that apply) {{17847aaa-ac9f-4b1e-ba58-3ee4c8e974b4}}")
    String UNUSED_adultThreeIAmOptions;
    @CsvBindByName(column="If 'Other', describe your employment status {{287d10c9-b75f-4ae0-ba67-180287ab06b2}}")
    String UNUSED_adultThreeIAmOtherNotes;
    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{6c9ae174-b11e-42a3-88e9-58e986c8f03a}}")
    String UNUSED_adultThreeHouseholdThingsPaidForByRegularIncome;

    //  *** START Adult One Fields -- these are the fields that are actually used.
    @CsvBindByName(column="Is Adult 1 (yourself) working?")
    String isAdultOneWorking;
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
//    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{1fa3e7e4-6787-414d-8db3-eca31220888a}}")
//    String adultOneHouseholdThingsPaidForByRegularIncome;
    @CsvBindByName(column="Is Adult 1 (yourself) in school, in a training program, or seeking work?")
    String isAdultOneSchoolTrainingSeekingWork;
    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement")
    String adultOneHIRERegistrationProof;
    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar")
    String adultOneProofOfSchoolEnrollment;
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead")
    String adultOneHoursAttendingTrainingCoursesDocument;
//    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar  {{da1a75fa-fffc-4c0e-8e44-6f7cc8589153}}")
//    String adultOneProofOfSchoolEnrollment2;
//    @CsvBindByName(column="Provide hours attending and training courses (or hours worked) on organization’s letterhead")
//    String adultOneHoursAttendingTrainingCoursesDocument2;
//    @CsvBindByName(column="(Adult 1) Verified hours in school, training, or work {{18ccb11c-15a2-4346-ada3-4788adea4a60}}")
//    String adultOneVerifiedHoursInSchoolTrainingWork;
    // *** END Adult One ***

    // *** START Adult Two ***
    @CsvBindByName(column="Is Adult 2 working?")
    String isAdultTwoWorking;
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 2) (dated within 45-60 days of filling out this application)")
    String adultTwoPayStatementOneAndTwo;
//    @CsvBindByName(column="Pay statement upload #3 and #4 (dated within 45-60 days of filling out this application) {{5ae83513-a34a-4a0f-9b17-ee4c306912bc}}")
//    String adultTwoPayStatementThreeAndFour;
//    @CsvBindByName(column="Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed) {{09c63241-d537-41e0-b8b8-34699c785366}}")
//    String adultTwoEmployerLetter;
    @CsvBindByName(column="I state that Adult 2's income or support comes from:")
    String adultTwoIncomeSupportComesFrom;
    @CsvBindByName(column="Upload Adult 2's most recent IRS Form 1099")
    String adultTwoSelfEmployment1099;
//    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support {{fcc69085-0f0f-44d8-805d-f5ebd3ab2437}}")
//    String adultTwoSupportFromParentFamilyStatementDocument;
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


//    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{10ac360a-b4d2-4172-ae28-d0f30d9c52ed}}")
//    String adultTwoHouseholdThingsPaidForByIrregularIncome; // TODO assumption it means irregular, not sure
//    @CsvBindByName(column="Upload documentation of Unemployment Benefits: a Monetary Determination letter from the Workforce Commission {{9780155c-c1c5-4363-b5e0-66c694beb173}}")
//    String adultTwoUnemploymentBenefitsDocumentation;
//    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement {{d2c636e7-205c-45f3-8974-af09a66010c9}}")
//    String adultTwoHIRERegistrationProof;
//    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar  {{b617abd4-eab8-4cde-a0c2-ef97f080c09c}}")
//    String adultTwoProofOfSchoolEnrollment;
//    @CsvBindByName(column="Provide hours attending and training courses (or hours worked) on organization’s letterhead {{14798ca8-c9b5-4488-a9e6-2efd30152e85}}")
//    String adultTwoHoursAttendingTrainingCoursesDocument;
//    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar {{80ebe9d6-bd51-432b-863e-599b7a24a453}}")
//    String adultTwoProofOfSchoolEnrollment2;
//    @CsvBindByName(column="Provide hours attending and training courses (or hours worked) on organization’s letterhead {{e3d391fc-11d9-4e02-aeab-9104139acb21}}")
//    String adultTwoHoursAttendingTrainingCoursesDocument2;
//    @CsvBindByName(column="(Adult 2) Verified hours in school, training, or work {{c5329d96-e0d5-4b96-a367-ad85bb7342fb}}")
//    String adultTwoVerifiedHoursInSchoolTrainingWork;
    // *** END Adult Two ***

    // *** START Adult Three ***
    @CsvBindByName(column="Is Adult 3 working?")
    String isAdultThreeWorking;
//    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 3) (dated within 45-60 days of filling out this application)")
//    String adultThreePayStatementOneAndTwo;
//    @CsvBindByName(column="Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed) {{07bea291-422b-41fd-b645-e6c8d2a4b0ed}}")
//    String adultThreeEmployerLetter;
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


    @CsvBindByName(column="Upload documentation of Unemployment Benefits: a Monetary Determination letter from the Workforce Commission {{a8c60a99-740e-4ca6-a83a-5a1d76107468}}")
    String adultThreeUnemploymentBenefitsDocumentation;

//    @CsvBindByName(column="Adult 3's rent/house payments, utilities, food, and transportation expenses are being paid for by: {{9002f394-486c-4d88-b2a4-4fcd3ca9e723}}")
//    String adultThreeHouseholdThingsPaidForByRegularIncome;

    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement {{2521146e-fa00-4c7d-b0bd-cf01ba62c6ad}}")
    String adultThreeHIRERegistrationProof;
    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar {{88789dbc-2e0f-4247-a771-daba641cf9be}}")
    String adultThreeProofOfSchoolEnrollment;
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead {{656e8683-1f04-4230-acbb-319c7a69b040}}")
    String adultThreeHoursAttendingTrainingCoursesDocument;
    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar {{93e3a15c-e125-416c-9e87-e8847f54e57a}}")
    String adultThreeProofOfSchoolEnrollment2;
    @CsvBindByName(column="Provide hours attending and training courses (or hours worked) on organization’s letterhead {{99912d95-4c04-4dc3-8129-60f4693b3916}}")
    String adultThreeHoursAttendingTrainingCoursesDocument2;
    @CsvBindByName(column="(Adult 3) Verified hours in school, training, or work {{2b3b6ecc-16e2-40e9-9324-c8f5a7ceb495}}")
    String adultThreeVerifiedHoursInSchoolTrainingWork;
    // *** END Adult Three ***

    @CsvBindByName(column="If there are additional adults in the household, upload their relevant documents for income, school, and/or training below. {{36545c0d-831c-4cf4-99d8-45e0be141374}}")
    String additionalAdultsDocumentation;
    @CsvBindByName(column="Date on Income Docs (Expires 2 months after app submission date if family is not verified): {{27397087-7332-4736-9080-cac145ca65a2}}")
    String dateOnIncomeDocuments;
    @CsvBindByName(column="Do any of the above adults receive child support, alimony, disability benefits, retirement benefits, and/or any other form of unearned income? {{a6220f72-8d89-45ad-aa5c-8435ae0d7c65}}")
    String doesAnyAdultGetUnearnedIncome;
    @CsvBindByName(column="Upload Unearned Income Documentation (child support, alimony, disability benefits, retirement benefits, etc.) {{014968a8-dd24-466d-aa0a-2d8c863702e4}}")
    String unearnedIncomeDocuments;
    // next one probably needs a converter? not even sure we collect this so maybe not.
    @CsvBindByName(column="List below each adult living in the household who provides financial support to the family, their age, and their relationship to the child applicant.  (Example: Mother - 35 YEARS, Father - 35 YEARS, Aunt - 24 YEARS, Grandmother - 68 YEARS)")
    List<String> adultsProvidingFinancialSupport;
    @CsvBindByName(column="What is the number of minors (below 18 years old) living in the household, INCLUDING THE CHILD APPLICANT?")
    String numberOfMinorsInHousehold;
    @CsvBindByName(column="List below each minor living in the household, their age, and their relationship to the child applicant.  (Example: Child Applicant - 3 YEARS, Brother - 10 YEARS, Sister - 7 YEARS, Cousin - 7 YEARS)")
    List<String> minorsInHousehold;
    @CsvBindByName(column="Upload the birth certificate (state issued or foreign) or passport or visa or hospital record or state-issued ID for each minor listed as a sibling of the applicant child.  Upload one of these required documents for ALL dependent children listed in the household.")
    String siblingProofData;
    @CsvBindByName(column="Upload additional sibling birth certificates, if needed.")
    String siblingAdditionalProofData;
    @CsvBindByName(column="Are any adults included in your household count caring for any children with disabilities in the household? {{baca161c-a8da-4408-bb02-6c028c7d203c}}")
    String areAnyAdultsInHouseholdCaregiversForChildWithDisabilitiesInHousehold;
    @CsvBindByName(column="If Yes, adult only needs to be working 15 hours. Notes on caring for SWD: {{eb230bf2-67dc-4781-b6bd-6c6af80c2323}}")
    String adultCaregiversForChildWithDisabilitiesNotes;
    @CsvBindByName(column="Do you work at one of the centers/schools you ranked?")
    String doesApplicantWorksAtSchoolCenterTheyRanked;
    @CsvBindByName(column="Which center/school do you work at?")
    String applicantWorksAt;
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
    @CsvBindByName(column="Is the parent applicant an unmarried minor (under age 18)?")
    String isParentApplicantUnmarriedMinor;

    @CsvBindByName(column="Does your child currently receive Child Care Assistance Program (CCAP) benefits? {{d70fc952-2934-4c0e-b746-ba8d9d1ec107}}")
    String doesChildReceiveCCAP;
    @CsvBindByName(column="Does your child have an Individualized Family Service Plans (IFSP), or are they being evaluated for special education services?")
    String doesChildHaveIFSPOrBeingEvaluated;
    @CsvBindByName(column="Does your child receive Family Independence Temporary Assistance (FITAP) or Temporary Assistance to Needy Families (TANF) benefits? {{34400559-5f00-4445-ac61-c5d1d04e1b58}}")
    String doesChildReceiveFITAPorTANF;
    @CsvBindByName(column="Upload proof of benefits. {{0cc44aac-4638-4f6f-9026-a94b31efbb3b}}")
    String benefitsDocumentation;
    @CsvBindByName(column="Does the parent/guardian receive Social Security Administration disability benefits, supplemental security income, or Veterans Administration disability benefits for a disability of at least 70 percent? {{f54e47cd-838e-4387-a06b-2aaf5873db9f}}")
    String doesParentGuardianReceiveSSDorSupplementalSecurityIncomeOrVADisability;
    @CsvBindByName(column="Upload proof of benefits or doctor’s statement of disability with date of letter. All disability verification must include the name of the person, the dates of validity of those benefits, and the dollar value of those benefits, if applicable.  {{47cd324f-a446-4947-a864-9672bed7532b}}")
    String disbilityVerification;
    @CsvBindByName(column="Is the applicant a child of a parent or guardian in active Military service?")
    String isChildsParentGuardianInMilitaryService;
    @CsvBindByName(column="Which language did your child learn first? {{6f9222c0-b4de-4427-a9c4-5699add06239}}")
    String childsFirstLanguage;
    @CsvBindByName(column="Which language does your child use most often at home? {{96ca9027-2d10-4560-ba0a-ebed20c987d4}}	In what language do you most often speak to your child? {{efa0e6ce-d581-4bc1-a4fd-93d8b58bc9bc}}")
    String childsPreferredLangAtHome;
    @CsvBindByName(column="A Gifted IEP is required for your child to attend Hynes Lakeview. Do you have a Gifted and Talented evaluation or Gifted and Talented IEP approved by OPSB's Office of Child Search? {{e6a2cf80-e34d-4185-91e5-d70539f9463e}}")
    String hasGiftedIEP;
    @CsvBindByName(column="[DOES NOT APPEAR ON APPLICATION] Please upload the Gifted and Talented IEP. {{0bd79982-e4b6-484b-bca1-4d2a47e3bc0e}}")
    String giftedIEPDocumentation;
    @CsvBindByName(column="If yes, click the link below to request an administrative review {{5e5b5538-dedf-4a34-bf29-6abfd20ed95a}}")
    String requestingAdministrativeReview;
    @CsvBindByName(column="If no, click the link below to schedule an evaluation. {{6934e6dd-5699-45ff-b7cf-e8368eee21c0}}")
    String requestingScheduleOfEvaluation;
    @CsvBindByName(column="Please rate your application experience on a scale of 1-5. {{e2785498-1a6f-4515-923e-dbed74717a48}}")
    String applicationFeedbackExperienceRating;
    @CsvBindByName(column="Provide any additional feedback on your application experience below. {{52a34525-ca2b-4cb3-ac8d-e284c6fac043}}")
    String applicationFeedbackAdditionalInfo;
    @CsvBindByName(column="Do you want to be contacted about jobs in early childhood? (either for yourself or someone you know) {{034af9c4-ae93-48c8-bf53-d3c39521345b}}")
    String okayToContactAboutJobsInEarlyChildhood;
    @CsvBindByName(column="Describe the parental status of the applicant's primary guardian. {{550ce129-9a8b-476c-b9b2-5b60e0a45bd2}}")
    String parentalStatusOfPrimaryGuardian;
    @CsvBindByName(column="Does the applicant have a disability? {{17b00594-b2c2-4491-b19b-46125b1979d5}}")
    String doesApplicantHaveDisability;
    @CsvBindByName(column="Is the primary caregiver working, in training, not working, or in school? {{a1102a95-ddae-485e-9706-9dbbc302c9a7}}")
    String isPrimaryCaregiverWorkingTrainingNotWorkingSchool;
    @CsvBindByName(column="Does the child have social service needs? {{213a8c83-e218-42ed-a5bc-740a325358e5}}")
    String doesChildHaveSocialServiceNeeds;
    @CsvBindByName(column="Homeless Status {{885fc44f-3b98-452f-8c6a-3258e630efbb}}")
    String homelessStatus;
    @CsvBindByName(column="Is the child applicant an English language learner? {{25bb9f7e-9146-4b49-8fac-101aa0d8781d}}")
    String isChildELL;
    @CsvBindByName(column="Does the child have serious health problems? {{bc4d2eef-bdbb-4e75-b19b-48a780f9a195}}")
    String doesChildHaveSeriousHealthIssues;
    @CsvBindByName(column="Have headstart services been provided to this family in the past? {{9f2a0b47-4f21-48ed-af6c-7c0e62ca5173}}")
    String hasFamilyHadHeadStartServicesPreviously;
    @CsvBindByName(column="Has this child been previously selected by a selection committee? {{aa75dc4c-3362-4ada-9271-ce4841a3216b}}")
    String wasChildPrevSelectedBySelectionCommittee;
    @CsvBindByName(column="Is sibling currently enrolled in EHS or HS center? {{d6b43f76-a441-44c2-be11-f6cf6467468d}}")
    String isSiblingCurrentlyEnrolledInEHSofHSCenter;
    @CsvBindByName(column="If yes, list center name {{f5b660ae-c39f-4957-9dbc-fed8dea82f0a}}")
    String centerName;
    @CsvBindByName(column="Does the parent participate in Parents As Educators Kingsley House program? {{102d4312-b812-477c-a703-c7bc928e95c5}}")
    String doesParentParticipateKingslyHouseProgram;
    @CsvBindByName(column="Is a parent/guardian active military? {{66af33a9-a309-4a4a-a916-5bcbe6504a3d}}")
    String isParentGuardianActiveMilitary;
    @CsvBindByName(column="Is the applicant parent pregnant? {{e9d593bd-e5fe-431b-a133-fb7333114719}}")
    String isApplicantParentPregnant;
    @CsvBindByName(column="Does the applicant parent participate in an EHS pregnant woman program? {{47ae17c7-41ab-4bb2-9361-4b0a2d96bb8e}}")
    String doesApplicantParentParticipateEHSPregnantWomenProgram;
    @CsvBindByName(column="What is the child applicant's CCAP status? {{eccba5aa-9292-475c-9c1a-12d28bb4af0e}}")
    String childApplicantsCCAPStatus;
    @CsvBindByName(column="COVID Effects {{cda1fab7-527e-4965-84fa-1119a126f781}}")
    String covidEffects;
    @CsvBindByName(column="Is applicant a resident of Columbia Park in Gentilly? {{259c65fa-7177-4eaa-9a8a-b0f5eecfda65}}")
    String isApplicantResidentOfColumbiaParkGentilly;
    @CsvBindByName(column="Is applicant's parent enrolled in Healthy Start? {{78ec6ef4-ce1b-4b97-a8dd-658cfe951790}}")
    String isApplicantsParentEnrolledHealthyStart;
    @CsvBindByName(column="Has applicant's parent accepted a referral to Healthy Start? {{df5f7b13-82df-49f4-b2c0-1bdd048b51ad}}")
    String hasApplicantParentAcceptedReferralHealthyStart;
    @CsvBindByName(column="Has applicant's parent declined Healthy Start services? {{e25217fb-2fe2-4aae-a0f7-c12b203b706c}}")
    String hasApplicantParentDeclinedHealthyStartServices;
    @CsvBindByName(column="Zip code {{0e6c71db-01ef-445b-b066-abacfa8adcc3}}")
    String zipCode;
    @CsvBindByName(column="Please select if any of the following intra-agency transfer requests apply {{3ea3da10-cdc3-4743-9c50-271975cd24b5}}")
    String interAgencyTransferRequests;
    @CsvBindByName(column="Transfer center: {{907d0f3b-fd25-42b5-a117-97a691be9bd8}}")
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
