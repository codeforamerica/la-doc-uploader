package org.ladocuploader.app.preparers;

import formflow.library.data.Submission;
import formflow.library.pdf.*;
import org.ladocuploader.app.data.enums.Parish;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class NoPermanentAddressMailingAddressPreparer implements SubmissionFieldPreparer {

  @Override
  public Map<String, SubmissionField> prepareSubmissionFields(Submission submission, PdfMap pdfMap) {
    Map<String, SubmissionField> results = new HashMap<>();

    Map<String, Object> inputData = submission.getInputData();

    boolean noHomeAddress = inputData.getOrDefault("noHomeAddress[]", List.of("false")).equals(List.of("true"));
    boolean noMailingaddress = inputData.getOrDefault("mailingAddressStreetAddress1", "").equals("") &&
            inputData.getOrDefault("mailingAddressCity", "").equals("") &&
            inputData.getOrDefault("mailingAddressState", "").equals("") &&
            inputData.getOrDefault("mailingAddressZipCode", "").equals("");

    if (noHomeAddress && noMailingaddress) {
      // get parish address and map to mailing address
      String selectedParish = (String) submission.getInputData().get("parish");
      Parish parishDetails = Parish.valueOf(selectedParish);
      results.put("mailingAddressZipCode", new SingleField("mailingAddressZipCode", parishDetails.getMailingAddressZipcode(), null));
      results.put("mailingAddressState", new SingleField("mailingAddressState", parishDetails.getMailingAddressState(), null));
      results.put("mailingAddressCity", new SingleField("mailingAddressCity", parishDetails.getMailingAddressCity(), null));
      results.put("mailingAddressStreetAddress1", new SingleField("mailingAddressStreetAddress1", parishDetails.getMailingAddress1(), null));
      results.put("mailingAddressStreetAddress2", new SingleField("mailingAddressStreetAddress2", parishDetails.getMailingAddress2(), null));
    }

    return results;
  }
}
