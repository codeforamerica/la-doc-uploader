package org.ladocuploader.app.interceptors;

import formflow.library.FormFlowController;
import formflow.library.config.FlowConfiguration;
import formflow.library.config.LandmarkConfiguration;
import formflow.library.data.Submission;
import formflow.library.data.SubmissionRepositoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class DataRequiredInterceptor implements HandlerInterceptor {
  public static final String PATH_FORMAT = "/flow/{flow}/{screen}";
  private final SubmissionRepositoryService submissionRepositoryService;
  private final List<FlowConfiguration> flowConfigurations;
  private LandmarkConfiguration landmarkConfiguration;

  private static final Map<String, String> REQUIRED_DATA = Map.ofEntries(
      Map.entry("laDocUpload", "firstName"),
      Map.entry("laDigitalAssister", "languageRead")
  );

  public DataRequiredInterceptor(SubmissionRepositoryService submissionRepositoryService,
      List<FlowConfiguration> flowConfigurations) {
    this.submissionRepositoryService = submissionRepositoryService;
    this.flowConfigurations = flowConfigurations;
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

      FlowConfiguration flowConfiguration = flowConfigurations.stream()
          .filter(fc -> fc.getName().equals(parsedUrl.get("flow")))
          .findFirst()
          .orElse(null);

      if (flowConfiguration == null) {
        log.error("Unexpected state - there are no configured flows!!!");
        return true;
      }

      landmarkConfiguration = flowConfiguration.getLandmarks();

      if (landmarkConfiguration.getFirstScreen().equals(screenName) || "GET".equals(request.getMethod())) {
        // We don't have values to lose on beginning pages and we're not handling GETs right now
        return true;
      }

      // First, is there a session?
      var session = request.getSession(false);
      if (session == null) {
        return handleMissingSession(request, response);
      }

      // Second, is there a submission for this session?
      Map<String, UUID> submissionMap = (Map) session.getAttribute(FormFlowController.SUBMISSION_MAP_NAME);
      String flow = parsedUrl.get("flow");
      UUID submissionId = null;
      if (submissionMap != null && submissionMap.get(flow) != null) {
        submissionId = submissionMap.get(flow);
      }

      if (submissionId != null) {
        var submissionMaybe = this.submissionRepositoryService.findById(submissionId);
        if (submissionMaybe.isPresent()) {
          // we require that some critical data be set, or it shows that the user skipped around
          return checkForMissingData(request, response, submissionId, submissionMaybe.get());
        } else {
          return handleMissingSubmission(request, response, submissionId);
        }
      } else {
        return handleMissingSubmissionId(request, response);
      }
    } catch (IllegalStateException e) {
      return true;
    }
  }

  /**
   * The session and submission ID are present.
   * Check if there's any data missing and then use the applicantId to determine next steps.
   */
  private boolean checkForMissingData(HttpServletRequest request, HttpServletResponse response, UUID submissionId, Submission submission) throws IOException {
    var applicantIdFromRequest = request.getParameter("applicantId");
    var applicantIdFromData = (String) submission.getInputData().getOrDefault("applicantId", "");
    var parsedUrl = new AntPathMatcher().extractUriTemplateVariables(PATH_FORMAT, request.getRequestURI());
    var flowName = parsedUrl.get("flow");
    if (applicantIdFromRequest != null && !applicantIdFromData.isBlank() && !applicantIdFromRequest.equals(applicantIdFromData)) {
      log.warn("Unexpected state - applicantIds do not match, param=%s inputData=%s".formatted(applicantIdFromRequest, applicantIdFromData));
      Map<String, UUID> sessionMap = new HashMap<>();
      sessionMap.put(parsedUrl.get("flow"), UUID.fromString(applicantIdFromData));
      request.getSession().setAttribute(FormFlowController.SUBMISSION_MAP_NAME, sessionMap);
      return true;
    }
    // ensure that a specific field is set in a flow to show that the user has entered
    // the flow correctly.
    var inputName = REQUIRED_DATA.get(flowName);
    if (submission.getInputData().get(inputName) == null) {
      log.error("Submission %s missing field data %s, redirecting".formatted(submissionId, inputName));
      response.sendRedirect(getRedirectUrl(flowName, parsedUrl.get("screen"));
      return false;
    }

    return manageSessionOrRedirect(request, response);
  }

  /**
   * The session is present, but it is missing an "id" attribute (submissionId).
   * Use the applicantId to determine next steps.
   */
  private boolean handleMissingSubmissionId(HttpServletRequest request, HttpServletResponse response) throws IOException {
    log.error("No submission ID in session");
    return manageSessionOrRedirect(request, response);

  }

  /**
   * The session and submission ID are present, but the submission associated with the submissionID is not present in the database.
   * Use the applicantId to determine next steps.
   */
  private boolean handleMissingSubmission(HttpServletRequest request, HttpServletResponse response, UUID submissionId) throws IOException {
    log.error("Submission %s not found in database".formatted(submissionId));
    return manageSessionOrRedirect(request, response);
  }

  /**
   * The session is not present - we're not able to find any existing information for this applicant.
   * Use the applicantId to determine next steps.
   */
  private boolean handleMissingSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
    log.warn("Unexpected state - No session present");
    return manageSessionOrRedirect(request, response);
  }

  /**
   * Check if the applicantId is available - this means that there was another session previously associated with this applicant's data.
   * If it is not available - we don't have any other information on this applicant and should assume it's a new application
   */
  private boolean manageSessionOrRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
    var applicantId = request.getParameter("applicantId");
    var parsedUrl = new AntPathMatcher().extractUriTemplateVariables(PATH_FORMAT, request.getRequestURI());
    if (applicantId == null) {
      // Middle of the flow without session or applicantId data - have to redirect
      log.warn("Redirecting");
      response.sendRedirect(getRedirectUrl(parsedUrl.get("flow"), parsedUrl.get("screen")));
      return false;
    } else {
      // Middle of the flow without session - but we have the applicantId to recover
      // What are the security concerns here? How can we mitigate?
      log.warn("Setting session id to %s".formatted(applicantId));
      Map<String, UUID> sessionMap = new HashMap<>();
      sessionMap.put(parsedUrl.get("flow"), UUID.fromString(applicantId));
      request.getSession().setAttribute(FormFlowController.SUBMISSION_MAP_NAME, sessionMap);
      return true;
    }
  }

  private String getRedirectUrl(String flow, String interceptedScreenName) {
    return String.format("/flow/%s/%s?intercepted=%s", flow, landmarkConfiguration.getFirstScreen(), interceptedScreenName);
  }
}
