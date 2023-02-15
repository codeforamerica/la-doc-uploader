package org.ladocuploader.app.journeys;

import static org.assertj.core.api.Assertions.assertThat;

import org.ladocuploader.app.utils.AbstractBasePageTest;
import org.junit.jupiter.api.Test;

public class LaDocUploadFlowJourneyTest extends AbstractBasePageTest {

  @Test
  void fullUbiFlow() {
    // Landing screen
    assertThat(testPage.getTitle()).isEqualTo("Louisiana Document Uploader");
    testPage.clickButton("Upload documents");

    // Client Info
    assertThat(testPage.getTitle()).isEqualTo("Match Info");
  }
}
