package org.ladocuploader.app.csv.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import formflow.library.data.Submission;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.csv.CsvBindByNameOrder;
import org.ladocuploader.app.csv.converters.AddressStreetConverter;
import org.ladocuploader.app.csv.converters.HouseholdBirthDateConverter;
import org.ladocuploader.app.utils.HouseholdUtilities;

@Getter
@Setter
@JsonTypeName("student")
@Slf4j
@CsvBindByNameOrder({"active", "reference_id", "first_name", "last_name", "street_address", "city", "state", "zip_code", "birth_date"})
public class StudentCsvModel extends BaseCsvModel {

    @CsvBindByName(column="active", required=true)
    private Boolean active = true;

    @CsvBindByName(column="reference_id")
    private String id; // uuid of student from household information

    @CsvBindByName(column="first_name")
    private String firstName; // student's first name

    @CsvBindByName(column="middle_name")
    private String middleName; // unset in system

    @CsvBindByName(column="last_name")
    private String lastName; // student's last name

    @CsvCustomBindByName(column="street_address", converter=AddressStreetConverter.class)
    private Map<String, String> homeAddressStreet = new HashMap<>();

    @CsvBindByName(column="city")
    private String homeAddressCity;

    @CsvBindByName(column="state")
    private String homeAddressState;

    @CsvBindByName(column="zip_code")
    private String homeAddressZipCode;

    @CsvCustomBindByName(column="birth_date", converter= HouseholdBirthDateConverter.class)
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
                //String relationship = (String) member.get("householdMemberRelationship");
                //if (relationship == null || !relationship.equalsIgnoreCase("child")) {
                //    continue;
               // }

                // are they even eligible?  if not, skip them
                if (!HouseholdUtilities.isMemberEceEligible(member, inputData)){
                    continue;
                }

                Map<String, Object> studentData = new HashMap<>();
                studentData.put("id", member.get("uuid"));
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
