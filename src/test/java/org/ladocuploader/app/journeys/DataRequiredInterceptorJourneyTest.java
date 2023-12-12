package org.ladocuploader.app.journeys;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.utils.AbstractBasePageTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;

@Slf4j
public class DataRequiredInterceptorJourneyTest extends AbstractBasePageTest {

  @Test
  void thingsWorkWellNoSessionLoss() {
    // How this works page
    testPage.navigateToFlowScreen("laDigitalAssister/howThisWorks");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Timeout notice");
    Cookie firstCookie = driver.manage().getCookieNamed("SESSION");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Language preference");

    // assert that the cookie hasn't changed.
    Cookie secondCookie = driver.manage().getCookieNamed("SESSION");
    assertThat(firstCookie.getValue()).isEqualTo(secondCookie.getValue());
  }

  @Test
  void lostSessionOnGETRequest() {
    // How this works page
    testPage.navigateToFlowScreen("laDigitalAssister/howThisWorks");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Timeout notice");
    String continueHref = testPage.findElementById("continue-link").getAttribute("href");
    assertThat(continueHref.contains("applicantId")).isTrue();

    String applicantId = getApplicantIdFromUrl(continueHref);
    Cookie firstCookie = driver.manage().getCookieNamed("SESSION");
   // get rid of cookies and try to keep going
    driver.manage().deleteAllCookies();
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Language preference");

    // assert that the url has the same application ID as before
    String postHref = driver.findElement(By.tagName("form")).getAttribute("action");
    assertThat(postHref.contains("applicantId")).isTrue();
    String nextApplicantUrl = getApplicantIdFromUrl(postHref);
    assertThat(nextApplicantUrl.equals(applicantId)).isTrue();

    // assert that the session has the same id as well.
    Cookie secondCookie = driver.manage().getCookieNamed("SESSION");
    assertThat(firstCookie.getValue()).isNotEqualTo(secondCookie.getValue());
  }

  private String getApplicantIdFromUrl(String url) {
    assertThat(url.contains("applicantId")).isTrue();
    int index = url.lastIndexOf("=");
    return url.substring(index);
  }

  @Test
  void jumpInFlowSkippingFirstPostPage() {}

  @Test
  void lostSessionOnPOSTRequest() {
    // How this works page
    testPage.navigateToFlowScreen("laDigitalAssister/howThisWorks");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Timeout notice");
    String continueHref = testPage.findElementById("continue-link").getAttribute("href");
    assertThat(continueHref.contains("applicantId")).isTrue();

    String origApplicantId = getApplicantIdFromUrl(continueHref);
    Cookie firstCookie = driver.manage().getCookieNamed("SESSION");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Language preference");
    // get rid of cookies and try to keep going
    driver.manage().deleteAllCookies();
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("What type of assistance would you like to apply for?");
    // assert that the session has the same id as well.
    Cookie secondCookie = driver.manage().getCookieNamed("SESSION");
    assertThat(firstCookie.getValue()).isNotEqualTo(secondCookie.getValue());

    // now check the applicantId's in the url
    String postHref = driver.findElement(By.tagName("form")).getAttribute("action");
    assertThat(postHref.contains("applicantId")).isTrue();
    String nextApplicantUrl = getApplicantIdFromUrl(postHref);
    assertThat(nextApplicantUrl.equals(origApplicantId)).isTrue();
  }

  @Test
  void sessionSubmissionIdAndUrlIdDoNotMatchOnGET() {}

  @Test
  void sessionSubmissionIdAndUrlIdDoNotMatchOnPOST() {}
}
