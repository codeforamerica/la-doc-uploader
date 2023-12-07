package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import formflow.library.data.Submission;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.ladocuploader.app.csv.converters.AddressStreetConverter;
import org.ladocuploader.app.csv.converters.PhoneNumberConverter;

@Getter
@Setter
public class ECEApplicationCsvModel extends BaseCsvModel {

    @CsvBindByName(column="cfa_reference_id")
    private String id;

    // they will populate this field, we put it in for convenience
    @CsvBindByName(column="Student Id {{student-id}}", required=true)
    private String studentId;

    // **********  BEGIN Avela required fields ************* //
    @CsvBindByName(column="Status (InProgress/Submitted) {{status}}", required=true)
    private String status = "InProgress";  // requested default

    @CsvBindByName(column="Hide Application from Parent (Yes/No) {{hide}}", required=true)
    private String hideApplication = "No"; // required field, requested default

    // **********  END Avela required fields ************* //

    // *** no questions for this one *** //
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
    @CsvBindByName(column="Select the option that best describes where you live. {{cbbe71ea-8564-4980-8550-09be1be6775a}}")
    private String descriptionOfLivingEnv;

    // *** no questions for this one *** //
    @CsvBindByName(column="This questionnaire is intended to address the McKinney-Vento Act. Your child may be eligible for additional educational services. Did the student receive McKinney Vento (Homeless) Services in a previous school district? {{2137fc3b-97a6-495c-983e-b7ffd706d5cb}}", required=true)
    private String hadHomelessServicesInPreviousSchool;

    @CsvBindByName(column="Is the student’s address a temporary living arrangement? {{d38f1b8e-0243-48aa-945b-16028a0ef711}}")
    private String isStudentAddressTemporaryLiving;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is the temporary living arrangement due to loss of housing or economic hardship? {{e48d3080-50af-40b3-9f56-bfa07ba0b4d5}}")
    private String isTempLivingDueToLossOfHousingOrEconomicHardship;

    @CsvBindByName(column="Does the student have a disability or receive any special education-related services?  {{a9a378c6-3a9c-4d02-8175-e08b53b5a6b6}}")
    private String doesStudentHaveDisabilityOrSpecEdServices;

    // TODO - what format? Address or description?
    @CsvBindByName(column="Where is the student currently living? {{89471b56-80c6-42f1-9306-52558993613a}}")
    private String whereDoesStudentCurrentlyLive;

    // *** no questions for this one *** //
    @CsvBindByName(column="Other specific information about where the student is currently living: {{1611f4e9-51c5-4b98-b341-5b6c7d7fd2da}}")
    private String specificsAboutWhereStudentCurrentlyLiving;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does the student exhibit any behaviors that may interfere with his or her academic performance? {{553eb30e-9b7a-4d70-b57f-67a57b2b3124}}")
    private String doesStudentHaveBehaviorsThatAffectAcademics;

    // *** no questions for this one *** //
    @CsvBindByName(column="Would you like assistance with uniforms, student records, school supplies, transportation, other? (Describe): {{2f4ec55e-b38a-41c3-b5df-b4f4c1f7397f}}")
    private String needAssistanceWithSchoolRelatedThings;

    // *** no questions for this one *** //
    @CsvBindByName(column="Migrant – Have you moved at any time during the past three (3) years to seek temporary or seasonal work in agriculture (including Poultry processing, dairy, nursery, and timber) or fishing? {{f15cc4fa-f374-4e11-915e-bf8080bdabe9}}")
    private String hasFamilyMovedForAgriWork;

    @CsvBindByName(column="How many people, including children, are in your household? Only include all children, parents, guardians, and/or additional adults who provide financial support to the family. {{2c9aa7c8-4d13-42ad-971d-c67b12b11f3c}}")
    private String howManyPeopleInHousehold;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload a current Foster Care Placement Agreement from DCFS. {{ec3173bd-76e0-4612-a619-1837bf38d8b0}}")
    private String placementAgreementDCFS;

    @CsvBindByName(column="Select the gender that best matches your child's SNAP application choice: {{ba83974f-bef9-41b0-9f6a-5616ffdbbd92}}")
    private String childGender;

    @CsvBindByName(column="Select the ethnicity that best matches your child's SNAP application choice: {{621a6556-63dd-48a2-9f84-38669389d683}}")
    private String childEthnicity;

    // TODO: what does this mean
    @CsvBindByName(column="Select the race that best matches your child's SNAP application choice: {{9e26600f-fe56-4b9d-9b50-2a9a2ee879a8}}")
    private String snapApplicationChoice;

    // TODO: probably don't need this field
    @CsvBindByName(column="Upload proof of SNAP benefits, upload must include: Child’s name, Parent’s name, and certification thru date. {{2b0fb1d7-960c-4539-bcc9-a937b57512cb}}")
    private String snapDocumentationProof;

    @CsvBindByName(column="(2 people) What is your monthly household income? {{0f5a6893-9984-43f5-bc2b-7d42961d82e8}}")
    private String monthlyHouseholdIncomeTwoPeople;

    @CsvBindByName(column="(3 people) What is your monthly household income? {{e6ed2d15-8a27-48d7-ae04-a6ea6a9cc69a}}")
    private String monthlyHouseholdIncomeThreePeople;

    @CsvBindByName(column="(4 people) What is your monthly household income? {{117f3de3-1965-40f9-95d6-e5f7ea26985d}}")
    private String monthlyHouseholdIncomeFourPeople;

    @CsvBindByName(column="(5 people) What is your monthly household income? {{62545824-4f48-4b2b-b27a-38ed11b1904e}}")
    private String monthlyHouseholdIncomeFivePeople;

    @CsvBindByName(column="(6 people) What is your monthly household income? {{cb48064f-34eb-43cb-9fb6-d0c9d642bb8f}}")
    private String monthlyHouseholdIncomeSixPeople;

    @CsvBindByName(column="(7 people) What is your monthly household income? {{f741dbf8-f125-45bd-8617-93a723a96abd}}")
    private String monthlyHouseholdIncomeSevenPeople;

