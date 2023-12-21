package org.ladocuploader.app.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RecoverySubmissionIDs implements Serializable {
  private final Map<String, UUID> recoverySubmissionIdMap = new HashMap<>();

  public UUID getFlowSubmissionId(String flow) {
    return recoverySubmissionIdMap.get(flow);
  }

  public void setFlowSubmissionId(String flow, UUID submissionId) {
    recoverySubmissionIdMap.put(flow, submissionId);
  }
}
