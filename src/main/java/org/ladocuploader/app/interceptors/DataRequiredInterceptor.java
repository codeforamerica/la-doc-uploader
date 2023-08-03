package org.ladocuploader.app.interceptors;

import formflow.library.data.SubmissionRepositoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class DataRequiredInterceptor implements HandlerInterceptor {
    public static final String PATH_FORMAT = "/flow/{flow}/{screen}";
    private final SubmissionRepositoryService submissionRepositoryService;
    private static final Map<String, String> REQUIRED_DATA = Map.ofEntries(
            // Step 1
            Map.entry("preScreenApplyingForSelf", "hasMoreThanOneStudent"),
            Map.entry("preScreenEnrolledInVirtualSchool", "hasMoreThanOneStudent"),
            Map.entry("preScreenUnenrolled", "hasMoreThanOneStudent"),
            Map.entry("preScreenUnenrolledSchoolName", "hasMoreThanOneStudent"),
            Map.entry("preScreenIneligible", "hasMoreThanOneStudent"),
            Map.entry("eligibilityLikely", "hasMoreThanOneStudent"),

            // Step 2
            Map.entry("studentsSignpost", "hasMoreThanOneStudent"),
            Map.entry("studentsAdd", "hasMoreThanOneStudent"),
            Map.entry("studentsDesignations", "hasMoreThanOneStudent"),
            Map.entry("studentsSchoolYear", "hasMoreThanOneStudent"),
            Map.entry("studentsUnenrolledSchoolName", "hasMoreThanOneStudent"),
            Map.entry("studentsWouldAttendSchoolName", "hasMoreThanOneStudent"),
            Map.entry("studentsSummary", "hasMoreThanOneStudent"),
            Map.entry("studentsDeleteConfirmation", "hasMoreThanOneStudent"),
            Map.entry("applicantIsInHousehold", "hasMoreThanOneStudent"),
            Map.entry("eligibilityStudents", "hasMoreThanOneStudent"),

            // Step 3
            Map.entry("gettingToKnowYou", "hasMoreThanOneStudent"),
            Map.entry("personalInfo", "hasMoreThanOneStudent"),
            Map.entry("homeAddress", "firstName"),
            Map.entry("pickHomeAddress", "firstName"),
            Map.entry("verifyHomeAddress", "firstName"),
            Map.entry("contactInfo", "firstName"),
            Map.entry("confirmBlankEmail", "firstName"),
            Map.entry("reviewPersonalInfo", "firstName"),

            // Step 4
            Map.entry("housemates", "firstName"),
            Map.entry("signpostHouseholdDetails", "firstName"),
            Map.entry("housemateInfo", "firstName"),
            Map.entry("householdList", "firstName"),
            Map.entry("householdReceivesBenefits", "firstName"),
            Map.entry("householdDeleteConfirmation", "firstName"),

            // Step 5
            Map.entry("signpostIncome", "firstName"),
            Map.entry("hasIncome", "firstName"),
            Map.entry("incomeAddJob", "firstName"),
            Map.entry("incomeChooseHouseholdMember", "firstName"),
            Map.entry("incomeJobName", "firstName"),
            Map.entry("incomeSelfEmployed", "firstName"),
            Map.entry("incomeIsJobHourly", "firstName"),
            Map.entry("incomeHourlyWageCalculator", "firstName"),
            Map.entry("incomeGrossMonthlyIndividual", "firstName"),
            Map.entry("incomeRegularPayCalculator", "firstName"),
            Map.entry("incomeWillBeLessYearly", "firstName"),
            Map.entry("incomeWillBeLessMonthly", "firstName"),
            Map.entry("incomeSelfEmployedGrossMonthly", "firstName"),
            Map.entry("incomeSelfEmployedOperatingExpenses", "firstName"),
            Map.entry("incomeSelfEmployedWillBeLess", "firstName"),
            Map.entry("incomeReview", "firstName"),
            Map.entry("incomeEarnedIncomeComplete", "firstName"),
            Map.entry("incomeUnearnedRetirementTypes", "firstName"),
            Map.entry("incomeUnearnedTypes", "firstName"),
            Map.entry("incomeUnearnedAmounts", "firstName"),
            Map.entry("incomeComplete", "firstName"),
            Map.entry("incomeDeleteConfirmation", "firstName"),

            // Step 6
            Map.entry("addingDocuments", "firstName"),
            Map.entry("docUploadHowTo", "firstName"),
            Map.entry("uploadIdentityDocuments", "firstName"),
            Map.entry("uploadEnrollmentDocuments", "firstName"),
            Map.entry("uploadIncomeDocuments", "firstName"),
            Map.entry("uploadUnearnedIncomeDocuments", "firstName"),
            Map.entry("docPendingConfirmation", "firstName"),
            Map.entry("docSubmitConfirmation", "firstName"),

            // Step 7
            Map.entry("submitting", "firstName"),
            Map.entry("legalStuff", "firstName"),
            Map.entry("signName", "firstName"),
            Map.entry("success", "firstName")
    );

    public DataRequiredInterceptor(SubmissionRepositoryService submissionRepositoryService) {
        this.submissionRepositoryService = submissionRepositoryService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            var parsedUrl = new AntPathMatcher().extractUriTemplateVariables(PATH_FORMAT, request.getRequestURI());
            if (!parsedUrl.get("flow").equals("pebt")) {
                return true; // Only enforce data requirements in PEBT flow.
            }
            var requiredData = REQUIRED_DATA.get(parsedUrl.get("screen"));
            if (requiredData == null) {
                return true; // There are no data requirements for this page
            }

            var session = request.getSession(false);
            if (session == null) {
                log.info("No session present (missing field data %s), redirecting to homepage".formatted(requiredData));
                response.sendRedirect("/");
                return false;
            }

            var submissionId = (UUID) session.getAttribute("id");
            if (submissionId != null) {
                var submissionMaybe = this.submissionRepositoryService.findById(submissionId);
                if (submissionMaybe.isPresent()) {
                    var submission = submissionMaybe.get();
                    if (submission.getInputData().getOrDefault(requiredData, "").toString().isBlank()) {
                        log.error("Submission %s missing field data %s, redirecting to homepage".formatted(submissionId, requiredData));
                        response.sendRedirect("/");
                        return false;
                    }
                } else {
                    log.error("Submission %s not found in database (required field %s), redirecting to homepage".formatted(submissionId, requiredData));
                    response.sendRedirect("/");
                    return false;
                }
            } else {
                log.error("No submission ID in session (required field %s), redirecting to homepage".formatted(requiredData));
                response.sendRedirect("/");
                return false;
            }

            return true;
        } catch (IllegalStateException e) {
            return true;
        }
    }
}
