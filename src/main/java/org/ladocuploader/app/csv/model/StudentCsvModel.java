package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import formflow.library.data.Submission;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.csv.converters.AddressStreetConverter;
import org.ladocuploader.app.csv.converters.HouseholdBirthDateConverter;

@Getter
@Setter
@JsonTypeName("student")
@Slf4j
public class StudentCsvModel extends BaseCsvModel {

    @CsvBindByName(column="active", required=true)
    private Boolean active = true;

    @CsvBindByName(column="reference_id")
    private String id; // uuid of student

    @CsvBindByName(column="first_name", required=true)
    private String firstName; // student's first name

    @CsvBindByName(column="last_name", required=true)
    private String lastName; // student's last name

    @CsvCustomBindByName(column="street_address", required=true, converter=AddressStreetConverter.class)
    private Map<String, String> homeAddressStreet = new HashMap<>();

    @CsvBindByName(column="city", required=true)
    private String homeAddressCity;

    @CsvBindByName(column="state", required=true)
    private String homeAddressState;

    @CsvBindByName(column="zip_code", required=true)
    private String homeAddressZipCode;

    @CsvCustomBindByName(column="birth_date", required=true, converter= HouseholdBirthDateConverter.class)
    private Map<String, Integer> birthDate = new HashMap<>();

    @JsonSetter(value="memberBirthDay")
    private void setMemberBirthDay(final String day) {
        try {
            if (day != null) {
                birthDate.put("day", Integer.valueOf(day));
            }
        } catch (NumberFormatException e) {
            log.error("JSON Mapping: Unable to set member birth day, as value '{}' is bad.", day);
        }
    }

    @JsonSetter(value="memberBirthMonth")
    private void setMemberBirthMonth(final String month) {
        try {
            if (month != null) {
                birthDate.put("month", Integer.valueOf(month));
            }
        } catch (NumberFormatException e) {
            log.error("JSON Mapping: Unable to set member birth month, as value '{}' is bad.", month);
        }
    }

    @JsonSetter(value="memberBirthYear")
    private void setMemberBirthYear(final String year) {
        try {
            if (year != null) {
                birthDate.put("year", Integer.valueOf(year));
            }
        } catch (NumberFormatException e) {
            log.error("JSON Mapping: Unable to set member birth year, as value '{}' is bad.", year);
        }
    }

    @JsonSetter(value="homeAddressStreetAddress1")
    private void setHomeAddress1(final String address) {
        if (address != null) {
            homeAddressStreet.put("address1", address);
        }
    }

    @JsonSetter(value="homeAddressStreetAddress2")
    private void setHomeAddress2(final String address) {
        if (address != null) {
            homeAddressStreet.put("address2", address);
        }
    }

    public static List<BaseCsvModel> generateModel(Submission submission){
        Map<String, Object> inputData = submission.getInputData();
        List<BaseCsvModel> students = new ArrayList<>();
        List<Map<String, Object>> household = (List)inputData.get("household");

        if (household != null && !household.isEmpty()) {
            for (Map<String, Object> member : household) {
                String relationship = (String) member.get("householdMemberRelationship");
                if (relationship == null || !relationship.equalsIgnoreCase("child")) {
                    continue;
                }

                Map<String, Object> studentData = new HashMap<>();
                studentData.put("id", submission.getId());
                studentData.put("firstName", member.get("householdMemberFirstName"));
                studentData.put("lastName", member.get("householdMemberLastName"));
                studentData.put("memberBirthDay", member.get("householdMemberBirthDay"));
                studentData.put("memberBirthMonth", member.get("householdMemberBirthMonth"));
                studentData.put("memberBirthYear", member.get("householdMemberBirthYear"));
                studentData.put("homeAddressStreetAddress1", inputData.get("homeAddressStreetAddress1"));
                studentData.put("homeAddressStreetAddress2", inputData.get("homeAddressStreetAddress2"));
                studentData.put("homeAddressCity", inputData.get("homeAddressCity"));
                studentData.put("homeAddressState", inputData.get("homeAddressState"));
                studentData.put("homeAddressZipCode", inputData.get("homeAddressZipCode"));
                StudentCsvModel studentModel = mapper.convertValue(studentData, StudentCsvModel.class);
                studentModel.setSubmissionId(submission.getId());
                students.add(studentModel);
            }
        }
        return students;
    }
}