    @CsvBindByName(column="(8 people) What is your monthly household income? {{414ba9f8-4820-4780-8ba9-5e4b5aaa7fd2}}")
    private String monthlyHouseholdIncomeEightPeople;

    @CsvBindByName(column="(9 people) What is your monthly household income? {{8d65d784-dd8a-46e9-9699-f5a52906417e}}")
    private String monthlyHouseholdIncomeNinePeople;

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

    @CsvBindByName(column="Select the option below that best describes your household: {{cecc3450-685a-4e5f-9530-61653eaac98d}}", required=true)
    private String householdDescription;

    @CsvBindByName(column="Provide 1 of 4 forms of verification documents listed below: {{bb6e7a83-f22e-48f0-8135-8b12594970b5}}", required=true)
    private String verificationDocumentOneType;

    @CsvBindByName(column="Verification document upload: {{5aa666e9-b955-4936-a2c3-d260b9278d10}}")
    private String verificationDocumentOne;

    @CsvBindByName(column="Provide 1 of 4 forms of verification documents listed below: {{81f31312-ec04-4612-93e2-971be134cd0f}}")
    private String verificationDocumentTwoType;

    @CsvBindByName(column="Verification document upload: {{50a35817-c6ad-4235-b7fb-afafc56a3aa9}}")
    private String verificationDocumentTwo;

    @CsvBindByName(column="[NO LONGER ON APPLICATION] Are ALL adults in the household working, in school, in a training program, or seeking work? (Household composition does not include: aunts, uncles, adult children, grandparents, etc. Only count adults who are legal guardians.) {{d28a4562-e878-4c60-bc67-8f3786a4c40e}}")
    private String areAllAdultsInHouseholdWorking;

    @CsvBindByName(column="[NO LONGER ON APPLICATION] Verification of disability allows adults in a household to be exempt from work or actively seeking employment requirements. Do any adults in your household have a disability? {{1416efe4-4f3e-4fbe-af1e-bbc31ee77d82}}")
    private String anyAdultsInHouseholdHaveDisability;

    // *** no questions for this one *** //
    @CsvBindByName(column="Admin notes on application {{8df5f004-4e22-4fad-9dc0-3028ff30fdac}}")
    private String adminNotesOnApplication;

    // *** no questions for this one *** //
    @CsvBindByName(column="[DOES NOT APPEAR ON APPLICATION] Are you interested in taking a seat now (2022-23 school year)? The list of available programs is linked below. We’ll make offers as seats become available. There is limited availability. {{ced46aa6-2de0-42de-ba17-01bf356b2437}}")
    private String takeASeatCurrentSchoolYear;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload your child's birth certificate. (If your child is not yet born, you will need to provide this before enrollment). {{dbecb765-3f4c-4763-82f7-0862c59308fa}}")
    private String studentsBirthCertificateDocument;

    @CsvBindByName(column="Child's birthdate (format: MM-DD-YYYY). Birthdate on birth certificate should match DOB entered on application. (If not, edit student record to match) {{4d057d18-dcd3-4905-aa1e-298d92509101}}")
    private String studentsBirthDate;

    @CsvBindByName(column="Is your name on the birth certificate? {{04b3a5f1-ff0d-43a8-8a6a-e8627bf61cd2}}")
    private String isParentNameOnBirthCertificate;

    // *** no questions for this one *** //
    @CsvBindByName(column="If your name is not on the birth certificate, then you will also need to provide proof of custody. If you are unable to provide one of these documents right now, you will need to provide proof of custody before enrollment. Select the document you are uploading. {{89a8a5e5-b60c-4e83-896c-318c64d5d475}}")
    private String custodyProofDocumentType;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload documentation of proof of custody. {{23baee5b-8f8e-4dc4-87f3-9a017f855859}}")
    private String custodyProofDocument;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does the adult on this application have the authority to complete this application for this child? Describe relationship to student (optional) {{ebafed1e-b16b-46ab-80f3-ee56b1516a64}}")
    private String applicantsRelationshipToStudent;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload the government-issued ID or Passport for adult(s) in the household, including the parent/guardian completing the application. {{473eea87-56c9-4e66-8a55-cbe979f77fb5}}")
    private String governmentIssuedIDDocument;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is your name or your spouse's name listed on the residency documents that you will be providing? {{30603d5d-3dee-45fa-b897-893cb5f1829f}}")
    private String isYourNameOrSpouseNameOnResidencyDocuments;

    // *** no questions for this one *** //
    @CsvBindByName(column="Proof of residency #1. {{3e0e2b61-d9bb-48b7-bf3a-9420e0d0ac75}}")
    private String proofOfResidencyDocumentOne;

    // *** no questions for this one *** //
    @CsvBindByName(column="Verified residency document #1 type: {{154963c2-8a53-4cf3-add2-330a84a0eaad}}")
    private String proofOfResidencyDocumentOneType;

    // *** no questions for this one *** //
    @CsvBindByName(column="Proof of residency #2. {{640e2fc0-552f-486e-b24e-ce715e551c2a}}")
    private String proofOfResidencyDocumentTwo;

    // *** no questions for this one *** //
    @CsvBindByName(column="Verified residency document #2 type: {{952bf770-a7a7-4060-9e85-0731f8ba553c}}")
    private String proofOfResidencyDocumentTwoType;

