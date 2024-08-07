package org.ladocuploader.app.csv.model;

import static org.ladocuploader.app.utils.SubmissionUtilities.FIVE_YEARS_AGO;
import static org.ladocuploader.app.utils.SubmissionUtilities.MM_DD_YYYY;
import static org.ladocuploader.app.utils.SubmissionUtilities.ONE_YEAR_AGO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import formflow.library.data.Submission;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.ladocuploader.app.csv.CsvBindByNameOrder;
import org.ladocuploader.app.csv.converters.PhoneNumberConverter;

@Getter
@Setter
@CsvBindByNameOrder({
        "Date",
        "First name",
        "Last name",
        "Phone number",
        "Email address",
        "ZIP code",
        "Do any of the following apply to you or anyone living in your home? None of the above",
        "A pregnant or recently pregnant woman",
        "An infant",
        "A child from 1 to 5 years old",
        "Has anyone in your home ever received WIC in Louisiana?",
        "Preferred language"
})
public class WICApplicationCsvModel extends BaseCsvModel {


    @CsvBindByName(column="Date")
    private String submittedAt;

    @CsvBindByName(column="First name")
    private String firstName;
    
    @CsvBindByName(column="Last name")
    private String lastName;
    
    @CsvCustomBindByName(column="Phone number", converter = PhoneNumberConverter.class)
    private String phoneNumber;

    @CsvBindByName(column="Email address")
    private String emailAddress;

    @CsvBindByName(column="ZIP code")
    private String homeAddressZipCode;
    
    @CsvBindByName(column="Do any of the following apply to you or anyone living in your home? None of the above")
    private String notPregnantNoInfantNoChild;
    
    @CsvBindByName(column="A pregnant or recently pregnant woman")
    private String hasPregnancyInHousehold;

    @CsvBindByName(column="An infant")
    private String householdHasInfant;

    @CsvBindByName(column="A child from 1 to 5 years old")
    private String householdHasChildUnderFive;
     
    @CsvBindByName(column="Has anyone in your home ever received WIC in Louisiana?")
    private String wicBefore = "I'm not sure";
    
    @CsvBindByName(column="Preferred language")
    private String preferredLanguage;
    
    public static BaseCsvModel generateModel(Submission submission) throws JsonProcessingException {
        Map<String, Object> inputData = submission.getInputData();
        Map<String, Object> objectData = new HashMap<>();

        OffsetDateTime date = submission.getSubmittedAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        objectData.put("submittedAt", date.format(formatter));
        objectData.put("firstName", inputData.get("firstName"));
        objectData.put("lastName", inputData.get("lastName"));
        objectData.put("phoneNumber", inputData.get("phoneNumber"));
        objectData.put("emailAddress", inputData.get("emailAddress"));
        objectData.put("homeAddressZipCode", inputData.get("homeAddressZipCode"));
        
        boolean hasPregnancy = "true".equals(inputData.getOrDefault("pregnancyInd", "false"));
        objectData.put("hasPregnancyInHousehold", hasPregnancy ? "Yes" : "No");
        
        boolean hasInfant = householdHasInfant(submission);
        objectData.put("householdHasInfant", hasInfant ? "Yes" : "No");
        
        boolean hasChildUnderFive = householdHasChildUnderFive(submission);
        objectData.put("householdHasChildUnderFive", hasChildUnderFive ? "Yes" : "No");
        
        String notPregnantNoInfantNoChild = !hasPregnancy && !hasInfant && !hasChildUnderFive ? "None of the above" : "";
        objectData.put("notPregnantNoInfantNoChild", notPregnantNoInfantNoChild);
        
        objectData.put("preferredLanguage", inputData.getOrDefault("languageRead", "English"));

        WICApplicationCsvModel wicModel = mapper.convertValue(objectData, WICApplicationCsvModel.class);
        wicModel.setSubmissionId(submission.getId());
        return wicModel;
    }

    private static boolean householdHasChildUnderFive(Submission submission) {
        var household = submission.getInputData().get("household");
        if (household != null) {
            for (Map<String, Object> member : ((List<Map<String, Object>>) household)) {
                if ("child".equals(member.get("householdMemberRelationship"))) {
                    var day = member.get("householdMemberBirthDay");
                    var year = member.get("householdMemberBirthYear");
                    var month = member.get("householdMemberBirthMonth");
                    LocalDate birthdate = LocalDate.parse("%s/%s/%s".formatted(month, day, year), MM_DD_YYYY);
                    if (birthdate.isAfter(FIVE_YEARS_AGO) && birthdate.isBefore(ONE_YEAR_AGO)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    
    private static boolean householdHasInfant(Submission submission) {
        var household = submission.getInputData().get("household");
        if (household != null) {
            for (Map<String, Object> member : ((List<Map<String, Object>>) household)) {
                if ("child".equals(member.get("householdMemberRelationship"))) {
                    var day = member.get("householdMemberBirthDay");
                    var year = member.get("householdMemberBirthYear");
                    var month = member.get("householdMemberBirthMonth");
                    LocalDate birthdate = LocalDate.parse("%s/%s/%s".formatted(month, day, year), MM_DD_YYYY);
                    if (birthdate.isAfter(ONE_YEAR_AGO)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
