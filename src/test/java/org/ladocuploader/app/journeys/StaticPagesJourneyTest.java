package org.ladocuploader.app.journeys;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;
import org.ladocuploader.app.utils.AbstractBasePageTest;
import org.ladocuploader.app.utils.Page;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

@Tag("staticPagesJourney")
public class StaticPagesJourneyTest extends AbstractBasePageTest {

  @Override
  protected void initTestPage() {
    testPage = new Page(driver, localServerPort);
  }

  @Test
  void staticPagesJourney() {
    // Landing screen
    assertThat(testPage.getTitle()).isEqualTo("Louisiana Digital Assister");
    assertThat(driver.getWindowHandles().size()).isEqualTo(1);
    String originalWindow = driver.getWindowHandle();
    // Back on landing screen
    assertThat(testPage.getTitle()).isEqualTo("Louisiana Digital Assister");
    assertThat(driver.getWindowHandles().size()).isEqualTo(1);
    // Go to privacy policy tab
    testPage.clickLink("Privacy Policy");
    assertThat(driver.getWindowHandles().size()).isEqualTo(2);
    switchAwayFromOriginalWindow(originalWindow);
    await().atMost(5, TimeUnit.SECONDS).until(
        () -> testPage.getTitle().equals("Privacy Policy")
    );
    assertThat(testPage.getTitle()).isEqualTo("Privacy Policy");
  }

  void switchAwayFromOriginalWindow(String originalWindow) {
    for (String windowHandle : driver.getWindowHandles()) {
      if(!originalWindow.contentEquals(windowHandle)) {
        driver.switchTo().window(windowHandle);
        break;
      }
    }
  }
}
