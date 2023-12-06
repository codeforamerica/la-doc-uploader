package org.ladocuploader.app.submission.actions;

import formflow.library.config.submission.Action;
import formflow.library.data.Submission;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static formflow.library.inputs.FieldNameMarkers.DYNAMIC_FIELD_MARKER;

/**
 *
 */
@Component
@Slf4j
public class StoreSSNsForHouseholdMembers implements Action {

  @Override
  public void run(Submission submission) {
    Map<String, Object> inputData = submission.getInputData();
    ArrayList<LinkedHashMap> householdMembers = (ArrayList) inputData.get("household");

    for (LinkedHashMap hhmember : householdMembers) {
      String ssnKey = "householdMemberSsn" + DYNAMIC_FIELD_MARKER + hhmember.get("uuid");
      if (inputData.containsKey(ssnKey)) {
        hhmember.put("householdMemberSsn",inputData.get(ssnKey));
      }
    }
  }

}
