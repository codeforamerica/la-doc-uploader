package org.ladocuploader.app.journeys;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.ladocuploader.app.utils.AbstractBasePageTest;
import org.openqa.selenium.By;
import pages.AccessibilityTestPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
//import static org.codeforamerica.shiba.pages.YesNoAnswer.NO;
//import static org.codeforamerica.shiba.pages.YesNoAnswer.YES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@Tag("a11y")
public class AccessibilityJourneyPageTest extends AbstractBasePageTest {
    protected static List<Rule> resultsList = new ArrayList<>();
    protected static Results results;
    protected AccessibilityTestPage testPage;

    @Override
    @BeforeEach
    public void setUp() throws IOException {
        super.setUp();
        testPage = new AccessibilityTestPage(driver);
    }

    @AfterEach
    void afterEach() {
        AxeBuilder builder = new AxeBuilder();
        results = builder.analyze(driver);
        resultsList.addAll(testPage.resultsList);
    }

    @AfterAll
    static void tearDownAll() {
        generateAccessibilityReport(results);
    }

//    @Test
//    void laterDocsFlow() {
//        when(featureFlagConfiguration.get("county-hennepin")).thenReturn(FeatureFlag.ON);
//        when(featureFlagConfiguration.get("county-morrison")).thenReturn(FeatureFlag.OFF);
//        when(featureFlagConfiguration.get("submit-via-api")).thenReturn(FeatureFlag.ON);
//
//
//        // WHEN V2 IS ENABLED IT...
//        // should give the option to enter zip instead of county
//        when(featureFlagConfiguration.get("later-docs-v2-feature")).thenReturn(FeatureFlag.ON);
//
//        testPage.clickButton("Upload documents");
//
//        testPage.clickLink("Enter my zip code instead.");
//
//        // should direct me to email the county if my zipcode is unrecognized or unsupported
//        testPage.enter("zipCode", "11111");
//        testPage.clickContinue();
//
//        // should allow me to proceed with the flow if I enter a zip code for an active county
//        testPage.clickLink("< Go Back");
//        testPage.enter("zipCode", "55444");
//        testPage.clickContinue();
//
//
//        // WHEN V2 IS DISABLED IT...
//        // should not allow me to enter my zip code
//        when(featureFlagConfiguration.get("later-docs-v2-feature")).thenReturn(FeatureFlag.OFF);
//        navigateTo("identifyCounty");
//
//        // should direct me to email docs to my county if my county is not supported
//        testPage.enter("county", "Morrison");
//        testPage.clickContinue();
//
//        // should allow me to enter personal info and continue the flow if my county is supported
//        testPage.clickLink("< Go Back");
//        testPage.enter("county", "Hennepin");
//        testPage.clickContinue();
//
//        testPage.enter("firstName", "defaultFirstName");
//        testPage.enter("lastName", "defaultLastName");
//        testPage.enter("dateOfBirth", "01/12/1928");
//        testPage.enter("ssn", "123456789");
//        testPage.enter("caseNumber", "1234567");
//        testPage.clickContinue();
//
//        // should allow me to upload documents and those documents should be sent to the ESB
//        uploadPdfFile();
//        await().until(uploadCompletes());
//        testPage.clickButton("I'm finished uploading");
//    }

    @Test
    void userCanCompleteTheNonExpeditedHouseholdFlow() {
        testPage.clickElementById("translate-button");
        testPage.clickLink("Español");
        testPage.clickElementById("translate-button");
        testPage.clickLink("English");

        testPage.clickButton("Upload documents");

        // Client Info
        testPage.enter("firstName", "Britney");
        testPage.enter("lastName", "Spears");
        testPage.enter("birthDate", "12/2/1981");
        testPage.enter("ssn", "000-00-1111");
        testPage.clickContinue();

        // Go back and ensure that SSN value shown in input is decrypted
        testPage.clickLink("< Go Back");
        testPage.clickContinue();

        // How to add docs
        testPage.clickContinue();

        // Upload documents
        assertThat(testPage.getTitle()).isEqualTo("Upload documents");
        assertThat(testPage.findElementById("form-submit-button").getAttribute("class").contains("display-none")).isTrue();
        uploadJpgFile();
        testPage.clickButton("I'm finished uploading");

        // Confirm submit
        testPage.clickButton("Yes, submit and finish");
    }

    private static void generateAccessibilityReport(Results results) {
        results.setViolations(resultsList);
        List<Rule> violations = results.getViolations();
        log.info("Found " + violations.size() + " accessibility related issues.");
        if (results.getViolations().size() > 0) {
            violations.stream().forEach(violation -> {
                log.info("Rule at issue: " + violation.getId());
                log.info("Rule description: " + violation.getDescription());
                log.info("Rule help text: " + violation.getHelp());
                log.info("Rule help page: " + violation.getHelpUrl());
                log.info("Accessibility impact: " + violation.getImpact());
                log.info("Page at issue: " + violation.getUrl());
                log.info("HTML with issue: " + violation.getNodes().get(0).getHtml());
            });
        }
        assertThat(violations.size()).isEqualTo(0);
    }
}
