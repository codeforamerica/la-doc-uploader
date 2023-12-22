package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static formflow.library.inputs.FieldNameMarkers.DYNAMIC_FIELD_MARKER;
import static java.util.Collections.emptyList;
import static org.ladocuploader.app.utils.SubmissionUtilities.getHouseholdMemberFullnameByUUID;

@Component
public class StudentDetailsPreparer implements SubmissionFieldPreparer {

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> result = new HashMap<>();
    var students = (List) submission.getInputData().getOrDefault("students[]", emptyList());
    if (!students.isEmpty()) {
      var i = 1;
      for (var studentUUID : students) {
        var studentName = getHouseholdMemberFullnameByUUID((String) studentUUID, submission.getInputData());
        var schoolName = (String) submission.getInputData().get("schoolName%s%s".formatted(DYNAMIC_FIELD_MARKER, studentUUID));
        var attendance = (String) submission.getInputData().get("schoolEnrollmentLevel%s%s".formatted(DYNAMIC_FIELD_MARKER, studentUUID));

        result.put("studentName" + i, new SingleField("studentName", studentName, i));
        result.put("schoolName" + i, new SingleField("schoolName", schoolName, i));
        result.put("attendance" + i, new SingleField("attendance", attendance, i));
        i++;
      }
    }
    return result;
  }
}
