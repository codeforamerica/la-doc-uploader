package org.ladocuploader.app.submission;

import com.mixpanel.mixpanelapi.ClientDelivery;
import com.mixpanel.mixpanelapi.MessageBuilder;
import com.mixpanel.mixpanelapi.MixpanelAPI;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MixpanelTracker {

  private final MessageBuilder messageBuilder;

  public MixpanelTracker(@Value("${form-flow.mixpanel.api-key}") String apiToken) {
    messageBuilder = new MessageBuilder(apiToken);
  }

  public void trackWithProfile(String sessionId, String eventName, Map<String, Object> properties) {
    try {
      JSONObject event = messageBuilder.event(sessionId, eventName, new JSONObject(properties));
      JSONObject userProfile = messageBuilder.set(sessionId, new JSONObject(properties));

      ClientDelivery delivery = new ClientDelivery();
      delivery.addMessage(event);
      delivery.addMessage(userProfile);
      new MixpanelAPI().deliver(delivery);
    } catch (IOException e) {
      log.warn("Issue sending mixpanel tracking event", e);
    }
  }
}
