package org.ladocuploader.app.journeys;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.utils.AbstractBasePageTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;

@Slf4j
public class DataRequiredInterceptorJourneyTest extends AbstractBasePageTest {

  @Test
  void noSessionLossThingsWorkWell() {
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
    String languageChoice = "Spanish";
    // How this works page
    testPage.navigateToFlowScreen("laDigitalAssister/howThisWorks");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Timeout notice");
    String continueHref = testPage.findElementById("continue-link").getAttribute("href");
    String applicantId = getApplicantIdFromUrl(continueHref);
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Language preference");
    testPage.selectFromDropdown("languageRead", languageChoice);
    testPage.selectRadio("needInterpreter", "Yes");
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Choose programs");
    //testPage.enter("programs", List.of("SNAP"));
    List<WebElement> formInputElements = driver.findElements(By.name("programs[]")).stream()
            .filter(e -> e.getAttribute("type").equals("checkbox")).toList();
    testPage.selectEnumeratedInput(formInputElements, List.of("SNAP"));
    testPage.clickContinue();

    assertThat(testPage.getTitle()).isEqualTo("Expedited notice");

    // on GET - delete the cookie
    Cookie firstCookie = driver.manage().getCookieNamed("SESSION");
    // get rid of cookies and try to keep going
    driver.manage().deleteAllCookies();

    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("Signpost");

    // assert that the url has the same application ID as before
    String postHref = driver.findElement(By.id("continue-link")).getAttribute("href");
    String nextApplicantUrl = getApplicantIdFromUrl(postHref);
    assertThat(nextApplicantUrl.equals(applicantId)).isTrue();

    // assert that the session has a different cookie
    Cookie secondCookie = driver.manage().getCookieNamed("SESSION");
    assertThat(firstCookie.getValue()).isNotEqualTo(secondCookie.getValue());

    // now go back and ensure that data is still good
    testPage.goBack();
    assertThat(testPage.getTitle()).isEqualTo("Expedited notice");
    testPage.goBack();
    assertThat(testPage.getTitle()).isEqualTo("Choose programs");
    List<String> programs = testPage.getCheckboxValues("programs");
    assertThat(programs.size()).isEqualTo(1);
    assertThat(programs.contains("Food (SNAP)")).isTrue();

    testPage.goBack();
    assertThat(testPage.getTitle()).isEqualTo("Language preference");

    testSelectValue("languageRead", languageChoice);
    assertThat(testPage.getRadioValue("needInterpreter")).isEqualTo("Yes");
    Cookie thirdCookie = driver.manage().getCookieNamed("SESSION");
    assertThat(thirdCookie.getValue()).isEqualTo(secondCookie.getValue());
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
    // assert that the session is different
    Cookie secondCookie = driver.manage().getCookieNamed("SESSION");
    assertThat(firstCookie.getValue()).isNotEqualTo(secondCookie.getValue());

    // now check the applicantId's in the url and that it equals the original applicant id.
    String postHref = driver.findElement(By.tagName("form")).getAttribute("action");
    String nextApplicantUrl = getApplicantIdFromUrl(postHref);
    assertThat(nextApplicantUrl.equals(origApplicantId)).isTrue();

    // go back and make sure data is still correct for previous page
    testPage.goBack();
    assertThat(testPage.getTitle()).isEqualTo("Language preference");

    String goBackHref = driver.findElement(By.tagName("form")).getAttribute("action");
    String goBackApplicantId = getApplicantIdFromUrl(goBackHref);

    assertThat(goBackApplicantId.equals(origApplicantId)).isTrue();

    // ensure that data was accurately retrieved, even though the session was lost
    testSelectValue("languageRead", languageReadChoice);
  }

  @Test
  void jumpInFlowSkippingFirstPostPageShouldRedirectToFirstPage() {
    testPage.navigateToFlowScreen("laDigitalAssister/whosApplying");
    assertThat(testPage.getTitle()).isEqualTo("Who's applying");
    testPage.selectRadio("whosApplying", "CommunityPartner");
    testPage.clickContinue();
    assertThat(testPage.getTitle()).isEqualTo("How this works");
  }

  private String getApplicantIdFromUrl(String url) {
    assertThat(url.contains("applicantId")).isTrue();
    int index = url.lastIndexOf("=");
    return url.substring(index);
  }

  private void testSelectValue(String elementId, String expectedValue) {
    String elementValue = driver.findElement(By.id(elementId))
        .findElements(By.tagName("option")).stream()
        .filter(WebElement::isSelected)
        .findFirst()
        .map(WebElement::getText)
        .orElseThrow();
    assertThat(elementValue).isEqualTo(expectedValue);
  }
}

