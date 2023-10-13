package org.ladocuploader.app.interceptors;

import formflow.library.FormFlowController;
import formflow.library.data.Submission;
import formflow.library.data.SubmissionRepositoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class DataRequiredInterceptor implements HandlerInterceptor {
  public static final String PATH_FORMAT = "/flow/{flow}/{screen}";
  private final SubmissionRepositoryService submissionRepositoryService;
  private static final Map<String, String> REQUIRED_DATA = Map.ofEntries(
      Map.entry("laDocUpload", "firstName"),
      Map.entry("laDigitalAssister", "languageRead")
  );

  private static final Map<String, String> BEGINNING_PAGES = Map.ofEntries(
      Map.entry("laDocUpload", "clientInfo"),
      Map.entry("laDigitalAssister", "languagePreference")
  );

  public DataRequiredInterceptor(SubmissionRepositoryService submissionRepositoryService) {
    this.submissionRepositoryService = submissionRepositoryService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    // submissionId --> parameter from session
    // applicationId --> parameter from inputData
    // Option 1: See if we're missing initial data (ex program selection) and check if submissionId is different than applicationId
    // -> then do mitigation
    // Option 2 (ideal): Always check if submissionId is different then applicationId
    // -> then do mitigation


    // Mitigation - the submissionId is different than applicationId
    // Option 1 (ideal): Overwrite session id with previous session id (from applicationId)
    // Option 2: Copy data associated with applicationId into new submission data - update db and update applicationId to match new session id
    // --> Might have stray records which might affect analytics, maybe delete these?


        try {
            var parsedUrl = new AntPathMatcher().extractUriTemplateVariables(PATH_FORMAT, request.getRequestURI());
            var requiredData = REQUIRED_DATA.get(parsedUrl.get("screen"));
            String redirect_url = String.format("/flow/laDocUpload/clientInfo?intercepted=%s", parsedUrl.get("screen"));

            if (requiredData == null) {
                return true; // There are no data requirements for this page
            }

      var parsedUrl = new AntPathMatcher().extractUriTemplateVariables(PATH_FORMAT, request.getRequestURI());
      var screenName = parsedUrl.get("screen");
      if (BEGINNING_PAGES.containsValue(screenName) || "GET".equals(request.getMethod())) {
        // We dont have values to lose on beginning pages and we're not handling GETs right now
        return true;
      }

      String flow = parsedUrl.get("flow");
      var requiredData = REQUIRED_DATA.get(flow);


      var session = request.getSession(false);
      if (session == null) {
        return handleMissingSession(request, response);
      }

      Map<String, UUID> submissionMap = (Map) session.getAttribute(FormFlowController.SUBMISSION_MAP_NAME);
      UUID submissionId = null;

      if (submissionMap != null && submissionMap.get(flow) != null) {
        submissionId = submissionMap.get(flow);
      }
      if (submissionId != null) {
        var submissionMaybe = this.submissionRepositoryService.findById(submissionId);
        if (submissionMaybe.isPresent()) {
          return checkForMissingData(request, response, requiredData, submissionId, submissionMaybe.get());
        } else {
          return handleMissingSubmission(request, response, requiredData, submissionId);
        }
      } else {
        return handleMissingSubmissionId(request, response);
      }
    } catch (IllegalStateException e) {
      return true;
    }
  }

  private boolean checkIfSubmissionIdAndApplicantIdMatch(HttpServletRequest request) {
    var session = request.getSession(false);
    var applicantId = request.getParameter("applicantId");
    boolean applicantIdPresent = applicantId != null && !applicantId.isEmpty();
    if (session == null) {
      return !applicantIdPresent;
    } else {
      var submissionId = session.getAttribute("id");
      if (submissionId == null) {
        return !applicantIdPresent;
      } else {
        return submissionId.equals(applicantId);
      }
    }
  }


  /**
   * The session and submission ID are present.
   * Check if there's any data missing and then use the applicantId to determine next steps.
   */
  private static boolean checkForMissingData(HttpServletRequest request, HttpServletResponse response, String requiredData, UUID submissionId, Submission submission) throws IOException {

    var applicantIdFromRequest = request.getParameter("applicantId");
    var applicantIdFromData = submission.getInputData().getOrDefault("applicantId", "");
    if (applicantIdFromRequest != null && !applicantIdFromRequest.equals(applicantIdFromData)) {
      log.warn("applicantIds do not match, param=%s inputData=%s".formatted(applicantIdFromRequest, applicantIdFromData));
    }

    var parsedUrl = new AntPathMatcher().extractUriTemplateVariables(PATH_FORMAT, request.getRequestURI());
    var flowName = parsedUrl.get("flow");
    var inputName = REQUIRED_DATA.get(flowName);
    if (submission.getInputData().getOrDefault(inputName, "").toString().isBlank()) {
      log.error("Submission %s missing field data %s, redirecting".formatted(submissionId, requiredData));
    }

    return manageSessionOrRedirect(request, response);
  }

  /**
   * The session is present, but it is missing an "id" attribute (submissionId).
   * Use the applicantId to determine next steps.
   */
  private static boolean handleMissingSubmissionId(HttpServletRequest request, HttpServletResponse response) throws IOException {
    log.error("No submission ID in session");
    return manageSessionOrRedirect(request, response);

  }

  /**
   * The session and submission ID are present, but the submission associated with the submissionID is not present in the database.
   * Use the applicantId to determine next steps.
   */
  private static boolean handleMissingSubmission(HttpServletRequest request, HttpServletResponse response, String requiredData, UUID submissionId) throws IOException {
    log.error("Submission %s not found in database".formatted(submissionId));
    return manageSessionOrRedirect(request, response);
  }

  /**
   * The session is not present - we're not able to find any existing information for this applicant.
   * Use the applicantId to determine next steps.
   */
  private static boolean handleMissingSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
    log.warn("Unexpected state - No session present");
    return manageSessionOrRedirect(request, response);
  }

  /**
   * Check if the applicantId is available - this means that there was another session previously associated with this applicant's data.
   * If it is not available - we don't have any other information on this applicant and should assume it's a new application
   */
  private static boolean manageSessionOrRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
    var applicantId = request.getParameter("applicantId");
    if (applicantId == null) {
      // Middle of the flow without session or applicantId data - have to redirect
      var parsedUrl = new AntPathMatcher().extractUriTemplateVariables(PATH_FORMAT, request.getRequestURI());
      var screenName = parsedUrl.get("screen");
      String flow = parsedUrl.get("flow");
      String redirect_url = String.format("/flow/%s/%s?intercepted=%s", flow, BEGINNING_PAGES.get(flow), screenName);
      log.warn("Redirecting");
      response.sendRedirect(redirect_url);
      return false;
    } else {
      // Middle of the flow without session - but we have the applicantId to recover
      // What are the security concerns here? How can we mitigate?
      log.warn("Setting session id to %s".formatted(applicantId));
      var session = request.getSession();
      session.setAttribute("id", UUID.fromString(applicantId));
      return true;
    }
  }
}
