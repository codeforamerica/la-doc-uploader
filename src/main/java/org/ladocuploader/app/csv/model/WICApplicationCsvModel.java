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
public class WICApplicationCsvModel extends BaseCsvModel {


    @CsvBindByName(column="reference_id")
    private String id;

    public static BaseCsvModel generateModel(Submission submission) throws JsonProcessingException {
        Map<String, Object> inputData = submission.getInputData();
        inputData.put("id", submission.getId());

        WICApplicationCsvModel wicModel = mapper.convertValue(inputData, WICApplicationCsvModel.class);
        wicModel.setSubmissionId(submission.getId());
        return wicModel;
    }
}
