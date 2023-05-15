package org.ladocuploader.app.journeys;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.ladocuploader.app.utils.AbstractBasePageTest;
import org.ladocuploader.app.utils.AccessibilityTestPage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Tag("a11y")
public class AccessibilityJourneyPageTest extends AbstractBasePageTest {
    protected static List<Rule> resultsList = new ArrayList<>();
    protected static Results results;
    protected AccessibilityTestPage testPage;

    @Override
    @BeforeEach
    public void setUp() throws IOException, URISyntaxException {
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

    @Test
    void userCanCompleteDocumentUploadFlow() {
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
            violations.forEach(violation -> {
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
