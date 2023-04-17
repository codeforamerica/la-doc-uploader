package org.ladocuploader.app.submission.actions;

import org.ladocuploader.app.submission.MixpanelTracker;
import org.springframework.stereotype.Component;

@Component
public class TrackClientSuccessEvent extends TrackClientInfo {

  public TrackClientSuccessEvent(MixpanelTracker mixpanelTracker) {
    super(mixpanelTracker);
  }

  @Override
  String getEventName() {
    return "doc_uploader_client_details_success";
  }
}
