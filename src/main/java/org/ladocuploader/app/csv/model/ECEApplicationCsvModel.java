package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import formflow.library.data.Submission;

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
        "Admin notes on application {{1ea02d97-5a65-479a-9b6e-dd9fa317b9cf}}",
        "Admin reviewer(s) {{15c0a22d-a577-4c3c-b124-f9a39b70440e}}",
        "Choose the grade your child is applying for {{3c904015-43fd-4217-a865-23169ea5a692}}",
        "Select the option that best describes where you live. {{c2d9075b-ac79-4388-ae23-818321cc4fe7}}",
        "This questionnaire is intended to address the McKinney-Vento Act. Your child may be eligible for additional educational services.   Did the student receive McKinney Vento (Homeless) Services in a previous school district? {{222934d3-5329-4b37-9320-427b5a8e6936}}",
        "Is the student’s address a temporary living arrangement? {{407c0d8e-de5c-42e9-a618-a6e0e2d20ec6}}",
        "Is the temporary living arrangement due to loss of housing or economic hardship? {{e5ab6e29-9879-4b33-aad8-9e15ffd64de5}}",
        "Does the student have a disability or receive any special education-related services? {{f7281425-c8b9-4753-8632-9a763d75042a}}",
        "Where is the student currently living? {{d14e3cb2-10cc-4187-8162-aae75a0e6dec}}",
        "Other specific information about where the student is currently living: {{b17a8030-cbaa-4736-b192-0ef8ed27981d}}",
        "Does the student exhibit any behaviors that may interfere with his or her academic performance? {{f9413403-dc9f-4b87-9163-fd7a6d14c5d6}}",
        "Would you like assistance with uniforms, student records, school supplies, transportation, other? (Describe): {{79bf3ad3-1e2d-4232-8cb3-3228fa196ef9}}",
        "Migrant – Have you moved at any time during the past three (3) years to seek temporary or seasonal work in agriculture (including Poultry processing, dairy, nursery, and timber) or fishing? {{aff63010-7d24-44df-9f9b-7c17622fa1d3}}",
        "How many people, including children, are in your household? Only include all children, parents, guardians, and/or additional adults who provide financial support to the family. {{d47c7ef3-54a8-4a24-8ddd-c6e0697f01f6}}",
        "What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{7aaf8626-45c6-480a-9c81-5a1e940942ce}}",
        "What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{c3f7b04e-c5ab-49af-b4db-2ca357b1f52f}}",
        "What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{64e73b02-eb41-4654-a2ee-5394010c0346}}",
        "What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{2901c4ea-8c58-401a-92a4-079dd93baa72}}",
        "What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{f79e4403-bc39-45a5-82f7-be9e0b8c1a69}}",
        "What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{d85fe6d1-47a3-4b46-a75a-1f13ad8fc873}}",
        "What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{b3e0d48e-224d-4448-8424-ef48cca90342}}",
        "What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{f687b290-d6a0-41ea-9f8b-8312f8246671}}",
        "Select the option below that best describes your household: {{89272119-bed2-4208-83ef-3b51b0f2f1d4}}",
        "Provide 1 of 4 forms of verification documents listed below: {{5fefb0bc-2660-42c8-a81e-296cdf151f3c}}",
        "Verification document upload: {{90c598ed-41bf-44c8-8f73-519147b5705a}}",
        "Provide 1 of 4 forms of verification documents listed below: {{838e90e2-9d13-4e00-9257-deac29b88d4a}}",
        "Verification document upload: {{38b38730-97c0-4c26-9f61-43aacb425083}}",
        "Is the child you are applying for a twin and/or triplet? {{6e1fce05-5390-45c9-94e5-d5d9eb6f4770}}",
        "Please list the name(s) of the child’s twin/triplets. {{6e4d00b5-a6b7-4c5e-9edb-c7fe98fb651d}}",
        "Are you interested in taking a seat now (2023-24 school year)? The list of available programs is linked below. We’ll make offers as seats become available. There is limited availability. {{d827d5b3-9d13-4cde-a782-a14f26dc515f}}",
        "Upload your child's birth certificate. (For unborn children, provide a doctor’s note with the anticipated birth date. After birth, upload Official Birth certificate before enrollment.) {{c5d1175b-9cf8-4f0d-b30f-f1d401781810}}",
        "Is your name on the birth certificate? {{c7ed4ad7-865c-4538-ba31-1ff81f2a7d83}}",
        "If your name is not on the birth certificate, then you will also need to provide proof of custody. If you are unable to provide one of these documents right now, you will need to provide proof of custody before your child receives a center/school placement.   Please select the document you are uploading. {{51f013ab-a17c-4583-8bd7-7b6d04dbe589}}",
        "Please upload documentation of proof of custody. {{0b0e7d3f-3222-4b03-849d-33c071ecf081}}",
        "Upload the ID of the parent/guardian completing the application. {{0492133c-787e-41e9-b82f-aadc832e4982}}",
        "List below each adult living in the household who provides financial support to the family, their age, and their relationship to the child applicant.  (Example: Mother - 35 YEARS, Father - 35 YEARS, Aunt - 24 YEARS, Grandmother - 68 YEARS) {{919c6cdf-847c-4f9b-89e7-41b7f621d43c}}",
        "What is the number of minors (below 18 years old) living in the household, INCLUDING THE CHILD APPLICANT? {{e9e429cd-d2e5-47a6-b47b-bcaa1c968384}}",
        "List below each minor living in the household, their age, and their relationship to the child applicant.   (Example: Child Applicant - 3 YEARS, Brother - 10 YEARS, Sister - 7 YEARS, Cousin - 7 YEARS) {{c8527b1a-7d23-4a5f-8e13-98ace80154f6}}",
        "Upload the birth certificate (state issued or foreign) or passport or visa or hospital record or state-issued ID for each minor listed as a sibling of the applicant child.   Upload one of these required documents for ALL dependent children listed in the household. {{d3620389-d922-4fdf-b2cc-9557e5ad1914}}",
        "Upload additional sibling birth certificates, if needed. {{1f56cedb-c59f-47d8-b13d-43705f635fee}}",
        "Does your child have a sibling attending any of the centers/schools you ranked? {{e6f89229-7720-4110-ad6c-58fbe5782360}}",
        "You can list up to 3 siblings. Sibling name #1: {{6917d2f8-1768-45a3-969d-fd73a2962808}}",
        "Which center/school does sibling #1 attend? {{3f44e9b6-f108-4b32-99fc-3131318c1341}}",
        "Sibling name #2: {{c31335e9-b7aa-44af-97bf-f682b691a6bd}}",
        "Which center/school does sibling #2 attend? {{e79c32d7-1c32-4747-b63a-4f663cef1408}}",
        "Sibling name #3: {{ea9f28bf-2732-47f3-83fd-4df8f88be17d}}",
        "Which center/school does sibling #3 attend? {{3acee9b8-0e1a-46b5-851c-96be200ff732}}",
        "Do you work at one of the centers/schools you ranked? {{8702abb7-7b17-4fcf-8d4b-ee86cdb291fd}}",
        "Which center/school do you work at? {{1f531b96-aa68-4565-bb62-f054490173a2}}",
        "Is the parent applicant an unmarried minor (under age 18)? {{a6a9d72d-1841-494a-96e0-225c2d702969}}",
        "Does your child have an Individualized Family Service Plans (IFSP), or are they being evaluated for special education services? {{8289f79d-b67b-470e-90b9-1476e6c1ec0d}}",
        "Is your name listed on the residency documents that you will be providing? {{2755afa2-5950-4708-a8bb-2cceb57b47a9}}",
        "Proof of residency {{1d712bb1-2edb-4114-a7e9-cbe5fd022dcf}}",
        "Verified residency document #1 type: {{30c1e240-9eeb-44ef-a837-b2e85f6aa9f0}}",
        "Proof of residency #2. {{4833129b-ea62-4353-941b-984605b58d94}}",
        "Verified residency document #2 type: {{4b58f8e4-57fc-48f4-a4e8-c1582e464f5b}}",
        "Either the parent/guardian name must be on the residency documents or if the parent/guardian lives with another adult who is named on the residency documents, the parent/guardian must upload a signed letter from the person named on the residency documents stating that the parent/guardian lives at that same address.   If uploading a letter, parent/guardian must also upload acceptable proofs of residency in resident’s name and a photo of the resident's ID. {{3418eb6c-1095-4487-9364-3c171304cb38}} If uploading a letter, parent/guardian must also upload acceptable proofs of residency in resident’s name and a photo of the resident's ID.",
        "Is the applicant a child of a parent or guardian in active Military service? {{5b3bd7b9-f0c4-4a4c-aec1-cdbe90cdc47e}}",
        "Is Adult 1 (yourself) working? {{17619702-ceae-43b7-b75e-9a76fb34c1fa}}",
        "Please select the gender that best matches your SNAP application choice: {{7f4e46ef-510d-4beb-9e67-6364cbc80f96}}",
        "Please select the ethnicity that best matches your SNAP application choice: {{2c07b960-12e1-49ff-9e91-69bf302b9928}}",
        "Please select the race that best matches your SNAP application choice: {{65ee6216-ddf8-4c9e-b174-21484c19966c}}",
        "Pay statement upload #1 and #2 (dated within 45-60 days of filling out this application) {{f191345b-d3b7-4463-a65a-947a2504670a}}",
        "Pay statement upload #3 and #4, if needed (dated within 45-60 days of filling out this application) {{06ca5c9c-013f-4b10-afc6-e8ab67c20bb7}}",
        "Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed) {{601c0ecc-59b7-4b4f-aa75-6b0f08a6daf4}}",
        "I state that my income or support comes from: {{0cbeb4ca-b51d-42e5-9109-e6e1f35dc806}}",
        "Upload your most recent IRS Form 1099 {{d403974c-fc46-4162-aa92-94b99472e5b5}}",
        "If choosing 'Parents/family', attach a statement from person providing support {{b2874345-5b9a-4399-ae97-bf4b9f25b1df}}",
        "Describe your source of income {{05e03f1a-3592-460e-9970-03f6015612af}}",
        "Gross Income January: {{aab8bd29-e9c2-4701-a250-32e0c9448d8a}}",
        "Gross Income February: {{9e6d52f4-c8b2-4708-8073-8184c14ceaf1}}",
        "Gross Income March: {{587e3ae2-db23-492c-8a1a-1f769314a3d0}}",
        "Gross Income April: {{0f495951-ed03-49c6-be3d-0d461a647189}}",
        "Gross Income May: {{db4d86b0-5483-46c8-ac4d-4196f9ce4b49}}",
        "Gross Income June: {{229a5959-8731-4521-868d-54393dd846d1}}",
        "Gross Income July: {{07af931d-e74f-4f56-a26e-d0d7bccecd6e}}",
        "Gross Income August: {{3011be45-de05-4649-a7fe-9eac0fe74c74}}",
        "Gross Income September: {{9b94418e-a0f1-40fe-9740-ed5d92ea2e07}}",
        "Gross Income October: {{96104436-97f9-4b6b-9596-48bec6d73d49}}",
        "Gross Income November: {{51bc9013-0aba-4ccb-9b65-691693b65a3e}}",
        "Gross Income December: {{55abbef5-1595-4916-8e8e-468a616fee04}}",
        "My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{3d0e1ee7-629e-433c-b861-1bc337e770bc}}",
        "Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission {{cff9e036-1b9c-4c21-8e8e-2b7bbdf04efb}}",
        "Enter the number of months you have been without income: {{6485dab3-b8e0-4c44-9460-7811e9feec18}}",
        "I am (check all that apply) {{422428f3-d96b-4df7-8c24-8e09454ddd8a}}",
        "If 'Other' please describe your employment status {{f5cd0c9f-84b9-447d-a039-7f05d6d38072}}",
        "Is Adult 1 (yourself) in school, in a training program, or seeking work? {{c906066e-72bd-441a-9fef-6674d9b5fe1d}}",
        "Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement {{db824fb9-9ab1-4446-a0f3-4f55248a21cd}}",
        "Provide a school transcript, detailed school schedule, or letter from the registrar {{74af906d-47d9-4c15-b97b-7211f0162cdc}}",
        "Provide hours attending and training courses on organization’s letterhead {{993e132f-4ea5-4a31-8eff-c59e8b695ff4}}",
        "Provide a school transcript, detailed school schedule, or letter from the registrar {{7b15b6dd-4ec2-43e5-96f2-7b3a614da530}}",
        "Provide hours attending and training courses (or hours worked) on organization’s letterhead {{212b7374-a297-4bb8-ace7-2090c72124fa}}",
        "Is Adult 2 working? {{47ddf1bc-ff1a-4e98-9834-f54832895d34}}",
        "Pay statement upload #1 and #2 (Adult 2) (dated within 45-60 days of filling out this application) {{87f33654-e87f-41f8-988c-deeeb4647416}}",
        "Pay statement upload #3 and #4, if needed (dated within 45-60 days of filling out this application) {{71ef3e8e-b841-4aaf-bc3d-036a51b02243}}",
        "Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed) {{27903cf3-d0d2-4759-b40e-8f345574b39c}}",
        "I state that Adult 2's income or support comes from: {{6438e42a-bef9-4bd3-a8a3-e7fcdad4b83f}}",
        "Upload Adult 2's most recent IRS Form 1099 {{bfd21fe6-cbf7-4855-ad7a-67ce2b815503}}",
        "If choosing 'Parents/family', attach a statement from person providing support {{0ef5ac33-1ecb-46bd-aa9e-e365601a8c74}}",
        "Describe Adult 2's source of income {{b40c9e68-ca3d-4ef0-acd8-dfdc724311ea}}",
        "Gross Income January: {{74a69e52-52e8-4d09-93f1-626da0fcd398}}",
        "Gross Income February: {{1c55b27a-c2a4-4437-8d13-8d4568e3bc07}}",
        "Gross Income March: {{0b6a6fee-27d8-4410-be09-e6ba48d70283}}",
        "Gross Income April: {{211ed013-9694-4b3f-9d9c-302f999a89ff}}",
        "Gross Income May: {{49162cb9-e86e-43fc-8e73-803ff8d9b250}}",
        "Gross Income June: {{66574a30-2aa5-4d54-9caa-02db6e6d691a}}",
        "Gross Income July: {{4417328d-f559-4195-8b91-5b1fda521b18}}",
        "Gross Income August: {{7633a8fa-9af5-4026-bb0d-06ebcc33fa2f}}",
        "Gross Income September: {{6dc89db6-34fc-472b-b4d4-439b4b89ed11}}",
        "Gross Income October: {{0d6cd82c-63f3-46ec-a620-465956fc5eff}}",
        "Gross Income November: {{effd27e3-5412-46e2-908d-b649a96a1591}}",
        "Gross Income December: {{3db0878e-668e-4c19-aca5-6057dd82ddeb}}",
        "Adult 2's rent/house payments, utilities, food, and transportation expenses are being paid for by: {{31abf25d-b2f8-4feb-9e5e-d9ba69454384}}",
        "Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission {{fd9e7a34-3d1c-499d-b704-f6722bfa4d33}}",
        "Enter the number of months Adult 2 has been without income: {{a73d8a7c-5256-4343-b5f2-310a000cb6d4}}",
        "Adult 2 is (check all that apply) {{1310342e-8ef4-40f4-9a6a-0b59404c37e4}}",
        "If 'Other' please describe Adult 2's employment status {{abac3c58-b56c-405a-a2a8-f7a570204219}}",
        "Adult 2's rent/house payments, utilities, food, and transportation expenses are being paid for by: {{0adb8559-ed84-478c-bce4-5292b0ff555f}}",
        "Is Adult 2 in school, in a training program, or seeking work? {{688c3e9c-bca7-4caf-8408-22fc692271b7}}",
        "Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement {{c326e0ff-60d3-49da-9de4-a7f6789b1a67}}",
        "Provide a school transcript, detailed school schedule, or letter from the registrar {{4579e265-4a22-4d41-a1ea-895ee62e0b15}}",
        "Provide hours attending and training courses on organization’s letterhead {{89bf0070-ce81-486c-8a70-72640f441fc4}}",
        "Provide a school transcript, detailed school schedule, or letter from the registrar {{9e804287-61e9-4715-9844-91a93e7cb795}}",
        "Provide hours attending and training courses (or hours worked) on organization’s letterhead {{dea3408a-05c9-40dd-bed8-35474cecbc7d}}",
        "Is Adult 3 working? {{468fad1e-5a4c-4d87-890e-91668474c4e0}}",
        "Pay statement upload #1 and #2 (Adult 3) (dated within 45-60 days of filling out this application) {{069b045d-1271-4306-a7c6-f1cdbe896040}}",
        "Pay statement upload #3 and #4, if needed (dated within 45-60 days of filling out this application) {{85bdebc3-bb8b-472e-be37-b5297a1690d9}}",
        "Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed) {{fa85315c-06a3-4a5c-b1a8-d9db842f00fb}}",
        "I state that Adult 3's income or support comes from: {{48f422fc-9eae-40ed-ae19-e73db5d6277e}}",
        "Upload Adult 3's most recent IRS Form 1099 {{dbd84517-1506-4e50-9fe7-eb8be60ed1a9}}",
        "If choosing 'Parents/family', attach a statement from person providing support {{a7e68d63-b766-4fd1-b280-0b1a4a29ffd8}}",
        "Describe Adult 3's source of income {{10a54f62-7612-4b8c-9800-dcffcd66c3de}}",
        "Gross Income January: {{fd4d20d5-7421-4120-88b7-1efd63d9c5ca}}",
        "Gross Income February: {{4d520b52-732e-49fe-a600-d82c317c6d35}}",
        "Gross Income March: {{fe21e683-c94d-4a6b-b3ae-74443e77cbe7}}",
        "Gross Income April: {{531b3cb0-6c39-4f8a-8568-7837a84f79c2}}",
        "Gross Income May: {{f4591953-52d4-4eb8-a5f6-899b37f8f2c1}}",
        "Gross Income June: {{33442272-0086-41a2-8a68-6145eca89a61}}",
        "Gross Income July: {{7aee8e41-39f5-43a8-bea4-7cf57a984082}}",
        "Gross Income August: {{6965b859-21cb-4255-b8c4-0b76e73f9b35}}",
        "Gross Income September: {{0e87346b-c520-4f2f-aee7-eaabaff68bce}}",
        "Gross Income October: {{91b9f6ca-cdbd-49d6-a528-59e1b205b896}}",
        "Gross Income November: {{05c2ff9c-9385-4b6c-9b85-897d0bcafe5b}}",
        "Gross Income December: {{1e52ffa9-87ee-4175-8247-133a4af9a171}}",
        "Adult 3's rent/house payments, utilities, food, and transportation expenses are being paid for by: {{22b6ff4c-3626-4685-808d-c20589417623}}",
        "Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission {{83a1dd74-7ace-4320-8772-d1878510f613}}",
        "Enter the number of months Adult 3 has been without income: {{a938620e-eccb-46ca-9fcc-da5d3a27717e}}",
        "Adult 3 is (check all that apply) {{34514049-0ca3-4b20-987c-d67f6e80bd11}}",
        "If 'Other' please describe Adult 3's employment status {{a2fe0adc-82f8-4a19-9bf3-7d4ff19cfd5f}}",
        "Adult 3's rent/house payments, utilities, food, and transportation expenses are being paid for by: {{df83076f-5ab8-408a-8a7b-accb39b350cd}}",
        "Is Adult 3 in school, in a training program, or seeking work? {{37255c78-e51e-4d84-903d-67c12413a6fd}}",
        "Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement {{5a8b3446-0100-49ba-ae0e-fbc20d32b4da}}",
        "Provide a school transcript, detailed school schedule, or letter from the registrar {{4e2d9ea8-0bb5-4267-bf0a-fc587cebd050}}",
        "Provide hours attending and training courses on organization’s letterhead {{86468d35-aeed-4665-a1a8-98226d8cae11}}",
        "Provide a school transcript, detailed school schedule, or letter from the registrar {{c4308b05-e3cc-46ce-835f-590c09877352}}",
        "Provide hours attending and training courses (or hours worked) on organization’s letterhead {{8741b484-e94b-49bf-bce7-c79df9fb1f3a}}",
        "Does your child receive SSI Benefits? {{171a9df3-e089-4246-a2af-5eac7aff4f47}}",
        "Upload a statement from the Social Security Administration verifying that the child listed on the application is a recipient of SSI benefits. {{37d49f8a-2df8-4115-b25a-112d85c4a130}}",
        "Does your child receive Family Independence Temporary Assistance (FITAP) or Temporary Assistance to Needy Families (TANF) benefits? {{4f5a2666-b697-4eaa-a12e-1ff855cb17a7}}",
        "Upload proof of benefits. {{a503aabf-d04d-45fe-9b78-e8edb26d587f}}",
        "Does the parent/guardian receive Social Security Administration disability benefits, supplemental security income, or Veterans Administration disability benefits for a disability of at least 70 percent? {{82b334d4-b1b4-4e18-8f85-934c7ec78db9}}",
        "Upload proof of benefits. {{54d96e77-7849-4649-b132-8b24c46f766f}}",
        "Are any adults included in your household count caring for any children with disabilities in the household? {{15e5f39c-6646-4ac0-94b5-f1eebbc60919}}",
        "(Adult 1) Verified hours in school, training, or work {{51489324-6a7b-4e18-b75f-75963326358b}}",
        "(Adult 2) Verified hours in school, training, or work {{e15538cd-d48e-4541-b995-340f6f520a7c}}",
        "(Adult 3) Verified hours in school, training, or work {{f3a4e495-7486-4fcd-b312-a05b8f0562a5}}",
        "Verified income (Use only numbers, no words) {{0917090d-83ce-4054-81ac-e9239387bd31}}",
        "Which language did your child learn first? {{f7f65b41-91a2-42e1-811c-3436da705948}}",
        "Which language does your child use most often at home? {{d8b2caca-879e-4195-98df-6afbc236d7a3}}",
        "In what language do you most often speak to your child? {{cef9d1f8-9db3-416f-9482-3fb36c7e22f0}}",
        "Current phone number: {{17bc7264-cb53-49a7-a367-6a0f9cf54cf3}}",
        "Do you want to receive text communication from NOLA Public Schools? {{697f0625-718c-468d-b321-92f024469226}}",
        "Current email address: {{9f7b086b-2900-4015-aa44-74bae74f343f}}",
        "A Gifted IEP is required for your child to attend Hynes Charter School. Do you have a Gifted and Talented evaluation or Gifted and Talented IEP approved by OPSB's Office of Child Search? {{f3e226b5-dca5-4f71-ae0a-79ae3340e091}}",
        "If yes, click the link below to request an administrative review {{4eb5e881-a3d2-475f-b1dc-facdbf96bfca}}",
        "If no, click the link below to schedule an evaluation. {{4807522b-43a8-4a09-bad4-7a14b18d38f3}}",
        "Please rate your application experience on a scale of 1-5. {{a55c8687-4719-42c9-870b-f9d3915b00c8}}",
        "Provide any additional feedback on your application experience below. {{5e42b372-ed93-4e7f-8ed9-631f606426f7}}",
        "Do you want to be contacted about jobs in early childhood? (either for yourself or someone you know) {{1482e66b-6e47-46d4-b851-f15e84343803}}",
        "Does the child have social service needs? {{94d1937f-e704-49f7-90d3-a24d5b7d21ba}}",
        "Have Headstart services been provided to this family in the past? {{f922381c-26e1-4761-9091-b9af421269d2}}",
        "Does the parent participate in Parents As Educators Kingsley House program? {{fc392319-3bc2-49a4-8c4f-991a93897473}}",
        "Is applicant a resident of Columbia Park in Gentilly? {{a698808e-b97e-41e1-adda-9c4dd7257ab6}}",
        "Please select if any of the following intra-agency transfer requests apply {{49ed1658-ced3-4461-a45d-0c870a987f3a}}",
        "Transfer center: {{947ab5cd-8b1f-4b98-bd3b-71c27a33b7c8}}"
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
    @CsvBindByName(column="Admin Notes on application {{1ea02d97-5a65-479a-9b6e-dd9fa317b9cf}}")
    private String adminNotes;

    //  *** no questions for this one *** //
    @CsvBindByName(column="Admin reviewer(s) {{15c0a22d-a577-4c3c-b124-f9a39b70440e}}")
    private String adminReviewers;

    //  *** no questions for this one *** //
    @CsvBindByName(column="Choose the grade your child is applying for {{3c904015-43fd-4217-a865-23169ea5a692}}")
    private String chooseStudentGrade;

    // *** no questions for this one *** //
    @CsvBindByName(column="Select the option that best describes where you live. {{c2d9075b-ac79-4388-ae23-818321cc4fe7}}")
    private String descriptionOfLivingEnv;

    // *** no questions for this one *** //
    @CsvBindByName(column="This questionnaire is intended to address the McKinney-Vento Act. Your child may be eligible for additional educational services.   Did the student receive McKinney Vento (Homeless) Services in a previous school district? {{222934d3-5329-4b37-9320-427b5a8e6936}}")
    private String hadHomelessServicesInPreviousSchool;

    // Mapped to "noHomeAddress"
    @CsvBindByName(column="Is the student’s address a temporary living arrangement? {{407c0d8e-de5c-42e9-a618-a6e0e2d20ec6}}")
    private String isStudentAddressTemporaryLiving;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is the temporary living arrangement due to loss of housing or economic hardship? {{e5ab6e29-9879-4b33-aad8-9e15ffd64de5}}")
    private String isTempLivingDueToLossOfHousingOrEconomicHardship;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does the student have a disability or receive any special education-related services? {{f7281425-c8b9-4753-8632-9a763d75042a}}")
    private String doesStudentHaveDisabilityOrSpecEdServices;

    // *** no questions for this one *** //
    @CsvBindByName(column="Where is the student currently living? {{d14e3cb2-10cc-4187-8162-aae75a0e6dec}}")
    private String whereDoesStudentCurrentlyLive;

    // *** no questions for this one *** //
    @CsvBindByName(column="Other specific information about where the student is currently living: {{b17a8030-cbaa-4736-b192-0ef8ed27981d}}")
    private String specificsAboutWhereStudentCurrentlyLiving;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does the student exhibit any behaviors that may interfere with his or her academic performance? {{f9413403-dc9f-4b87-9163-fd7a6d14c5d6}}")
    private String doesStudentHaveBehaviorsThatAffectAcademics;

    // *** no questions for this one *** //
    @CsvBindByName(column="Would you like assistance with uniforms, student records, school supplies, transportation, other? (Describe): {{79bf3ad3-1e2d-4232-8cb3-3228fa196ef9}}")
    private String needAssistanceWithSchoolRelatedThings;

    // *** no questions for this one *** //
    @CsvBindByName(column="Migrant – Have you moved at any time during the past three (3) years to seek temporary or seasonal work in agriculture (including Poultry processing, dairy, nursery, and timber) or fishing? {{aff63010-7d24-44df-9f9b-7c17622fa1d3}}")
    private String hasFamilyMovedForAgriWork;

    // filled out
    @CsvBindByName(column="How many people, including children, are in your household? Only include all children, parents, guardians, and/or additional adults who provide financial support to the family. {{d47c7ef3-54a8-4a24-8ddd-c6e0697f01f6}}")
    private String howManyPeopleInHousehold;

    @CsvBindByName(column="What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{7aaf8626-45c6-480a-9c81-5a1e940942ce}}")
    private String monthlyHouseholdIncome;

    @CsvBindByName(column="What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{c3f7b04e-c5ab-49af-b4db-2ca357b1f52f}}")
    private String monthlyHouseholdIncome2;

    @CsvBindByName(column="What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{64e73b02-eb41-4654-a2ee-5394010c0346}}")
    private String monthlyHouseholdIncome3;

    @CsvBindByName(column="What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{2901c4ea-8c58-401a-92a4-079dd93baa72}}")
    private String monthlyHouseholdIncome4;

    @CsvBindByName(column="What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{f79e4403-bc39-45a5-82f7-be9e0b8c1a69}}")
    private String monthlyHouseholdIncome5;

    @CsvBindByName(column="What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{d85fe6d1-47a3-4b46-a75a-1f13ad8fc873}}")
    private String monthlyHouseholdIncome6;

    @CsvBindByName(column="What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{b3e0d48e-224d-4448-8424-ef48cca90342}}")
    private String monthlyHouseholdIncome7;

    @CsvBindByName(column="What is your monthly household income? (If your child has an IEP and you make below the maximum allowable income limit for your household size, choose your income here instead of indicating the IEP status.) {{f687b290-d6a0-41ea-9f8b-8312f8246671}}")
    private String monthlyHouseholdIncome8;

    // *** no questions for this one; don't have mapping either *** //
    @CsvBindByName(column="Select the option below that best describes your household: {{89272119-bed2-4208-83ef-3b51b0f2f1d4}}")
    private String householdDescription;

    @CsvBindByName(column="Provide 1 of 4 forms of verification documents listed below: {{5fefb0bc-2660-42c8-a81e-296cdf151f3c}}")
    private String verificationDocumentOneType;

    @CsvBindByName(column="Verification document upload: {{90c598ed-41bf-44c8-8f73-519147b5705a}}")
    private String verificationDocumentOne;

    @CsvBindByName(column="Provide 1 of 4 forms of verification documents listed below: {{838e90e2-9d13-4e00-9257-deac29b88d4a}}")
    private String verificationDocumentOneTypeDuplicate;

    @CsvBindByName(column="Verification document upload: {{38b38730-97c0-4c26-9f61-43aacb425083}}")
    private String verificationDocumentOneDuplicate;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is the child you are applying for a twin and/or triplet? {{6e1fce05-5390-45c9-94e5-d5d9eb6f4770}}")
    private String twinOrTriplet;

    // *** no questions for this one *** //
    @CsvBindByName(column="Please list the name(s) of the child’s twin/triplets. {{6e4d00b5-a6b7-4c5e-9edb-c7fe98fb651d}}")
    private String twinOrTripletName;

    // *** no questions for this one *** //
    @CsvBindByName(column="Are you interested in taking a seat now (2023-24 school year)? The list of available programs is linked below. We’ll make offers as seats become available. There is limited availability. {{d827d5b3-9d13-4cde-a782-a14f26dc515f}}")
    private String takeASeatCurrentSchoolYear;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload your child's birth certificate. (For unborn children, provide a doctor’s note with the anticipated birth date. After birth, upload Official Birth certificate before enrollment.) {{c5d1175b-9cf8-4f0d-b30f-f1d401781810}}")
    private String studentsBirthCertificateDocument;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is your name on the birth certificate? {{c7ed4ad7-865c-4538-ba31-1ff81f2a7d83}}")
    private String isParentNameOnBirthCertificate;

    // *** no questions for this one *** //
    @CsvBindByName(column="If your name is not on the birth certificate, then you will also need to provide proof of custody. If you are unable to provide one of these documents right now, you will need to provide proof of custody before your child receives a center/school placement.   Please select the document you are uploading. {{51f013ab-a17c-4583-8bd7-7b6d04dbe589}}")
    private String custodyProofDocumentType;

    // *** no questions for this one *** //
    @CsvBindByName(column="Please upload documentation of proof of custody. {{0b0e7d3f-3222-4b03-849d-33c071ecf081}}")
    private String custodyProofDocument;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload the ID of the parent/guardian completing the application. {{0492133c-787e-41e9-b82f-aadc832e4982}}")
    private String parentIdDocumentation;

    // *** cannot answer as we do not know relationship to child, only the applicant *** //
    @CsvBindByName(column="List below each adult living in the household who provides financial support to the family, their age, and their relationship to the child applicant.  (Example: Mother - 35 YEARS, Father - 35 YEARS, Aunt - 24 YEARS, Grandmother - 68 YEARS) {{919c6cdf-847c-4f9b-89e7-41b7f621d43c}}")
    private String adultsWhoProvideSupport;

    // mapped
    @CsvBindByName(column="What is the number of minors (below 18 years old) living in the household, INCLUDING THE CHILD APPLICANT? {{e9e429cd-d2e5-47a6-b47b-bcaa1c968384}}")
    private String numberOfMinors;

    // *** cannot answer as we do not know relationship to child, only the applicant *** //
    @CsvBindByName(column="List below each minor living in the household, their age, and their relationship to the child applicant.   (Example: Child Applicant - 3 YEARS, Brother - 10 YEARS, Sister - 7 YEARS, Cousin - 7 YEARS) {{c8527b1a-7d23-4a5f-8e13-98ace80154f6}}")
    List<String> minorsInHousehold;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload the birth certificate (state issued or foreign) or passport or visa or hospital record or state-issued ID for each minor listed as a sibling of the applicant child.   Upload one of these required documents for ALL dependent children listed in the household. {{d3620389-d922-4fdf-b2cc-9557e5ad1914}}")
    String siblingProofData;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload additional sibling birth certificates, if needed. {{1f56cedb-c59f-47d8-b13d-43705f635fee}}")
    String siblingAdditionalProofData;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does your child have a sibling attending any of the centers/schools you ranked? {{e6f89229-7720-4110-ad6c-58fbe5782360}}")
    String siblingAtSchoolCenterRanked;

    // *** no questions for this one *** //
    @CsvBindByName(column="You can list up to 3 siblings. Sibling name #1: {{6917d2f8-1768-45a3-969d-fd73a2962808}}")
    String siblingOneName;

    // *** no questions for this one *** //
    @CsvBindByName(column="Which center/school does sibling #1 attend? {{3f44e9b6-f108-4b32-99fc-3131318c1341}}")
    String siblingOneSchoolCenter;

    // *** no questions for this one *** //
    @CsvBindByName(column="Sibling name #2: {{c31335e9-b7aa-44af-97bf-f682b691a6bd}}")
    String siblingTwoName;

    // *** no questions for this one *** //
    @CsvBindByName(column="Which center/school does sibling #2 attend? {{e79c32d7-1c32-4747-b63a-4f663cef1408}}")
    String siblingTwoSchoolCenter;

    // *** no questions for this one *** //
    @CsvBindByName(column="Sibling name #3: {{ea9f28bf-2732-47f3-83fd-4df8f88be17d}}")
    String siblingThreeName;

    // *** no questions for this one *** //
    @CsvBindByName(column="Which center/school does sibling #3 attend? {{3acee9b8-0e1a-46b5-851c-96be200ff732}}")
    String siblingThreeSchoolCenter;

    // *** no questions for this one *** //
    @CsvBindByName(column="Do you work at one of the centers/schools you ranked? {{8702abb7-7b17-4fcf-8d4b-ee86cdb291fd}}")
    String doesApplicantWorksAtSchoolCenterTheyRanked;

    // *** no questions for this one *** //
    @CsvBindByName(column="Which center/school do you work at? {{1f531b96-aa68-4565-bb62-f054490173a2}}")
    String applicantWorksAt;

    @CsvCustomBindByName(column="Is the parent applicant an unmarried minor (under age 18)? {{a6a9d72d-1841-494a-96e0-225c2d702969}}", converter=UnmarriedMinorConverter.class)
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
    @CsvBindByName(column="Does your child have an Individualized Family Service Plans (IFSP), or are they being evaluated for special education services? {{8289f79d-b67b-470e-90b9-1476e6c1ec0d}}")
    String doesChildHaveIFSPOrBeingEvaluated;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is your name listed on the residency documents that you will be providing? {{2755afa2-5950-4708-a8bb-2cceb57b47a9}}")
    private String isYourNameOrSpouseNameOnResidencyDocuments;

    // *** no questions for this one *** //
    @CsvBindByName(column="Proof of residency {{1d712bb1-2edb-4114-a7e9-cbe5fd022dcf}}")
    private String proofOfResidencyDocumentOne;

    // *** no questions for this one *** //
    @CsvBindByName(column="Verified residency document #1 type: {{30c1e240-9eeb-44ef-a837-b2e85f6aa9f0}}")
    private String proofOfResidencyDocumentOneType;

    // *** no questions for this one *** //
    @CsvBindByName(column="Proof of residency #2. {{4833129b-ea62-4353-941b-984605b58d94}}")
    private String proofOfResidencyDocumentTwo;

    // *** no questions for this one *** //
    @CsvBindByName(column="Verified residency document #2 type: {{4b58f8e4-57fc-48f4-a4e8-c1582e464f5b}}")
    private String proofOfResidencyDocumentTwoType;

    // *** no questions for this one *** //
    @CsvBindByName(column="Either the parent/guardian name must be on the residency documents or if the parent/guardian lives with another adult who is named on the residency documents, the parent/guardian must upload a signed letter from the person named on the residency documents stating that the parent/guardian lives at that same address.   If uploading a letter, parent/guardian must also upload acceptable proofs of residency in resident’s name and a photo of the resident's ID. {{3418eb6c-1095-4487-9364-3c171304cb38}} If uploading a letter, parent/guardian must also upload acceptable proofs of residency in resident’s name and a photo of the resident's ID.")
    String residencyNotice;

    // *** cannot answer, only have veteran *** //
    @CsvBindByName(column="Is the applicant a child of a parent or guardian in active Military service? {{5b3bd7b9-f0c4-4a4c-aec1-cdbe90cdc47e}}")
    String isChildsParentGuardianInMilitaryService;

    // mapped
    @CsvBindByName(column="Is Adult 1 (yourself) working? {{17619702-ceae-43b7-b75e-9a76fb34c1fa}}")
    String isAdultOneWorking;

    // mapped
    @CsvBindByName(column="Please select the gender that best matches your SNAP application choice: {{7f4e46ef-510d-4beb-9e67-6364cbc80f96}}")
    private String sex;

    // mapped
    @CsvBindByName(column="Please select the ethnicity that best matches your SNAP application choice: {{2c07b960-12e1-49ff-9e91-69bf302b9928}}")
    private String ethnicitySelected;

    // mapped
    @CsvBindByName(column="Please select the race that best matches your SNAP application choice: {{65ee6216-ddf8-4c9e-b174-21484c19966c}}")
    private String raceSelected;

    // *** no questions for this one *** //
    @CsvBindByName(column="Pay statement upload #1 and #2 (dated within 45-60 days of filling out this application) {{f191345b-d3b7-4463-a65a-947a2504670a}}")
    String adultOnePayStatementOneAndTwo;

    // *** no questions for this one *** //
    @CsvBindByName(column="Pay statement upload #3 and #4, if needed (dated within 45-60 days of filling out this application) {{06ca5c9c-013f-4b10-afc6-e8ab67c20bb7}}")
    String adultOnePayStatementThreeAndFour;

    // *** no questions for this one *** //
    @CsvBindByName(column="Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed) {{601c0ecc-59b7-4b4f-aa75-6b0f08a6daf4}}")
    String adultOneEmployerLetter;

    // *** no questions for this one *** //
    @CsvBindByName(column="I state that my income or support comes from: {{0cbeb4ca-b51d-42e5-9109-e6e1f35dc806}}")
    String adultOneIncomeSupportComesFrom;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload your most recent IRS Form 1099 {{d403974c-fc46-4162-aa92-94b99472e5b5}}")
    String adultOneSelfEmployment1099;

    // *** no questions for this one *** //
    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support {{b2874345-5b9a-4399-ae97-bf4b9f25b1df}}")
    String adultOneSupportFromParentFamilyStatementDocument;

    // *** no questions for this one *** //
    @CsvBindByName(column="Describe your source of income {{05e03f1a-3592-460e-9970-03f6015612af}}")
    String adultOneDescribeSourceOfIncome;

    @CsvBindByName(column="Gross Income January: {{aab8bd29-e9c2-4701-a250-32e0c9448d8a}}")
    String adultOneGrossIncomeJanuary;
    @CsvBindByName(column="Gross Income February: {{9e6d52f4-c8b2-4708-8073-8184c14ceaf1}}")
    String adultOneGrossIncomeFebruary;
    @CsvBindByName(column="Gross Income March: {{587e3ae2-db23-492c-8a1a-1f769314a3d0}}")
    String adultOneGrossIncomeMarch;
    @CsvBindByName(column="Gross Income April: {{0f495951-ed03-49c6-be3d-0d461a647189}}")
    String adultOneGrossIncomeApril;
    @CsvBindByName(column="Gross Income May: {{db4d86b0-5483-46c8-ac4d-4196f9ce4b49}}")
    String adultOneGrossIncomeMay;
    @CsvBindByName(column="Gross Income June: {{229a5959-8731-4521-868d-54393dd846d1}}")
    String adultOneGrossIncomeJune;
    @CsvBindByName(column="Gross Income July: {{07af931d-e74f-4f56-a26e-d0d7bccecd6e}}")
    String adultOneGrossIncomeJuly;
    @CsvBindByName(column="Gross Income August: {{3011be45-de05-4649-a7fe-9eac0fe74c74}}")
    String adultOneGrossIncomeAugust;
    @CsvBindByName(column="Gross Income September: {{9b94418e-a0f1-40fe-9740-ed5d92ea2e07}}")
    String adultOneGrossIncomeSeptember;
    @CsvBindByName(column="Gross Income October: {{96104436-97f9-4b6b-9596-48bec6d73d49}}")
    String adultOneGrossIncomeOctober;
    @CsvBindByName(column="Gross Income November: {{51bc9013-0aba-4ccb-9b65-691693b65a3e}}")
    String adultOneGrossIncomeNovember;
    @CsvBindByName(column="Gross Income December: {{55abbef5-1595-4916-8e8e-468a616fee04}}")
    String adultOneGrossIncomeDecember;

    // *** no questions for this one *** //
    @CsvBindByName(column="My rent/house payments, utilities, food, and transportation expenses are being paid for by: {{3d0e1ee7-629e-433c-b861-1bc337e770bc}}")
    String adultOneHouseholdThingsPaidForByIrregularIncome; // TODO assumption it means irregular, not sure

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission {{cff9e036-1b9c-4c21-8e8e-2b7bbdf04efb}}")
    String adultOneUnemploymentBenefitsDocumentation;

    // *** no questions for this one *** //
    @CsvBindByName(column="Enter the number of months you have been without income: {{6485dab3-b8e0-4c44-9460-7811e9feec18}}")
    String adultOneNumberMonthsWithoutIncome;

    // *** no questions for this one *** //
    @CsvBindByName(column="I am (check all that apply) {{422428f3-d96b-4df7-8c24-8e09454ddd8a}}")
    String adultOneIAmOptions;

    @CsvBindByName(column="If 'Other' please describe your employment status {{f5cd0c9f-84b9-447d-a039-7f05d6d38072}}")
    String adultOneIAmOtherEmploymentStatusDescription;

    // mapped
    @CsvBindByName(column="Is Adult 1 (yourself) in school, in a training program, or seeking work? {{c906066e-72bd-441a-9fef-6674d9b5fe1d}}")
    String isAdultOneSchoolTrainingSeekingWork;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement {{db824fb9-9ab1-4446-a0f3-4f55248a21cd}}")
    String adultOneHIRERegistrationProof;

    // *** no questions for this one *** //
    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar {{74af906d-47d9-4c15-b97b-7211f0162cdc}}")
    String adultOneProofOfSchoolEnrollment;

    // *** no questions for this one *** //
    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead {{993e132f-4ea5-4a31-8eff-c59e8b695ff4}}")
    String adultOneHoursAttendingTrainingCoursesDocument;

    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar {{7b15b6dd-4ec2-43e5-96f2-7b3a614da530}}")
    String adultOneProofOfSchoolEnrollmentDuplicate;

    // *** no questions for this one *** //
    @CsvBindByName(column="Provide hours attending and training courses (or hours worked) on organization’s letterhead {{212b7374-a297-4bb8-ace7-2090c72124fa}}")
    String hoursAttendingOrWorked;

    // mapped
    @CsvBindByName(column="Is Adult 2 working? {{47ddf1bc-ff1a-4e98-9834-f54832895d34}}")
    String isAdultTwoWorking;

    // *** no questions for this one *** //
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 2) (dated within 45-60 days of filling out this application) {{87f33654-e87f-41f8-988c-deeeb4647416}}")
    String adultTwoPayStatementOneAndTwo;

    @CsvBindByName(column="Pay statement upload #3 and #4, if needed (dated within 45-60 days of filling out this application) {{71ef3e8e-b841-4aaf-bc3d-036a51b02243}}")
    String adultTwoPayStatementThreeAndFour;

    @CsvBindByName(column="Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed) {{27903cf3-d0d2-4759-b40e-8f345574b39c}}")
    String adultTwoEmployerLetter;

    @CsvBindByName(column="I state that Adult 2's income or support comes from: {{6438e42a-bef9-4bd3-a8a3-e7fcdad4b83f}}")
    String adultTwoIncomeSupportComesFrom;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload Adult 2's most recent IRS Form 1099 {{bfd21fe6-cbf7-4855-ad7a-67ce2b815503}}")
    String adultTwoSelfEmployment1099;

    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support {{0ef5ac33-1ecb-46bd-aa9e-e365601a8c74}}")
    String adultTwoParentsFamilySupport;

    @CsvBindByName(column="Describe Adult 2's source of income {{b40c9e68-ca3d-4ef0-acd8-dfdc724311ea}}")
    String adultTwoDescribeSourceOfIncome;

    @CsvBindByName(column="Gross Income January: {{74a69e52-52e8-4d09-93f1-626da0fcd398}}")
    String adultTwoGrossIncomeJan;
    @CsvBindByName(column="Gross Income February: {{1c55b27a-c2a4-4437-8d13-8d4568e3bc07}}")
    String adultTwoGrossIncomeFeb;
    @CsvBindByName(column="Gross Income March: {{0b6a6fee-27d8-4410-be09-e6ba48d70283}}")
    String adultTwoGrossIncomeMarch;
    @CsvBindByName(column="Gross Income April: {{211ed013-9694-4b3f-9d9c-302f999a89ff}}")
    String adultTwoGrossIncomeApril;
    @CsvBindByName(column="Gross Income May: {{49162cb9-e86e-43fc-8e73-803ff8d9b250}}")
    String adultTwoGrossIncomeMay;
    @CsvBindByName(column="Gross Income June: {{66574a30-2aa5-4d54-9caa-02db6e6d691a}}")
    String adultTwoGrossIncomeJune;
    @CsvBindByName(column="Gross Income July: {{4417328d-f559-4195-8b91-5b1fda521b18}}")
    String adultTwoGrossIncomeJuly;
    @CsvBindByName(column="Gross Income August: {{7633a8fa-9af5-4026-bb0d-06ebcc33fa2f}}")
    String adultTwoGrossIncomeAugust;
    @CsvBindByName(column="Gross Income September: {{6dc89db6-34fc-472b-b4d4-439b4b89ed11}}")
    String adultTwoGrossIncomeSeptember;
    @CsvBindByName(column="Gross Income October: {{0d6cd82c-63f3-46ec-a620-465956fc5eff}}")
    String adultTwoGrossIncomeOct;

    @CsvBindByName(column="Gross Income November: {{effd27e3-5412-46e2-908d-b649a96a1591}}")
    String adultTwoGrossIncomeNov;


    @CsvBindByName(column="Gross Income December: {{3db0878e-668e-4c19-aca5-6057dd82ddeb}}")
    String adultTwoGrossIncomeDec;

    // *** no questions for this one *** //
    @CsvBindByName(column="Adult 2's rent/house payments, utilities, food, and transportation expenses are being paid for by: {{31abf25d-b2f8-4feb-9e5e-d9ba69454384}}")
    String adultTwoHouseholdThingsPaidForByRegularIncome;

    @CsvBindByName(column="Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission {{fd9e7a34-3d1c-499d-b704-f6722bfa4d33}}")
    String adultTwoUnemploymentDocs;

    // *** no questions for this one *** //
    @CsvBindByName(column="Enter the number of months Adult 2 has been without income: {{a73d8a7c-5256-4343-b5f2-310a000cb6d4}}")
    String adultTwoNumberMonthsWithoutIncome;

    // *** no questions for this one *** //
    @CsvBindByName(column="Adult 2 is (check all that apply) {{1310342e-8ef4-40f4-9a6a-0b59404c37e4}}")
    String adultTwoIAmOptions;

    // *** no questions for this one *** //
    @CsvBindByName(column="If 'Other' please describe Adult 2's employment status {{abac3c58-b56c-405a-a2a8-f7a570204219}}")
    String adultTwoIAmOtherEmploymentStatusDescription;

    @CsvBindByName(column="Adult 2's rent/house payments, utilities, food, and transportation expenses are being paid for by: {{0adb8559-ed84-478c-bce4-5292b0ff555f}}")
    String adultTwoExpenses;

    // mapped
    @CsvBindByName(column="Is Adult 2 in school, in a training program, or seeking work? {{688c3e9c-bca7-4caf-8408-22fc692271b7}}")
    String isAdultTwoSchoolTrainingSeekingWork;

    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement {{c326e0ff-60d3-49da-9de4-a7f6789b1a67}}")
    String adultTwoHire;

    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar {{4579e265-4a22-4d41-a1ea-895ee62e0b15}}")
    String adultTwoTranscript;

    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead {{89bf0070-ce81-486c-8a70-72640f441fc4}}")
    String adultTwoTrainingCourses;

    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar {{9e804287-61e9-4715-9844-91a93e7cb795}}")
    String adultTwoTranscriptDuplicate;

    @CsvBindByName(column="Provide hours attending and training courses (or hours worked) on organization’s letterhead {{dea3408a-05c9-40dd-bed8-35474cecbc7d}}")
    String adultTwoTrainingCoursesOrHours;

    // mapped
    @CsvBindByName(column="Is Adult 3 working? {{468fad1e-5a4c-4d87-890e-91668474c4e0}}")
    String isAdultThreeWorking;

    // *** no questions for this one *** //
    @CsvBindByName(column="Pay statement upload #1 and #2 (Adult 3) (dated within 45-60 days of filling out this application) {{069b045d-1271-4306-a7c6-f1cdbe896040}}")
    String adultThreePayStatementOneAndTwo;

    @CsvBindByName(column="Pay statement upload #3 and #4, if needed (dated within 45-60 days of filling out this application) {{85bdebc3-bb8b-472e-be37-b5297a1690d9}}")
    String adultThreePayStatementThreeAndFour;

    @CsvBindByName(column="Employer letter (stating where adult is employed, work hours, rate of pay, start date and signature of employer with date signed) {{fa85315c-06a3-4a5c-b1a8-d9db842f00fb}}")
    String adultThreeEmployerLetter;

    // *** no questions for this one *** //
    @CsvBindByName(column="I state that Adult 3's income or support comes from: {{48f422fc-9eae-40ed-ae19-e73db5d6277e}}")
    String adultThreeIncomeSupportComesFrom;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload Adult 3's most recent IRS Form 1099 {{dbd84517-1506-4e50-9fe7-eb8be60ed1a9}}")
    String adultThreeSelfEmployment1099;

    @CsvBindByName(column="If choosing 'Parents/family', attach a statement from person providing support {{a7e68d63-b766-4fd1-b280-0b1a4a29ffd8}}")
    String adultThreeParentsFamily;

    // *** no questions for this one *** //
    @CsvBindByName(column="Describe Adult 3's source of income {{10a54f62-7612-4b8c-9800-dcffcd66c3de}}")
    String adultThreeDescribeSourceOfIncome;

    @CsvBindByName(column="Gross Income January: {{fd4d20d5-7421-4120-88b7-1efd63d9c5ca}}")
    String adultThreeGrossIncomeJan;
    @CsvBindByName(column="Gross Income February: {{4d520b52-732e-49fe-a600-d82c317c6d35}}")
    String adultThreeGrossIncomeFeb;
    @CsvBindByName(column="Gross Income March: {{fe21e683-c94d-4a6b-b3ae-74443e77cbe7}}")
    String adultThreeGrossIncomeMarch;
    @CsvBindByName(column="Gross Income April: {{531b3cb0-6c39-4f8a-8568-7837a84f79c2}}")
    String adultThreeGrossIncomeApril;
    @CsvBindByName(column="Gross Income May: {{f4591953-52d4-4eb8-a5f6-899b37f8f2c1}}")
    String adultThreeGrossIncomeMay;
    @CsvBindByName(column="Gross Income June: {{33442272-0086-41a2-8a68-6145eca89a61}}")
    String adultThreeGrossIncomeJun;
    @CsvBindByName(column="Gross Income July: {{7aee8e41-39f5-43a8-bea4-7cf57a984082}}")
    String adultThreeGrossIncomeJuly;
    @CsvBindByName(column="Gross Income August: {{6965b859-21cb-4255-b8c4-0b76e73f9b35}}")
    String adultThreeGrossIncomeAug;
    @CsvBindByName(column="Gross Income September: {{0e87346b-c520-4f2f-aee7-eaabaff68bce}}")
    String adultThreeGrossIncomeSep;
    @CsvBindByName(column="Gross Income October: {{91b9f6ca-cdbd-49d6-a528-59e1b205b896}}")
    String adultThreeGrossIncomeOct;
    @CsvBindByName(column="Gross Income November: {{05c2ff9c-9385-4b6c-9b85-897d0bcafe5b}}")
    String adultThreeGrossIncomeNov;

    @CsvBindByName(column="Gross Income December: {{1e52ffa9-87ee-4175-8247-133a4af9a171}}")
    String adultThreeGrossIncomeDec;

    // *** no questions for this one *** //
    @CsvBindByName(column="Adult 3's rent/house payments, utilities, food, and transportation expenses are being paid for by: {{22b6ff4c-3626-4685-808d-c20589417623}}")
    String adultThreeHouseholdThingsPaidForByIrregularIncome; // TODO assumption it means irregular, not sure

    @CsvBindByName(column="Upload documentation of your Unemployment Benefits: a Monetary Determination letter from the Workforce Commission {{83a1dd74-7ace-4320-8772-d1878510f613}}")
    String adultThreeUnemploymentDocs;



    // *** no questions for this one *** //
    @CsvBindByName(column="Enter the number of months Adult 3 has been without income: {{a938620e-eccb-46ca-9fcc-da5d3a27717e}}")
    String adultThreeNumberMonthsWithoutIncome;

    // *** no questions for this one *** //
    @CsvBindByName(column="Adult 3 is (check all that apply) {{34514049-0ca3-4b20-987c-d67f6e80bd11}}")
    String adultThreeIAmOptions;

    // *** no questions for this one *** //
    @CsvBindByName(column="If 'Other' please describe Adult 3's employment status {{a2fe0adc-82f8-4a19-9bf3-7d4ff19cfd5f}}")
    String adultThreeIAmOtherEmploymentStatusDescription;

    @CsvBindByName(column="Adult 3's rent/house payments, utilities, food, and transportation expenses are being paid for by: {{df83076f-5ab8-408a-8a7b-accb39b350cd}}")
    String adultThreeRentHousePayments;

    // mapped
    @CsvBindByName(column="Is Adult 3 in school, in a training program, or seeking work? {{37255c78-e51e-4d84-903d-67c12413a6fd}}")
    String isAdultThreeSchoolTrainingSeekingWork;

    @CsvBindByName(column="Upload proof of HIRE account registration with date of registration OR proof of unemployment pay statement {{5a8b3446-0100-49ba-ae0e-fbc20d32b4da}}")
    String adultThreeHire;

    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar {{4e2d9ea8-0bb5-4267-bf0a-fc587cebd050}}")
    String adultThreeTranscript;

    @CsvBindByName(column="Provide hours attending and training courses on organization’s letterhead {{86468d35-aeed-4665-a1a8-98226d8cae11}}")
    String adultThreeHours;

    @CsvBindByName(column="Provide a school transcript, detailed school schedule, or letter from the registrar {{c4308b05-e3cc-46ce-835f-590c09877352}}")
    String adultThreeTranscriptDuplicate;

    @CsvBindByName(column = "Provide hours attending and training courses (or hours worked) on organization’s letterhead {{8741b484-e94b-49bf-bce7-c79df9fb1f3a}}")
    String adultThreeHoursDuplicate;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does your child receive SSI Benefits? {{171a9df3-e089-4246-a2af-5eac7aff4f47}}")
    String doesChildReceiveSSI;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload a statement from the Social Security Administration verifying that the child listed on the application is a recipient of SSI benefits. {{37d49f8a-2df8-4115-b25a-112d85c4a130}}")
    String ssiVerificationDocument;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does your child receive Family Independence Temporary Assistance (FITAP) or Temporary Assistance to Needy Families (TANF) benefits? {{4f5a2666-b697-4eaa-a12e-1ff855cb17a7}}")
    String doesChildReceiveFITAPorTANF;

    // *** no questions for this one *** //
    @CsvBindByName(column="Upload proof of benefits. {{a503aabf-d04d-45fe-9b78-e8edb26d587f}}")
    String benefitsDocumentation;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does the parent/guardian receive Social Security Administration disability benefits, supplemental security income, or Veterans Administration disability benefits for a disability of at least 70 percent? {{82b334d4-b1b4-4e18-8f85-934c7ec78db9}}")
    String doesParentGuardianReceiveSSDorSupplementalSecurityIncomeOrVADisability;

    @CsvBindByName(column="Upload proof of benefits. {{54d96e77-7849-4649-b132-8b24c46f766f}}")
    String socialSecurityProof;

    // *** no questions for this one *** //
    @CsvBindByName(column="Are any adults included in your household count caring for any children with disabilities in the household? {{15e5f39c-6646-4ac0-94b5-f1eebbc60919}}")
    String areAnyAdultsInHouseholdCaregiversForChildWithDisabilitiesInHousehold;

    // *** no questions for this one *** //
    @CsvBindByName(column="(Adult 1) Verified hours in school, training, or work {{51489324-6a7b-4e18-b75f-75963326358b}}")
    String adultOneVerifiedHoursInSchoolTrainingWork;

    // *** no questions for this one *** //
    @CsvBindByName(column="(Adult 2) Verified hours in school, training, or work {{e15538cd-d48e-4541-b995-340f6f520a7c}}")
    String adultTwoVerifiedHoursInSchoolTrainingWork;

    // *** no questions for this one *** //
    @CsvBindByName(column="(Adult 3) Verified hours in school, training, or work {{f3a4e495-7486-4fcd-b312-a05b8f0562a5}}")
    String adultThreeVerifiedHoursInSchoolTrainingWork;

    // *** no questions for this one *** //
    @CsvBindByName(column="Verified income (Use only numbers, no words) {{0917090d-83ce-4054-81ac-e9239387bd31}}")
    String verifiedIncome;

    // *** no questions for this one *** //
    @CsvBindByName(column="Which language did your child learn first? {{f7f65b41-91a2-42e1-811c-3436da705948}}")
    String childsFirstLanguage;

    // *** no questions for this one *** //
    @CsvBindByName(column="Which language does your child use most often at home? {{d8b2caca-879e-4195-98df-6afbc236d7a3}}")
    String childsPreferredLangAtHome;

    // *** no questions for this one *** //
    @CsvBindByName(column="In what language do you most often speak to your child? {{cef9d1f8-9db3-416f-9482-3fb36c7e22f0}}")
    String languageSpeakToChild;

    // mapped
    @CsvBindByName(column="Current phone number: {{17bc7264-cb53-49a7-a367-6a0f9cf54cf3}}")
    private String phoneNumber;

    // *** no questions for this one *** //
    @CsvBindByName(column="Do you want to receive text communication from NOLA Public Schools? {{697f0625-718c-468d-b321-92f024469226}}")
    private String allowTextCommunicationFromNolaPS;

    // mapped
    @CsvBindByName(column="Current email address: {{9f7b086b-2900-4015-aa44-74bae74f343f}}")
    private String emailAddress;

    // *** no questions for this one *** //
    @CsvBindByName(column="A Gifted IEP is required for your child to attend Hynes Charter School. Do you have a Gifted and Talented evaluation or Gifted and Talented IEP approved by OPSB's Office of Child Search? {{f3e226b5-dca5-4f71-ae0a-79ae3340e091}}")
    String hasGiftedIEP;

    @CsvBindByName(column="If yes, click the link below to request an administrative review {{4eb5e881-a3d2-475f-b1dc-facdbf96bfca}}")
    String giftedIEPYes;

    @CsvBindByName(column="If no, click the link below to schedule an evaluation. {{4807522b-43a8-4a09-bad4-7a14b18d38f3}}")
    String giftedIEPNo;

    // *** no questions for this one *** //
    @CsvBindByName(column="Please rate your application experience on a scale of 1-5. {{a55c8687-4719-42c9-870b-f9d3915b00c8}}")
    String applicationFeedbackExperienceRating;

    // *** no questions for this one *** //
    @CsvBindByName(column="Provide any additional feedback on your application experience below. {{5e42b372-ed93-4e7f-8ed9-631f606426f7}}")
    String applicationFeedbackAdditionalInfo;

    // *** no questions for this one *** //
    @CsvBindByName(column="Do you want to be contacted about jobs in early childhood? (either for yourself or someone you know) {{1482e66b-6e47-46d4-b851-f15e84343803}}")
    String okayToContactAboutJobsInEarlyChildhood;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does the child have social service needs? {{94d1937f-e704-49f7-90d3-a24d5b7d21ba}}")
    String doesChildHaveSocialServiceNeeds;

    // *** no questions for this one *** //
    @CsvBindByName(column="Have Headstart services been provided to this family in the past? {{f922381c-26e1-4761-9091-b9af421269d2}}")
    String hasFamilyHadHeadStartServicesPreviously;

    // *** no questions for this one *** //
    @CsvBindByName(column="Does the parent participate in Parents As Educators Kingsley House program? {{fc392319-3bc2-49a4-8c4f-991a93897473}}")
    String doesParentParticipateKingslyHouseProgram;

    // *** no questions for this one *** //
    @CsvBindByName(column="Is applicant a resident of Columbia Park in Gentilly? {{a698808e-b97e-41e1-adda-9c4dd7257ab6}}")
    String isApplicantResidentOfColumbiaParkGentilly;

    // *** no questions for this one *** //
    @CsvBindByName(column="Please select if any of the following intra-agency transfer requests apply {{49ed1658-ced3-4461-a45d-0c870a987f3a}}")
    String interAgencyTransferRequests;

    // *** no questions for this one *** //
    @CsvBindByName(column="Transfer center: {{947ab5cd-8b1f-4b98-bd3b-71c27a33b7c8}}")
    String transferCenter;

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
        eceDataMap.put("sex", inputData.getOrDefault("sex", ""));
        eceDataMap.put("ethnicitySelected", inputData.getOrDefault("ethnicitySelected", ""));
        eceDataMap.put("raceSelected", inputData.getOrDefault("raceSelected", ""));
        eceDataMap.put("maritalStatus", inputData.getOrDefault("maritalStatus", ""));
        eceDataMap.put("phoneNumber", inputData.getOrDefault("phoneNumber", ""));
        eceDataMap.put("emailAddress", inputData.getOrDefault("emailAddress", ""));
        eceDataMap.put("id", submission.getId());

        int householdMemberCountProvidingSupport = (int) incomeList
                .stream()
                .map(map -> map.get("householdMemberJobAdd"))
                .distinct()
                .count();
        eceDataMap.put("howManyPeopleInHousehold", householdMemberCountProvidingSupport);

        IncomeCalculator incomeCalculator = new IncomeCalculator(submission);
        double totalHouseholdMonthlyIncome = incomeCalculator.totalFutureEarnedIncome();
        eceDataMap.put("monthlyHouseholdIncome", totalHouseholdMonthlyIncome);

         // Get adults in household
        List<Map<String, Object>> adults = new ArrayList<>();
        for (Map<String, Object> householdMember : householdList){
            try {
                if (HouseholdUtilities.isMember18orOlder(
                                    Integer.parseInt((String) householdMember.get("householdMemberBirthYear")),
                                    Integer.parseInt((String) householdMember.get("householdMemberBirthMonth")),
                                    Integer.parseInt((String) householdMember.get("householdMemberBirthDay")))){
                    adults.add(householdMember);
                }
            } catch (NumberFormatException e){
                log.error("Unable to work with household member {}'s birthday ({}/{}/{}): {}",
                        householdMember.get("householdMemberFirstName"),
                        (String)householdMember.get("householdMemberBirthDay"),
                        (String)householdMember.get("householdMemberBirthMonth"),
                        (String)householdMember.get("householdMemberBirthYear"),
                        e.getMessage());
            }
        }

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