    @CsvBindByName(column="Either the parent/guardian name must be on the residency documents or if the parent/guardian lives with another adult who is named on the residency documents, the parent/guardian must upload two proofs of residency in the resident's name and a photo ID from the person named on the documents. {{79aae8cf-13f0-4787-8c23-4f0a87e319e0}}")
    @CsvBindByName(column="What is the name of the person on the residency documents? (ID matching that name must be uploaded) {{bce6f956-52b7-40a2-b042-886051e9f5cd}}")
    @CsvBindByName(column="What is the phone number of the person named on the residency documents uploaded? {{b6a5c1ce-5eb0-45ca-af5e-66dd440fe1e6}}")
    @CsvBindByName(column="Does the named resident provide financial support to the child’s family? {{9b6bbda0-04f6-49b5-949c-ee75977d47e7}}")
    @CsvBindByName(column="Date on Residency Docs (Expires 2 months after app submission date if family is not verified): {{9140ae3c-c6dc-4f67-8bde-ee87b8ef202b}}")
    @CsvBindByName(column="Upload your applicant child's current immunization records. {{34c5e5ee-c4ca-43de-8b89-00e672f6f8c4}}")
    @CsvBindByName(column="Notes on immunization records, if needed {{59f6d133-3da5-4c3e-bbf8-e7383bea4913}}")
    @CsvBindByName(column="How many adults are in the household? {{6c7586e5-b989-402a-b1bd-7f7474e4a7fc}}")
    @CsvBindByName(column="[NO LONGER ON APPLICATION] For each adult in the household (18+ years old, up to 3 adults), provide the following information.  Is Adult 1 (yourself) working, in school, or in a training program? {{316a6e00-a7bd-49f1-9432-f58d3d401ae4}}")
    @CsvBindByName(column="Pay statement upload #1 and #2. Two consecutive pay statements if paid twice a month or 4 consecutive pay statements if paid every week {{3d81cb01-ab00-4588-abad-3a5197f2f88f}}")
    @CsvBindByName(column="Pay statement upload #3 and #4 {{a0d5e1e8-f2b4-4f2c-9069-74a1ed36d9d8}}")
    @CsvBindByName(column="Employer letter {{9cf66ea0-094e-4808-ad80-2cb5b01bcefe}}")
    @CsvBindByName(column="I state that my income or support comes from: {{92230880-e90b-4c7a-a26c-d7d1bd907baf}}")
    @CsvBindByName(column="If choosing 'Self-Employment', upload your most recent IRS Form 1099. {{8153e62d-e053-45f0-95dc-f94b395f4443}}")
    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support. {{55da4cd1-dd49-40dc-98da-d6fac94bb5f7}}")
    @CsvBindByName(column="If choosing 'Other', describe your source of income. {{b6e220c0-f3f4-47e8-a372-89f4ced37e1e}}")
    @CsvBindByName(column="Gross Income January: {{f62b5e19-f768-415f-8b23-735108c0eaa8}}")
    @CsvBindByName(column="Gross Income February: {{40da6d7a-2d29-4a21-b843-7555699da6bb}}")
    @CsvBindByName(column="Gross Income March: {{63677019-2e4d-4fbf-8299-8a90b30f50fb}}")
    @CsvBindByName(column="Gross Income April: {{f9debaf7-fde6-45e2-bb35-6190527626c6}}")
    @CsvBindByName(column="Gross Income May: {{4bee9be8-f049-4b51-8d2b-fdecacfceabe}}")
    @CsvBindByName(column="Gross Income June: {{3ec3dc81-dbd8-4686-86cf-e5fb635e2aac}}")
    @CsvBindByName(column="Gross Income July: {{9db3568e-1f3a-4e71-90d0-5f82cd79f8d1}}")
    @CsvBindByName(column="Gross Income August: {{5903772c-8aa5-467e-95ac-058004e08a0b}}")
    @CsvBindByName(column="Gross Income September: {{7b4b2a1d-0818-4762-8014-c46dfea1d569}}")
    @CsvBindByName(column="Gross Income October: {{1b2301b3-ac72-4b0f-b869-f90072462816}}")
    @CsvBindByName(column="Gross Income November: {{a4a63253-0566-46c1-8c7b-27aa9571df48}}")
    @CsvBindByName(column="Gross Income December: {{8a974b5e-c05e-44d5-99dd-29937fbdee63}}")
    @CsvBindByName(column="(Irregular Income) My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{4c39f2a0-b342-4be7-8c3d-226c81ba4d0b}}")
    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration {{263c0713-2c95-4108-a934-a053885e0823}}")
    @CsvBindByName(column="Proof of enrollment in school {{cd112c23-0c61-4545-820d-c30d606e5c6e}}")
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead {{bb48b12b-9b98-4608-ab95-5a01bfd1d0a6}}")
    @CsvBindByName(column="Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission {{764309ac-35c9-4174-a7e8-348aec0478c0}}")
    @CsvBindByName(column="Enter the number of months you have been without income: {{7318d2d4-3540-4003-9693-39fae21789f3}}")
    @CsvBindByName(column="I am (check all that apply) {{70d8c66e-1b33-43d2-8986-8e91d9aebb81}}")
    @CsvBindByName(column="If 'Other', describe your employment status {{5dfe978d-d70d-4660-a307-cdb02e09c0ff}}")
    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{c37ef8be-0053-43ed-92b6-375bfa09d2b1}}")
    @CsvBindByName(column="[NO LONGER ON APPLICATION] Is Adult 2 (if applicable) working, in school, or in a training program? {{07f6f02c-214b-47e8-9475-3a4ece6ff4c3}}")
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 2) Two consecutive pay statements if paid twice a month or 4 consecutive pay statements if paid every week) {{610ce4e6-6a77-4e4c-a15e-adf2ffc080b1}}")
    @CsvBindByName(column="Pay statement upload #3 and #4 (Adult 2) {{c0b7c33d-a6ae-4e8f-a221-79c388682e2c}}")
    @CsvBindByName(column="I state that my income or support comes from (Adult 2): {{efd9993b-5e84-476f-af3f-d3d9dfa795fb}}")
    @CsvBindByName(column="If choosing 'Self-Employment', upload your most recent IRS Form 1099. (Adult 2) {{a16298bd-4328-47f1-8853-29ff5155304d}}")
    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support. (Adult 2) {{b1aa21d8-3dcd-4a7a-83c1-8c5a49b5f0a7}}")
    @CsvBindByName(column="If choosing 'Other', describe your source of income. (Adult 2) {{737800bc-65df-4e4f-93ce-7681ce0182f9}}")
    @CsvBindByName(column="Gross Income January (Adult 2): {{69f13f77-2223-4b9e-96a8-61d5bab01529}}")
    @CsvBindByName(column="Gross Income February (Adult 2): {{69c416de-58eb-4443-893b-f679e1b4f138}}")
    @CsvBindByName(column="Gross Income March (Adult 2): {{e7b6e9d2-2a79-457b-a60a-513c7ab67e81}}")
    @CsvBindByName(column="Gross Income April (Adult 2): {{b9d0afad-4ca1-46b1-a162-3484bab03b26}}")
    @CsvBindByName(column="Gross Income May (Adult 2): {{359cb6dc-e6c2-4983-9c3e-f6231e8fadb0}}")
    @CsvBindByName(column="Gross Income June (Adult 2): {{5027f057-4f9b-476a-8cf3-1f54b1e686f5}}")
    @CsvBindByName(column="Gross Income July (Adult 2): {{ce4b82a2-f016-4e32-b5ca-f6266754827e}}")
    @CsvBindByName(column="Gross Income August (Adult 2): {{7a859023-348c-40dd-93d5-b0e5b0f359ad}}")
    @CsvBindByName(column="Gross Income September (Adult 2): {{b025340a-8d7e-4c09-9327-6db1274676ff}}")
    @CsvBindByName(column="Gross Income October (Adult 2): {{ce94d5ab-7039-444a-812c-564fe1faebe7}}")
    @CsvBindByName(column="Gross Income November (Adult 2): {{f4bccfa9-e185-457b-85a3-8d0fe9f20ffb}}")
    @CsvBindByName(column="Gross Income December (Adult 2): {{28d6bb2b-bb4f-4a2b-b762-fbfea7391f80}}")
    @CsvBindByName(column="(Irregular Income, Adult 2) My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{bf647581-7ee2-4e76-abf8-c7c0b101d2c0}}")
    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration (Adult 2) {{891df962-c957-4ffb-b7e6-e628563ff87a}}")
    @CsvBindByName(column="Proof of enrollment in school (Adult 2) {{bf8f98d3-6c4a-4dd9-9170-b9fe5b5a3088}}")
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead (Adult 2) {{494a6953-4b97-4749-84c0-d0a1aa031660}}")
    @CsvBindByName(column="Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission (Adult 2) {{7894fdd2-51e1-45f6-bd70-73cc4ed19662}}")
    @CsvBindByName(column="Enter the number of months you have been without income (Adult 2): {{30b42ec0-59a4-454a-95cb-c87fd3c9e710}}")
    @CsvBindByName(column="I am (check all that apply, Adult 2) {{a22abf87-4808-4d2b-b2d7-3f85a95d1a1a}}")
    @CsvBindByName(column="If 'Other', describe your employment status (Adult 2) {{590669f1-02df-47c5-a8a7-f47cfbd1ad09}}")
    @CsvBindByName(column="(Adult 2) My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{7fd9ec84-b503-45c0-a085-4e187228db7f}}")
    @CsvBindByName(column="[NO LONGER ON APPLICATION] Is Adult 3 (if applicable) working, in school, or in a training program? {{ec07d3d6-397b-401e-9f48-571b9073afe3}}")
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 3) Two consecutive pay statements if paid twice a month or 4 consecutive pay statements if paid every week) {{9f00c9f7-d15f-411e-90fd-3d977bfbd1d9}}")
    @CsvBindByName(column="Pay statement upload #3 and #4 {{b4d44aa0-d9a1-46a7-a452-e8ee7e0412fa}}")
    @CsvBindByName(column="I state that my income or support comes from: {{3f134540-a1a3-43cd-bdae-345151aa5377}}")
    @CsvBindByName(column="If choosing 'Self-Employment', upload your most recent IRS Form 1099. {{5bd0cf83-3e8b-417d-8db8-1b61d43a44ca}}")
    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support. {{0f180ed7-983a-44fc-80d3-1950cae9e678}}")
    @CsvBindByName(column="If choosing 'Other', describe your source of income. {{62e0bbd2-c5b1-43b1-bf09-3c8054f7131e}}")
    @CsvBindByName(column="Gross Income January: {{5267cde3-6c69-4171-9d6d-20f7865530f6}}")
    @CsvBindByName(column="Gross Income February: {{d4faa19a-4c4c-4019-87d6-ce4c083abe0a}}")
    @CsvBindByName(column="Gross Income March: {{1d80a97e-46d0-49ca-ba3a-c420087dc99f}}")
    @CsvBindByName(column="Gross Income April: {{9eecd840-577b-4092-b6a1-a1abb48754f6}}")
    @CsvBindByName(column="Gross Income May: {{4bf5d125-9265-45f5-965b-0e52744e2233}}")
    @CsvBindByName(column="Gross Income June: {{994b737d-1a4d-43d7-b0ba-a3be8c61abae}}")
    @CsvBindByName(column="Gross Income July: {{039ecd28-8284-4080-9c7c-1f0d5447d8db}}")
    @CsvBindByName(column="Gross Income August: {{c1464761-8176-455b-afc1-0bbd60e08b73}}")
    @CsvBindByName(column="Gross Income September: {{7badb7f3-8ef6-4b48-af22-86f1e2f0efe8}}")
    @CsvBindByName(column="Gross Income October: {{e3a72121-51c2-42da-baae-b3e850b259f7}}")
    @CsvBindByName(column="Gross Income November: {{b8d018da-0a9a-4943-b110-11a0d80e8442}}")
    @CsvBindByName(column="Gross Income December: {{b829b0b6-0269-41de-af71-03c61b4d2b28}}")
    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{f80bebe0-3490-4dd3-8d42-d0954d93a9cc}}")
    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration {{c26a87e7-7c0c-4677-b5b1-885e242e6344}}")
    @CsvBindByName(column="Proof of enrollment in school {{cd07ee12-4a11-43c9-a9e1-4d20c3876b95}}")
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead {{e3fdaa05-8288-455b-84e4-9d2670245aba}}")
    @CsvBindByName(column="Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission {{2828e44b-0a1f-4587-a683-20635f9d6623}}")
    @CsvBindByName(column="Enter the number of months you have been without income: {{ec6887a7-6d67-4516-b9df-18bb4de9f9d0}}")
    @CsvBindByName(column="I am (check all that apply) {{17847aaa-ac9f-4b1e-ba58-3ee4c8e974b4}}")
    @CsvBindByName(column="If 'Other', describe your employment status {{287d10c9-b75f-4ae0-ba67-180287ab06b2}}")
    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{6c9ae174-b11e-42a3-88e9-58e986c8f03a}}")
    @CsvBindByName(column="For each adult in the household (18+ years old, up to 3 adults), provide the following information. Is Adult 1 (yourself) working? {{aa31382c-6c30-4111-8272-43a58b3deb79}}")
    @CsvBindByName(column="Pay statement upload #1 and #2 (dated within 45-60 days of filling out this application) {{8cd83ffa-b61a-4056-ac58-bdc24e725b91}}")
    @CsvBindByName(column="Pay statement upload #3 and #4, if needed (dated within 45-60 days of filling out this application) {{51a394be-1aa8-4b3f-9310-444f31c0bc8e}}")
    @CsvBindByName(column="Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed) {{af09a137-ad2c-417e-84e2-84a324583073}}")
    @CsvBindByName(column="I state that my income or support comes from: {{edfaabe6-e830-41cb-811d-effb73095203}}")
    @CsvBindByName(column="Upload your most recent IRS Form 1099 {{211ed2d3-ccc8-472d-a916-5940c685c83a}}")
    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support {{d696c985-182a-4681-9ef8-9e6ae2674fde}}")
    @CsvBindByName(column="Describe your source of income {{444443f9-db05-47f6-a312-5b77c4ddcb27}}")
    @CsvBindByName(column="Gross Income January: {{a54ffabb-4a61-4bf7-a4a7-0402d1fdfc9a}}")
    @CsvBindByName(column="Gross Income February: {{2ba00291-6b93-44e5-9aa5-f2fb18e116b0}}")
    @CsvBindByName(column="Gross Income March: {{8552f63d-422e-403c-b2da-94094f886f4e}}")
    @CsvBindByName(column="Gross Income April: {{3e352744-e7ab-41ea-8435-b52d5a33b216}}")
    @CsvBindByName(column="Gross Income May: {{67c042b0-1ba8-44c9-87bc-486b8147e3c1}}")
    @CsvBindByName(column="Gross Income June: {{82ebed26-af78-4f9c-8dc0-53dee9d36675}}")
    @CsvBindByName(column="Gross Income July: {{af1a39dd-16ba-49f1-bb1f-b8ec4f8a90d8}}")
    @CsvBindByName(column="Gross Income August: {{2b2d310d-901d-459a-9571-51daf5769da4}}")
    @CsvBindByName(column="Gross Income September: {{652e0162-1b50-4b67-bf40-51c7353743aa}}")
    @CsvBindByName(column="Gross Income October: {{9f603f2f-7ecd-42bb-ba8e-3060837651bf}}")
    @CsvBindByName(column="Gross Income November: {{bbe1289d-4066-4fc0-9fd6-166220106dde}}")
    @CsvBindByName(column="Gross Income December: {{81abe417-2e3e-48ac-9ba1-81aceb6abb79}}")
    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{dc46611b-3bd1-4be8-8da1-3f3e29f3d279}}")
    @CsvBindByName(column="Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission {{9d1e4a12-5361-40a6-99fc-13d652e93f57}}")
    @CsvBindByName(column="Enter the number of months you have been without income: {{84145ec0-aaf5-4f6d-b938-173e86541dcd}}")
    @CsvBindByName(column="I am (check all that apply) {{d599b70b-0342-46a3-aa3d-99dac369becd}}")
    @CsvBindByName(column="If 'Other', describe your employment status {{1f58cdcc-186b-4b4b-bc6b-4291aa8e1be7}}")
    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{1fa3e7e4-6787-414d-8db3-eca31220888a}}")
    @CsvBindByName(column="Is Adult 1 (yourself) in school, in a training program, or seeking work? {{90dc1348-6db3-436f-937f-306aa812ded1}}")
    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement {{ccd82836-4b21-4463-91a9-414c034cff40}}")
    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar {{5a8a72f5-cec8-47db-941a-fe953cd2652e}}")
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead {{86148dbc-21c1-4f42-b775-e729cd90366d}}")
    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar  {{da1a75fa-fffc-4c0e-8e44-6f7cc8589153}}")
    @CsvBindByName(column="Provide hours attending and training courses (or hours worked) on organization’s letterhead {{6be5d690-5b1b-419b-999b-5efe6949d636}}")
    @CsvBindByName(column="(Adult 1) Verified hours in school, training, or work {{18ccb11c-15a2-4346-ada3-4788adea4a60}}")
    @CsvBindByName(column="Is Adult 2 working? {{c9f80dca-6623-4435-8b8e-3fb237a295db}}")
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 2) (dated within 45-60 days of filling out this application) {{41bed2d6-af77-4e53-b894-253bfd1440bd}}")
    @CsvBindByName(column="Pay statement upload #3 and #4 (dated within 45-60 days of filling out this application) {{5ae83513-a34a-4a0f-9b17-ee4c306912bc}}")
    @CsvBindByName(column="Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed) {{09c63241-d537-41e0-b8b8-34699c785366}}")
    @CsvBindByName(column="I state that Adult 2's income or support comes from: {{838d43dc-79b0-4401-adc0-941d320a63d7}}")
    @CsvBindByName(column="Upload your most recent IRS Form 1099 {{9f8f1ff0-df9b-458f-8d87-51fed6f58aad}}")
    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support {{fcc69085-0f0f-44d8-805d-f5ebd3ab2437}}")
    @CsvBindByName(column="Describe the source of income {{42034c33-a5a1-4851-975b-09c58341906c}}")
    @CsvBindByName(column="Gross Income January: {{79bc70ec-0213-4d06-8ada-b198d933683e}}")
    @CsvBindByName(column="Gross Income February: {{d0ce4a12-7320-436a-a6b0-46a7dc4a0ad9}}")
    @CsvBindByName(column="Gross Income March: {{5e1aa8ee-6494-4b25-81c0-ab7d31b7cfba}}")
    @CsvBindByName(column="Gross Income April: {{05afd738-659a-4827-a4ec-64d896234a4f}}")
    @CsvBindByName(column="Gross Income May: {{3eeb9667-4aa2-4a84-866c-b948baa152c3}}")
    @CsvBindByName(column="Gross Income June: {{af489912-626c-4263-967f-e7f101893708}}")
    @CsvBindByName(column="Gross Income July: {{1afc22c0-b8ac-4265-bb32-2082063141b1}}")
    @CsvBindByName(column="Gross Income August: {{c9992775-6d62-481b-bddb-6bfafbd85dd9}}")
    @CsvBindByName(column="Gross Income September: {{7d9de7e9-827a-4d1b-8a19-c6ae375ecffe}}")
    @CsvBindByName(column="Gross Income October: {{7a0b0328-d9c4-4b23-86c3-fb6553cf3422}}")
    @CsvBindByName(column="Gross Income November: {{5a700f3d-c2b4-43b8-80db-3731014f0b57}}")
    @CsvBindByName(column="Gross Income December: {{80886262-a90a-4205-9b37-cfe6a7422d81}}")
    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{10ac360a-b4d2-4172-ae28-d0f30d9c52ed}}")
    @CsvBindByName(column="Upload documentation of Unemployment Benefits: a Monetary Determination letter from the Workforce Commission {{9780155c-c1c5-4363-b5e0-66c694beb173}}")
    @CsvBindByName(column="Enter the number of months without income {{9c1327e6-6574-4ea9-8d7b-14b48b07f47d}}")
    @CsvBindByName(column="Adult 2 is (check all that apply) {{2e780838-1ef2-4fc9-bf07-b68a628b84ce}}")
    @CsvBindByName(column="If “Other” please describe the employment status {{1eb95835-4a01-4101-87df-882fd4a5f2d3}}")
    @CsvBindByName(column="Adult 2's rent/house payments, utilities, food, and transportation expenses are being paid for by: {{d819295a-f0be-49f3-853e-a4f6b378308b}}")
    @CsvBindByName(column="Is Adult 2 in school, in a training program, or seeking work? {{85befbbc-d3f6-43d9-a2ca-a12b20f5e933}}")
    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement {{d2c636e7-205c-45f3-8974-af09a66010c9}}")
    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar  {{b617abd4-eab8-4cde-a0c2-ef97f080c09c}}")
    @CsvBindByName(column="Provide hours attending and training courses (or hours worked) on organization’s letterhead {{14798ca8-c9b5-4488-a9e6-2efd30152e85}}")
    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar {{80ebe9d6-bd51-432b-863e-599b7a24a453}}")
    @CsvBindByName(column="Provide hours attending and training courses (or hours worked) on organization’s letterhead {{e3d391fc-11d9-4e02-aeab-9104139acb21}}")
    @CsvBindByName(column="(Adult 2) Verified hours in school, training, or work {{c5329d96-e0d5-4b96-a367-ad85bb7342fb}}")
    @CsvBindByName(column="Is Adult 3 working? {{8d1b43ff-3291-496d-ad1f-53cf57361f44}}")
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 3) (dated within 45-60 days of filling out this application) {{430530c8-6c23-4ca4-aa8d-8ce2186abd7c}}")
    @CsvBindByName(column="Pay statement upload #3 and #4 (dated within 45-60 days of filling out this application) {{abad5402-b4b5-4f35-92f4-6513763d483c}}")
    @CsvBindByName(column="Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed) {{07bea291-422b-41fd-b645-e6c8d2a4b0ed}}")
    @CsvBindByName(column="I state that Adult 3's income or support comes from: {{83de622a-0cff-460b-a10c-d814d019d46a}}")
    @CsvBindByName(column="Upload your most recent IRS Form 1099 {{fa615f5d-5389-46d3-b275-f0199519278b}}")
    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support {{b239f740-6d21-4f7e-adcc-9a9ffa19ee39}}")
    @CsvBindByName(column="Describe the source of income {{0bd9b7f0-406c-472b-971c-11a20c6bd202}}")
    @CsvBindByName(column="Gross Income January: {{5280e420-a6f8-4e93-8cb6-b7b3795b7f7f}}")
    @CsvBindByName(column="Gross Income February: {{4a5b5898-a8fb-476d-a789-e748baa8c35e}}")
    @CsvBindByName(column="Gross Income March: {{405f3e19-dacb-4982-991b-b60542c0b2a1}}")
    @CsvBindByName(column="Gross Income April: {{fbc13dd5-b63a-4e26-804e-4de01c0c49ef}}")
    @CsvBindByName(column="Gross Income May: {{dd8a5293-43fe-4a34-9e58-df9a6db4d67d}}")
    @CsvBindByName(column="Gross Income June: {{72e22bab-1d40-4b17-8390-7f4ebe0a72c1}}")
    @CsvBindByName(column="Gross Income July: {{2763b0b9-8b31-42fe-9b96-4e2e797680d2}}")
    @CsvBindByName(column="Gross Income August: {{1cf5e233-313f-4311-8c30-b60e9d59c938}}")
    @CsvBindByName(column="Gross Income September: {{94d27987-80eb-433b-b79e-fc5916ff2bd3}}")
    @CsvBindByName(column="Gross Income October: {{9fb62419-f6f7-4988-9853-e2a7b0f1a81d}}")
    @CsvBindByName(column="Gross Income November: {{3e898622-62d7-4756-87c5-aefed38ef73f}}")
    @CsvBindByName(column="Gross Income December: {{5b93dd60-97d9-47bb-b7e5-66360313ecc4}}")
    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{31ef8a1b-71d4-42ca-91b4-dd9dd7a720b0}}")
    @CsvBindByName(column="Upload documentation of Unemployment Benefits: a Monetary Determination letter from the Workforce Commission {{a8c60a99-740e-4ca6-a83a-5a1d76107468}}")
    @CsvBindByName(column="Enter the number of months without income {{d14b0311-b208-4d5f-899f-54d58ac4c4e4}}")
    @CsvBindByName(column="Adult 3 is (check all that apply) {{d08972bb-87f3-487e-bc3c-40f6d6b6df16}}")
    @CsvBindByName(column="If “Other” please describe the employment status {{55b20c20-7a41-489a-8014-aaf370191f1b}}")
    @CsvBindByName(column="Adult 3's rent/house payments, utilities, food, and transportation expenses are being paid for by: {{9002f394-486c-4d88-b2a4-4fcd3ca9e723}}")
    @CsvBindByName(column="Is Adult 3 in school, in a training program, or seeking work? {{0a568f4c-8fad-48f3-9ebb-719ba50c0f07}}")
    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement {{2521146e-fa00-4c7d-b0bd-cf01ba62c6ad}}")
    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar {{88789dbc-2e0f-4247-a771-daba641cf9be}}")
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead {{656e8683-1f04-4230-acbb-319c7a69b040}}")
    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar {{93e3a15c-e125-416c-9e87-e8847f54e57a}}")
    @CsvBindByName(column="Provide hours attending and training courses (or hours worked) on organization’s letterhead {{99912d95-4c04-4dc3-8129-60f4693b3916}}")
    @CsvBindByName(column="(Adult 3) Verified hours in school, training, or work {{2b3b6ecc-16e2-40e9-9324-c8f5a7ceb495}}")
    @CsvBindByName(column="If there are additional adults in the household, upload their relevant documents for income, school, and/or training below. {{36545c0d-831c-4cf4-99d8-45e0be141374}}")
    @CsvBindByName(column="Date on Income Docs (Expires 2 months after app submission date if family is not verified): {{27397087-7332-4736-9080-cac145ca65a2}}")
    @CsvBindByName(column="Do any of the above adults receive child support, alimony, disability benefits, retirement benefits, and/or any other form of unearned income? {{a6220f72-8d89-45ad-aa5c-8435ae0d7c65}}")
    @CsvBindByName(column="Upload Unearned Income Documentation (child support, alimony, disability benefits, retirement benefits, etc.) {{014968a8-dd24-466d-aa0a-2d8c863702e4}}")
    @CsvBindByName(column="List below each adult living in the household who provides financial support to the family, their age, and their relationship to the child applicant. (Example: Mother - 35 YEARS, Father - 35 YEARS, Aunt - 24 YEARS, Grandmother - 68 YEARS) {{fc409a35-8dbc-4c63-a0cf-e766d7b01070}}")
    @CsvBindByName(column="What is the number of minors (below 18 years old) living in the household, INCLUDING THE CHILD APPLICANT? {{19143fe1-fb50-438e-ad1e-bcc468cd3d29}}")
    @CsvBindByName(column="List below each minor living in the household, their age, and their relationship to the child applicant. (Example: Child Applicant - 3 YEARS, Brother - 10 YEARS, Sister - 7 YEARS, Cousin - 7 YEARS) {{8c0269c4-eaa8-4b06-9feb-2b11a6ff0044}}")
    @CsvBindByName(column="Upload the birth certificate (state issued or foreign) or passport or visa or hospital record or state-issued ID for each minor listed as a sibling of the applicant child. Upload one of these required documents for ALL dependent children listed in the household. {{8376a3aa-2eee-4107-85ed-9873da18bc28}}")
    @CsvBindByName(column="Upload additional sibling birth certificates, if needed. {{7c600e76-583f-4436-b8c8-89a7ca1705be}}")
    @CsvBindByName(column="Are any adults included in your household count caring for any children with disabilities in the household? {{baca161c-a8da-4408-bb02-6c028c7d203c}}")
    @CsvBindByName(column="If Yes, adult only needs to be working 15 hours. Notes on caring for SWD: {{eb230bf2-67dc-4781-b6bd-6c6af80c2323}}")
    @CsvBindByName(column="Do you work at one of the centers/schools you ranked? {{1b346f97-ad79-4250-a08a-4a24a945253e}}")
    @CsvBindByName(column="Which center/school do you work at? {{e4bed81e-4db9-4966-9732-22198e91ed5b}}")
    @CsvBindByName(column="Does your child have a sibling attending any of the centers/schools you ranked? {{f8b7d2ef-565d-4998-939b-e61cc89a166a}}")
    @CsvBindByName(column="You can list up to 3 siblings. Sibling name #1: {{e6160d12-6624-4fec-ab57-391d50df2534}}")
    @CsvBindByName(column="Which center/school does sibling #1 attend? {{62a15dcb-906e-4acd-8b58-7fe87a1f26e5}}")
    @CsvBindByName(column="Sibling name #2: {{ccb95c5c-bb89-4f84-b070-bd24797f32cd}}")
    @CsvBindByName(column="Which center/school does sibling #2 attend? {{f7d308b3-6fd1-458d-826c-9d6e7f7581d8}}")
    @CsvBindByName(column="Sibling name #3: {{b29398d3-b800-43f2-a1ee-87a5cd746507}}")
    @CsvBindByName(column="Which center/school does sibling #3 attend? {{a21bb34f-796d-45a2-9c4d-4c71d83b5d3c}}")
    @CsvBindByName(column="Is the parent applicant an unmarried minor (under age 18)? {{972f39f4-f3aa-45c5-b7a8-d298f7e7a232}}")
    @CsvBindByName(column="Does your child receive SSI Benefits? {{f2c6684f-cb3d-4416-9fc0-dd86fc55ae53}}")
    @CsvBindByName(column="Upload a statement from the Social Security Administration verifying that the child listed on the application is a recipient of SSI benefits. {{dc216f53-fe2a-48cb-856b-67c357f9f2c7}}")
    @CsvBindByName(column="Does your child currently receive Child Care Assistance Program (CCAP) benefits? {{d70fc952-2934-4c0e-b746-ba8d9d1ec107}}")
    @CsvBindByName(column="Does your child currently have an Individualized Family Service Plans (IFSP) or are they being evaluated for special education services? {{71f7253f-7a8e-48b6-bce4-fc28d5420838}}")
    @CsvBindByName(column="Does your child receive Family Independence Temporary Assistance (FITAP) or Temporary Assistance to Needy Families (TANF) benefits? {{34400559-5f00-4445-ac61-c5d1d04e1b58}}")
    @CsvBindByName(column="Upload proof of benefits. {{0cc44aac-4638-4f6f-9026-a94b31efbb3b}}")
    @CsvBindByName(column="Does the parent/guardian receive Social Security Administration disability benefits, supplemental security income, or Veterans Administration disability benefits for a disability of at least 70 percent? {{f54e47cd-838e-4387-a06b-2aaf5873db9f}}")
    @CsvBindByName(column="Upload proof of benefits or doctor’s statement of disability with date of letter. All disability verification must include the name of the person, the dates of validity of those benefits, and the dollar value of those benefits, if applicable.  {{47cd324f-a446-4947-a864-9672bed7532b}}")
    @CsvBindByName(column="Is the applicant a child of a parent or guardian in active military service? {{e61b8c1c-bef4-417c-80ee-b49cc27cc03a}}")
    @CsvBindByName(column="Which language did your child learn first? {{6f9222c0-b4de-4427-a9c4-5699add06239}}")
    @CsvBindByName(column="Which language does your child use most often at home? {{96ca9027-2d10-4560-ba0a-ebed20c987d4}}	In what language do you most often speak to your child? {{efa0e6ce-d581-4bc1-a4fd-93d8b58bc9bc}}")
    @CsvBindByName(column="A Gifted IEP is required for your child to attend Hynes Lakeview. Do you have a Gifted and Talented evaluation or Gifted and Talented IEP approved by OPSB's Office of Child Search? {{e6a2cf80-e34d-4185-91e5-d70539f9463e}}")
    @CsvBindByName(column="[DOES NOT APPEAR ON APPLICATION] Please upload the Gifted and Talented IEP. {{0bd79982-e4b6-484b-bca1-4d2a47e3bc0e}}")
    @CsvBindByName(column="If yes, click the link below to request an administrative review {{5e5b5538-dedf-4a34-bf29-6abfd20ed95a}}")
    @CsvBindByName(column="If no, click the link below to schedule an evaluation. {{6934e6dd-5699-45ff-b7cf-e8368eee21c0}}")
    @CsvBindByName(column="Please rate your application experience on a scale of 1-5. {{e2785498-1a6f-4515-923e-dbed74717a48}}")
    @CsvBindByName(column="Provide any additional feedback on your application experience below. {{52a34525-ca2b-4cb3-ac8d-e284c6fac043}}")
    @CsvBindByName(column="Do you want to be contacted about jobs in early childhood? (either for yourself or someone you know) {{034af9c4-ae93-48c8-bf53-d3c39521345b}}")
    @CsvBindByName(column="Describe the parental status of the applicant's primary guardian. {{550ce129-9a8b-476c-b9b2-5b60e0a45bd2}}")
    @CsvBindByName(column="Does the applicant have a disability? {{17b00594-b2c2-4491-b19b-46125b1979d5}}")
    @CsvBindByName(column="Is the primary caregiver working, in training, not working, or in school? {{a1102a95-ddae-485e-9706-9dbbc302c9a7}}")
    @CsvBindByName(column="Does the child have social service needs? {{213a8c83-e218-42ed-a5bc-740a325358e5}}")
    @CsvBindByName(column="Homeless Status {{885fc44f-3b98-452f-8c6a-3258e630efbb}}")
    @CsvBindByName(column="Is the child applicant an English language learner? {{25bb9f7e-9146-4b49-8fac-101aa0d8781d}}")
    @CsvBindByName(column="Does the child have serious health problems? {{bc4d2eef-bdbb-4e75-b19b-48a780f9a195}}")
    @CsvBindByName(column="Have headstart services been provided to this family in the past? {{9f2a0b47-4f21-48ed-af6c-7c0e62ca5173}}")
    @CsvBindByName(column="Has this child been previously selected by a selection committee? {{aa75dc4c-3362-4ada-9271-ce4841a3216b}}")
    @CsvBindByName(column="Is sibling currently enrolled in EHS or HS center? {{d6b43f76-a441-44c2-be11-f6cf6467468d}}")
    @CsvBindByName(column="If yes, list center name {{f5b660ae-c39f-4957-9dbc-fed8dea82f0a}}")
    @CsvBindByName(column="Does the parent participate in Parents As Educators Kingsley House program? {{102d4312-b812-477c-a703-c7bc928e95c5}}")
    @CsvBindByName(column="Is a parent/guardian active military? {{66af33a9-a309-4a4a-a916-5bcbe6504a3d}}")
    @CsvBindByName(column="Is the applicant parent pregnant? {{e9d593bd-e5fe-431b-a133-fb7333114719}}")
    @CsvBindByName(column="Does the applicant parent participate in an EHS pregnant woman program? {{47ae17c7-41ab-4bb2-9361-4b0a2d96bb8e}}")
    @CsvBindByName(column="What is the child applicant's CCAP status? {{eccba5aa-9292-475c-9c1a-12d28bb4af0e}}")
    @CsvBindByName(column="COVID Effects {{cda1fab7-527e-4965-84fa-1119a126f781}}")
    @CsvBindByName(column="Is applicant a resident of Columbia Park in Gentilly? {{259c65fa-7177-4eaa-9a8a-b0f5eecfda65}}")
    @CsvBindByName(column="Is applicant's parent enrolled in Healthy Start? {{78ec6ef4-ce1b-4b97-a8dd-658cfe951790}}")
    @CsvBindByName(column="Has applicant's parent accepted a referral to Healthy Start? {{df5f7b13-82df-49f4-b2c0-1bdd048b51ad}}")
    @CsvBindByName(column="Has applicant's parent declined Healthy Start services? {{e25217fb-2fe2-4aae-a0f7-c12b203b706c}}")
    @CsvBindByName(column="Zip code {{0e6c71db-01ef-445b-b066-abacfa8adcc3}}")
    @CsvBindByName(column="Please select if any of the following intra-agency transfer requests apply {{3ea3da10-cdc3-4743-9c50-271975cd24b5}}")
    @CsvBindByName(column="Transfer center: {{907d0f3b-fd25-42b5-a117-97a691be9bd8}}")

    public static BaseCsvModel generateModel(Submission submission) throws JsonProcessingException {
        Map<String, Object> inputData = submission.getInputData();
        inputData.put("id", submission.getId());

        return mapper.convertValue(inputData, ECEApplicationCsvModel.class);
    }
}
