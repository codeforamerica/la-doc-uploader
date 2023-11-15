package org.ladocuploader.app.journeys;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.utils.AbstractBasePageTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;

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

    // assert that the SESSION cookie hasn't changed.
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

  @Test
  void lostSessionOnPOSTRequest() {
    String languageReadChoice = "Spanish";

    // How this works page - GET PAGE
    testPage.navigateToFlowScreen("laDigitalAssister/howThisWorks");
    testPage.clickContinue();

    // GET PAGE
    assertThat(testPage.getTitle()).isEqualTo("Timeout notice");
    String continueHref = testPage.findElementById("continue-link").getAttribute("href");
    assertThat(continueHref.contains("applicantId")).isTrue();

    String origApplicantId = getApplicantIdFromUrl(continueHref);
    Cookie firstCookie = driver.manage().getCookieNamed("SESSION");
    testPage.clickContinue();

    // POST PAGE
    assertThat(testPage.getTitle()).isEqualTo("Language preference");
    testPage.selectFromDropdown("languageRead", languageReadChoice);
    // get rid of cookies and try to keep going
    driver.manage().deleteAllCookies();
    testPage.clickContinue();

    // POST PAGE
    assertThat(testPage.getTitle()).isEqualTo("Choose programs");
    // assert that the session has the same id as well.
    Cookie secondCookie = driver.manage().getCookieNamed("SESSION");
    assertThat(firstCookie.getValue()).isNotEqualTo(secondCookie.getValue());

    // now check the applicantId's in the url
    String postHref = driver.findElement(By.tagName("form")).getAttribute("action");
    assertThat(postHref.contains("applicantId")).isTrue();
    String nextApplicantUrl = getApplicantIdFromUrl(postHref);
    assertThat(nextApplicantUrl.equals(origApplicantId)).isTrue();

    // go back and make sure data is still correct for previous page
    testPage.goBack();
    assertThat(testPage.getTitle()).isEqualTo("Language preference");

    String goBackHref = driver.findElement(By.tagName("form")).getAttribute("action");
    String goBackApplicantId = getApplicantIdFromUrl(goBackHref);

    assertThat(goBackHref.contains("applicantId")).isTrue();
    assertThat(goBackApplicantId.equals(origApplicantId)).isTrue();

    // ensure that data was accurately retrieved, even though the session was lost
    String languageReadSubmitted = driver.findElement(By.id("languageRead"))
        .findElements(By.tagName("option")).stream()
        .filter(WebElement::isSelected)
        .findFirst()
        .map(WebElement::getText)
        .orElseThrow();
    assertThat(languageReadChoice).isEqualTo(languageReadSubmitted);
  }

  @Test
  void jumpInFlowSkippingFirstPostPageShouldRedirectToFirstPage() {
    testPage.navigateToFlowScreen("laDigitalAssister/whosApplying");
    testPage.selectRadio("whosApplying", "CSRAssistant");
    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("How this works");
  }

  @Test
  void sessionSubmissionIdAndUrlIdDoNotMatchOnGET() {

  }

  @Test
  void sessionSubmissionIdAndUrlIdDoNotMatchOnPOST() {}


  private String getApplicantIdFromUrl(String url) {
    assertThat(url.contains("applicantId")).isTrue();
    int index = url.lastIndexOf("=");
    return url.substring(index);
  }

}
