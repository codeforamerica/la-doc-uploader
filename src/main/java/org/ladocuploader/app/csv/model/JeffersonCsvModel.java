package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
import org.ladocuploader.app.csv.converters.AddressStreetConverter;
import org.ladocuploader.app.csv.converters.HouseholdBirthDateConverter;
import org.ladocuploader.app.csv.converters.PhoneNumberConverter;
import org.ladocuploader.app.utils.HouseholdUtilities;
import org.ladocuploader.app.utils.IncomeCalculator;

@Getter
@Setter
@JsonTypeName("jefferson")
@Slf4j
@CsvBindByNameOrder({"cfa_reference","Are you currently pregnant and planning to apply for your unborn child? *","First Name","Last Name","Gender","Birthdate","Race","Street Address","Street Address 2","City","State","Zip Code","Applicant First Name","Applicant Last Name","Cell Phone", "Main Phone","Work Phone","Email","Total Household Number","Applicant Gross Income","Do any of the child's legal guardians (who live in your household) have a disability?","How many of the child's legal guardians (who live in your household) are either working, in school, in a training program, or seeking work?"})
public class JeffersonCsvModel extends BaseCsvModel {

    @CsvBindByName(column="cfa_reference")
    private String id;
    
    @CsvBindByName(column="Are you currently pregnant and planning to apply for your unborn child? *")
    private String pregnant;

    @CsvBindByName(column="First Name")
    private String childFirstName; 

    @CsvBindByName(column="Last Name")
    private String childLastName;
    
    @CsvBindByName(column="Gender")
    private String childGender;

    @CsvCustomBindByName(column="Birthdate", converter= HouseholdBirthDateConverter.class)
    private Map<String, Integer> childBirthDate = new HashMap<>();
    
    @CsvBindByName(column="Race")
    private String childRace;

    @CsvBindByName(column="Street Address")
    private String homeAddressStreet;
    
    @CsvBindByName(column="Street Address 2")
    private String homeAddressStreet2;

    @CsvBindByName(column="City")
    private String homeAddressCity;

    @CsvBindByName(column="State")
    private String homeAddressState;

    @CsvBindByName(column="Zip Code")
    private String homeAddressZipCode;

    @CsvBindByName(column="Applicant First Name")
    private String firstName;

    @CsvBindByName(column="Applicant Last Name")
    private String lastName;

    @CsvCustomBindByName(column="Cell Phone", converter = PhoneNumberConverter.class)
    private String cellPhoneNumber;

    @CsvCustomBindByName(column="Main Phone", converter = PhoneNumberConverter.class)
    private String phoneNumber;

    @CsvCustomBindByName(column="Work Phone", converter = PhoneNumberConverter.class)
    private String workPhoneNumber;

    @CsvBindByName(column="Email")
    private String emailAddress;
    
    @CsvBindByName(column="Total Household Number")
    private String totalHouseholdNumber;
    
    @CsvBindByName(column="Applicant Gross Income")
    private String applicantGrossIncome;
    
    @CsvBindByName(column="Do any of the child's legal guardians (who live in your household) have a disability?")
    private String disability;
    
    @CsvBindByName(column="How many of the child's legal guardians (who live in your household) are either working, in school, in a training program, or seeking work?")
    private String guardiansWorking;
    
    
    @JsonSetter(value="memberBirthDay")
    private void setMemberBirthDay(final String day) {
        try {
            if (day != null) {
                childBirthDate.put("day", Integer.valueOf(day));
            }
        } catch (NumberFormatException e) {
            log.error("JSON Mapping: Unable to set member birth day, as value '{}' is bad.", day);
        }
    }

    @JsonSetter(value="memberBirthMonth")
    private void setMemberBirthMonth(final String month) {
        try {
            if (month != null) {
                childBirthDate.put("month", Integer.valueOf(month));
            }
        } catch (NumberFormatException e) {
            log.error("JSON Mapping: Unable to set member birth month, as value '{}' is bad.", month);
        }
    }

