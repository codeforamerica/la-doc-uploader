package org.ladocuploader.app.testutils;
import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class AccessibilityTestPage extends Page {
    public List<Rule> resultsList = new ArrayList<>();


    public AccessibilityTestPage(RemoteWebDriver driver, String localServerPort, MessageSource messageSource) {
        super(driver, localServerPort, messageSource);
    }

    @Override
    public void clickLink(String linkText) {
        super.clickLink(linkText);
        testAccessibility();
    }

    @Override
    public void clickButton(String buttonText) {
        super.clickButton(buttonText);
        testAccessibility();
    }

    @Override
    public void clickButtonLink(String buttonLinkText) {
        super.clickButtonLink(buttonLinkText);
        testAccessibility();
    }

    @Override
    public void clickContinue() {
        super.clickContinue();
        testAccessibility();
    }

    public void testAccessibility() {
        AxeBuilder builder = new AxeBuilder();
        builder.setOptions("""
                {   "resultTypes": ["violations"],
                    "runOnly": { 
                        "type": "tag", 
                        "values": ["wcag2a", "wcag2aa", "wcag21a", "wcag21aa", "section508"]
                    } 
                }
                """);
        Results results = builder.analyze(driver);
        List<Rule> violations = results.getViolations();
        violations.forEach(rule -> rule.setUrl(getTitle()));
        resultsList.addAll(violations);
        log.info("Testing a11y on page %s, found %s violations".formatted(getTitle(), violations.size()));
    }
}