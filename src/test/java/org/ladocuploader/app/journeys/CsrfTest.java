package org.ladocuploader.app.journeys;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ladocuploader.app.config.SecurityConfiguration.CSRF_COOKIE;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.ladocuploader.app.utils.AbstractBasePageTest;
import org.openqa.selenium.WebElement;

@Slf4j
public class CsrfTest extends AbstractBasePageTest {

  @Test
  void tryRequestFromOutsidePage() {
    // first walk through a few pages and then try a random request with a bad csrf.
    String languageReadChoice = "Spanish";

    // How this works page - GET PAGE
    testPage.navigateToFlowScreen("laDigitalAssister/howThisWorks");
    testPage.clickContinue();

    // GET PAGE
    assertThat(testPage.getTitle()).isEqualTo("Timeout notice");
    String continueHref = testPage.findElementById("continue-link").getAttribute("href");

    testPage.clickContinue();

    // POST PAGE
    assertThat(testPage.getTitle()).isEqualTo("Language preference");
    testPage.selectFromDropdown("languageRead", languageReadChoice);

    String evilCsrfString = "12345.JUNKRANDOM!12345";

    Map<String, Object> bodyMap = new HashMap<>();
    bodyMap.put("csrfField",  evilCsrfString);
    bodyMap.put("firstName",  "Evil");
    bodyMap.put("lastName",  "Actor");
    bodyMap.put("emailAddress", "my.evil.email@mailinator.com");
    bodyMap.put("phoneNumber", "(123) 123-1235");

    try {
      HttpResponse<String> response = sendPostRequest(bodyMap, driver.getCurrentUrl(), evilCsrfString,
          driver.manage().getCookieNamed("SESSION").getValue());

      assertThat(response.statusCode()).isEqualTo(403);
    } catch (Exception e) {
      log.warn("Caught exception: " + e.getMessage());
    }
  }

  @Test  void tryWithLegitCsrfAndNoSession() {
    String languageReadChoice = "Spanish";

    // How this works page - GET PAGE
    testPage.navigateToFlowScreen("laDigitalAssister/howThisWorks");
    testPage.clickContinue();

    // GET PAGE
    assertThat(testPage.getTitle()).isEqualTo("Timeout notice");
    String continueHref = testPage.findElementById("continue-link").getAttribute("href");
    String origApplicantId = getApplicantIdFromUrl(continueHref);
    org.openqa.selenium.Cookie firstCookie = driver.manage().getCookieNamed("SESSION");

    testPage.clickContinue();

    // POST PAGE
    assertThat(testPage.getTitle()).isEqualTo("Language preference");
    testPage.selectFromDropdown("languageRead", languageReadChoice);

    String windowGood = driver.getWindowHandle();

    WebElement buttonToClick = testPage.findElementsByButtonText("Continue");

    String stolenCsrf = driver.manage().getCookieNamed(CSRF_COOKIE).getValue();

    Map<String, Object> bodyMap = new HashMap<>();
    bodyMap.put("csrfField",  stolenCsrf);
    bodyMap.put("firstName",  "Evil");
    bodyMap.put("lastName",  "Actor");
    bodyMap.put("emailAddress", "my.evil.email@mailinator.com");
    bodyMap.put("phoneNumber", "(123) 123-1235");

    try {
      HttpResponse<String> response = sendPostRequest(bodyMap, driver.getCurrentUrl(), stolenCsrf,
          null);

      // apparently just sending the CSRF w/o no session will cause the CSRF in the cookies
      // to get lost, maybe when a new session is made?
      assertThat(response.statusCode()).isEqualTo(403);
    } catch (Exception e) {
      log.warn("Caught exception: " + e.getMessage());
    }
  }

  private HttpResponse<String> sendPostRequest(Map<String, Object> bodyMap, String url,
      String csrfToken, String sessionId) throws IOException, InterruptedException {

    String formData = bodyMap.entrySet().stream()
        .map(entry -> { return String.format("%s=%s", entry.getKey(), entry.getValue()); })
        .collect(Collectors.joining("&"));

    CookieManager cookieManager = new CookieManager();
    cookieManager.getCookieStore().add(URI.create(url), new HttpCookie(CSRF_COOKIE, csrfToken));
    if (sessionId != null) {
      cookieManager.getCookieStore().add(URI.create(url), new HttpCookie("SESSION", sessionId));
    }
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/x-www-form-urlencoded")
        .POST(HttpRequest.BodyPublishers.ofString(formData))
        .build();
    HttpClient client = HttpClient.newBuilder()
        .followRedirects(Redirect.NORMAL)
        .cookieHandler(cookieManager)
        .connectTimeout(Duration.ofSeconds(20))
        .build();

    return client.send(request, BodyHandlers.ofString());
  }
}
