package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.CheckboxField;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.ladocuploader.app.utils.SubmissionUtilities.PDF_EDUCATION_MAP;

@Component
public class ApplicantDetailsPreparer implements SubmissionFieldPreparer {

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> results = new HashMap<>();

    Map<String, Object> inputData = submission.getInputData();

    // Convert "who does this apply to" responses to a boolean indicator
    Map.of("students[]", "applicantStudentInd",
            "nonCitizens[]", "applicantNonCitizenInd",
            "homeless[]", "applicantHomelessInd")
        .forEach((key, value) -> {
          var inputs = (List<String>) inputData.getOrDefault(key, new ArrayList<>());
          var ind = inputs != null && inputs.contains("you") ? "Yes" : "No";
          results.put(value, new SingleField(value, ind, null));
        });

    // Format birthday
    var birthday = Stream.of("birthMonth", "birthDay", "birthYear")
        .map(inputData::get)
        .reduce((e, c) -> e + "/" + c)
        .get();
    results.put("applicantBirthday", new SingleField("applicantBirthdayFormatted", (String) birthday, null));

    var educationStatus = inputData.get("highestEducation");
    results.put("highestEducation", new SingleField("highestEducationFormatted", PDF_EDUCATION_MAP.get(educationStatus), null));

    // If they said they don't want us to collect this info, don't pass it on, even if they had previously set it.
    // This check helps if they went back and forward in the pages changing values. The data that prepares these fields is
    // the default OneToManyPreparer in the ffl. This code just overwrites the fields to _unset_ them.
    if (submission.getInputData().getOrDefault("permissionToAskAboutRace", "false").equals("false")) {
      results.put("raceSelected", new CheckboxField("raceSelected", List.of(), null));
      results.put("ethnicitySelected", new SingleField("ethnicitySelected", "", null));
    }

    return results;
  }
}
