package org.ladocuploader.app.csv.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;
import formflow.library.data.Submission;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Student extends ParentStudentShared {
    @CsvBindByName(column = "first_name", required=true)
    private String firstName; // use id of applicant

    @CsvBindByName(column="last_name", required=true)
    private String lastName; // id of subflow member

    @CsvBindByName(column="birth_date", required=true)
    private String birthDate;

    public static BaseCsvModel generateModel(Submission submission){
        Map<String, Object> inputData = submission.getInputData();
        inputData.put("id", submission.getId());

        return mapper.convertValue(inputData, Student.class);
    }

}
