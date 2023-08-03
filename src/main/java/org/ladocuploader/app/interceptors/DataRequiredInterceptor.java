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
            Map.entry("howToAddDocuments", "firstName"),
            Map.entry("uploadDocuments", "firstName"),
            Map.entry("docSubmitConfirmation", "firstName"),
            Map.entry("finalConfirmation", "firstName")
    );

    public DataRequiredInterceptor(SubmissionRepositoryService submissionRepositoryService) {
        this.submissionRepositoryService = submissionRepositoryService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            var parsedUrl = new AntPathMatcher().extractUriTemplateVariables(PATH_FORMAT, request.getRequestURI());
            var requiredData = REQUIRED_DATA.get(parsedUrl.get("screen"));
            if (requiredData == null) {
                return true; // There are no data requirements for this page
            }

            var session = request.getSession(false);
            if (session == null) {
                log.info("No session present (missing field data %s), redirecting to clientInfo page".formatted(requiredData));
                // TODO: error display before redirect
                response.sendRedirect("/flow/laDocUpload/clientInfo");
                return false;
            }

            var submissionId = (UUID) session.getAttribute("id");
            if (submissionId != null) {
                var submissionMaybe = this.submissionRepositoryService.findById(submissionId);
                if (submissionMaybe.isPresent()) {
                    var submission = submissionMaybe.get();
                    if (submission.getInputData().getOrDefault(requiredData, "").toString().isBlank()) {
                        log.error("Submission %s missing field data %s, redirecting to clientInfo page".formatted(submissionId, requiredData));
                        response.sendRedirect("/flow/laDocUpload/clientInfo");
                        return false;
                    }
                } else {
                    log.error("Submission %s not found in database (required field %s), redirecting to clientInfo page".formatted(submissionId, requiredData));
                    response.sendRedirect("/flow/laDocUpload/clientInfo");
                    return false;
                }
            } else {
                log.error("No submission ID in session (required field %s), redirecting to clientInfo page".formatted(requiredData));
                response.sendRedirect("/flow/laDocUpload/clientInfo");
                return false;
            }

            return true;
        } catch (IllegalStateException e) {
            return true;
        }
    }
}
