package org.ladocuploader.app.cli;

import java.util.Arrays;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CronUtility {
  private final ConfigurableApplicationContext context;

  public CronUtility(ConfigurableApplicationContext context) {
    this.context = context;
  }

  public void closeApplicationContext() {
    String[] profiles = context.getEnvironment().getActiveProfiles();
    if (!Arrays.asList(profiles).contains("test")) {
      context.close();
    }
  }
}
