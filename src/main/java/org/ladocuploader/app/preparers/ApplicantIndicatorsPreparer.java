package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ApplicantIndicatorsPreparer implements SubmissionFieldPreparer {

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    var students = (List<String>) submission.getInputData().getOrDefault("students[]", new ArrayList<>());
    String applicantStudentInd = students != null && students.contains("you") ? "Yes" : "No";

    var nonCitizens = (List<String>) submission.getInputData().getOrDefault("nonCitizens[]", new ArrayList<>());
    String applicantCitizenInd = nonCitizens != null && nonCitizens.contains("you") ? "No" : "Yes";

    var homeless = (List<String>) submission.getInputData().getOrDefault("homeless[]", new ArrayList<>());
    String applicantHomelessInd = homeless != null && homeless.contains("you") ? "Yes" : "No";

    return Map.of(
        "applicantStudentInd", new SingleField("applicantStudentInd", applicantStudentInd, null),
        "applicantCitizenInd", new SingleField("applicantCitizenInd", applicantCitizenInd, null),
        "applicantHomelessInd", new SingleField("applicantHomelessInd", applicantHomelessInd, null)
    );
  }
}
