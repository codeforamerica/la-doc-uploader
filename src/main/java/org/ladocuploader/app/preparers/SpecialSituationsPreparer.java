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
import static org.ladocuploader.app.utils.StringUtilities.joinWithCommaAndSpace;
import static org.ladocuploader.app.utils.SubmissionUtilities.householdMemberFullName;

@Component
public class SpecialSituationsPreparer implements SubmissionFieldPreparer {

  private static final List<String> INPUTS = List.of(
      "outOfStateBenefitsRecipients",
      "veterans",
      "fostersAgedOut");

  private static final String OUT_OF_STATE_PREFIX = "outOfStateBenefitsStates_wildcard_";

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> result = new HashMap<>();

    Map<String, List<String>> inputToNames = new HashMap<>();
    List<String> outOfStateBenefitsStates = new ArrayList<>();
    Map<String, List<String>> inputToUUIDs = new HashMap<>();
    for (String input : INPUTS) {
      List inputValue = (List) submission.getInputData().getOrDefault(input + "[]", emptyList());
      inputToUUIDs.put(input, inputValue);
      inputToNames.put(input, new ArrayList<>());
    }

    var members = (List<Map<String, Object>>) submission.getInputData().get("household");
    if (members != null) {
      for (var member : members) {
        var uuid = (String) member.get("uuid");
        var memberName = householdMemberFullName(member);
        // TODO: search for out of state benefits wildcard
        String outOfStateBenefitsValue = (String) submission.getInputData().getOrDefault(OUT_OF_STATE_PREFIX + uuid, null);
        if (outOfStateBenefitsValue != null) {
          outOfStateBenefitsStates.add(outOfStateBenefitsValue);
        }

        for (String input : INPUTS) {
          if (inputToUUIDs.get(input).contains(uuid)) {
            inputToNames.get(input).add(memberName);
          }
        }

      }
    }

    // Check single-member household and format fields
    for (String input : INPUTS) {
      List<String> names = inputToNames.get(input);
      if (inputToUUIDs.get(input).contains("you")) {
        names.add("%s %s".formatted(submission.getInputData().get("firstName"), submission.getInputData().get("lastName")));
      }
      if (!names.isEmpty()) {
        result.put(input + "Names", new SingleField(input + "Names", joinWithCommaAndSpace(names), null));
      }
    }
    String outOfStateBenefitsYou = (String) submission.getInputData().getOrDefault(OUT_OF_STATE_PREFIX + "you", null);
    // TODO: check if there is a state for wildcard_you
    if (outOfStateBenefitsYou != null) {
      outOfStateBenefitsStates.add(outOfStateBenefitsYou);
    }
    result.put("outOfStateBenefitsStates", new SingleField("outOfStateBenefitsStates", joinWithCommaAndSpace(outOfStateBenefitsStates), null));

    return result;
  }
}
