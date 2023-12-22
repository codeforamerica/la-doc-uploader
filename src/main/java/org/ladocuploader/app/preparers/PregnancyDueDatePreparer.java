package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.PdfMap;
import formflow.library.pdf.SingleField;
import formflow.library.pdf.SubmissionField;
import formflow.library.pdf.SubmissionFieldPreparer;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.ladocuploader.app.utils.SubmissionUtilities.getHouseholdMemberFullnameByUUID;

@Component
public class PregnancyDueDatePreparer implements SubmissionFieldPreparer {

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> result = new HashMap<>();
    var pregnancies = (List) submission.getInputData().getOrDefault("pregnancies[]", emptyList());
    if (!pregnancies.isEmpty()) {
      List<String> pregnantMemberNames = new ArrayList<>();
      List<String> pregnantMemberDueDates = new ArrayList<>();
      for (var uuid : pregnancies) {
        var day = submission.getInputData().get("dayPregnancyDueDate_wildcard_%s".formatted(uuid));
        var year = submission.getInputData().get("yearPregnancyDueDate_wildcard_%s".formatted(uuid));
        var month = submission.getInputData().get("monthPregnancyDueDate_wildcard_%s".formatted(uuid));
        pregnantMemberDueDates.add("%s/%s/%s".formatted(month, day, year));

        var pregnantMemberName = getHouseholdMemberFullnameByUUID((String) uuid, submission.getInputData());
        pregnantMemberNames.add(pregnantMemberName);
      }
      result.put("pregnantMemberNames", new SingleField("pregnantMemberNames", Strings.join(pregnantMemberNames, ','), null));
      result.put("pregnantMemberDueDates", new SingleField("pregnantMemberDueDates", Strings.join(pregnantMemberDueDates, ','), null));
    }

    return result;
  }
}
