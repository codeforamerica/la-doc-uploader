package org.ladocuploader.app.journeys;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.ladocuploader.app.utils.AccessibilityTestPage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Tag("a11y")
public class AccessibilityJourneyPageTest extends LaDigitalAssisterFlowJourneyTest {
    protected static List<Rule> resultsList = new ArrayList<>();
    protected static Results results;

    @Override
    @BeforeEach
    public void setUp() throws IOException, URISyntaxException {
        super.setUp();
        testPage = new AccessibilityTestPage(driver, localServerPort, messageSource);
    }
    @AfterEach
    void afterEach() {
        AxeBuilder builder = new AxeBuilder();
        results = builder.analyze(driver);
        resultsList.addAll(((AccessibilityTestPage)testPage).resultsList);
    }

    @AfterAll
    static void tearDownAll() {
        generateAccessibilityReport(results);
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