    @JsonSetter(value="memberBirthYear")
    private void setMemberBirthYear(final String year) {
        try {
            if (year != null) {
                childBirthDate.put("year", Integer.valueOf(year));
            }
        } catch (NumberFormatException e) {
            log.error("JSON Mapping: Unable to set member birth year, as value '{}' is bad.", year);
        }
    }

    public static BaseCsvModel generateModel(Submission submission){
        Map<String, Object> inputData = submission.getInputData();
        Map<String, Object> jeffersonDataMap = new HashMap<>();
        
        jeffersonDataMap.put("id", submission.getId());
        boolean hasPregnancy = "true".equals(inputData.getOrDefault("pregnancyInd", "false"));
        jeffersonDataMap.put("pregnant", hasPregnancy ? "Yes" : "No");

        List<Map<String, Object>> householdList = (List) inputData.getOrDefault("household", List.of());
        householdList.stream().filter(HouseholdUtilities::isMemberCutoffEligible).findFirst().ifPresent(member -> {
            jeffersonDataMap.put("childFirstName", member.get("householdMemberFirstName"));
            jeffersonDataMap.put("childLastName", member.get("householdMemberLastName"));
            jeffersonDataMap.put("childGender", member.get("householdMemberSex"));
            jeffersonDataMap.put("memberBirthDay", member.get("householdMemberBirthDay"));
            jeffersonDataMap.put("memberBirthMonth", member.get("householdMemberBirthMonth"));
            jeffersonDataMap.put("memberBirthYear", member.get("householdMemberBirthYear"));
            if (submission.getInputData().containsKey("householdMemberRace_wildcard_" + member.get("uuid") + "[]")) {
                jeffersonDataMap.put("childRace", String.join(", ", (List) submission.getInputData().get("householdMemberRace_wildcard_" + member.get("uuid") + "[]")));
            }
        });
        
        jeffersonDataMap.put("homeAddressStreet", inputData.get("homeAddressStreetAddress1"));
        jeffersonDataMap.put("homeAddressStreet2", inputData.getOrDefault("homeAddressStreetAddress2", ""));
        jeffersonDataMap.put("homeAddressCity", inputData.get("homeAddressCity"));
        jeffersonDataMap.put("homeAddressState", inputData.get("homeAddressState"));
        jeffersonDataMap.put("homeAddressZipCode", inputData.get("homeAddressZipCode"));
        jeffersonDataMap.put("firstName", inputData.get("firstName"));
        jeffersonDataMap.put("lastName", inputData.get("lastName"));
        jeffersonDataMap.put("cellPhoneNumber", inputData.getOrDefault("cellPhoneNumber", ""));
        jeffersonDataMap.put("phoneNumber", inputData.getOrDefault("phoneNumber", ""));
        jeffersonDataMap.put("workPhoneNumber", inputData.getOrDefault("workPhoneNumber", ""));
        jeffersonDataMap.put("emailAddress", inputData.getOrDefault("emailAddress", ""));
        jeffersonDataMap.put("totalHouseholdNumber", householdList.size() + 1); // +1 for the applicant

        IncomeCalculator incomeCalculator = new IncomeCalculator(submission);
        
        jeffersonDataMap.put("applicantGrossIncome", incomeCalculator.applicantTotalFutureEarnedIncome());
        boolean guardianHasDisability = "true".equals(inputData.getOrDefault("guardiansHaveDisabilityInd", "false"));
        jeffersonDataMap.put("disability", guardianHasDisability ? "Yes" : "No");
        jeffersonDataMap.put("guardiansWorking", inputData.getOrDefault("adultsWorking", ""));
        JeffersonCsvModel jeffersonModel = mapper.convertValue(jeffersonDataMap, JeffersonCsvModel.class);
        jeffersonModel.setSubmissionId(submission.getId());
        return jeffersonModel;
    }
}
