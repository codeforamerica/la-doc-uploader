package org.ladocuploader.app.interceptors;

import formflow.library.FormFlowController;
import formflow.library.config.FlowConfiguration;
import formflow.library.config.LandmarkConfiguration;
import formflow.library.data.Submission;
import formflow.library.data.SubmissionRepositoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map.Entry;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.ladocuploader.app.config.UrlParams;
import org.springframework.web.servlet.ModelAndView;
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
  public static final String PATH_FORMAT_NAV = "/flow/{flow}/{screen}/navigation";
  private final SubmissionRepositoryService submissionRepositoryService;
  private final List<FlowConfiguration> flowConfigurations;
  private LandmarkConfiguration landmarkConfiguration;

  // for each flow, only supply one page and whatever fields on that page you want to check.
  // <flow, <pageName, inputName>>
  // For any POST page in a flow, it MUST have the data listed, unless it is the page that the data is collected on.
  private static final Map<String, Map<String,String>> REQUIRED_DATA = Map.of(
          "laDocUpload",  Map.of("clientInfo", "firstName"),
          "laDigitalAssister", Map.of("languagePreference", "languageRead")
  );

  public DataRequiredInterceptor(SubmissionRepositoryService submissionRepositoryService,
      List<FlowConfiguration> flowConfigurations) {
    this.submissionRepositoryService = submissionRepositoryService;
    this.flowConfigurations = flowConfigurations;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    try {

      // Note for future - We may want to abstract the applicantId away from equaling the submission ID.
      // but for now applicantId == submissionId.
      // We may also wish to add an expiration time for the id in the URL.

      String pathMatchString = PATH_FORMAT;
      boolean navigationLink = false;
      if (request.getRequestURL().toString().contains("/navigation")) {
        pathMatchString = PATH_FORMAT_NAV;
        navigationLink = true;
      }

      var parsedUrl = new AntPathMatcher().extractUriTemplateVariables(pathMatchString, request.getRequestURI());
      var screenName = parsedUrl.get("screen");
      String flowName = parsedUrl.get("flow");

      FlowConfiguration flowConfiguration = flowConfigurations.stream()
          .filter(fc -> fc.getName().equalsIgnoreCase(flowName))
          .findFirst()
          .orElse(null);

      if (flowConfiguration == null) {
        log.error("Unexpected state - there are no configured flows!!!");
        return true;
      }

      landmarkConfiguration = flowConfiguration.getLandmarks();

      // let it create one if one doesn't exist.  We will check below to see if it contains submission data.
      HttpSession session = request.getSession();

      // We don't have values to lose on beginning pages
      if (landmarkConfiguration.getFirstScreen().equals(screenName)) {
        return true;
      }

      if ("GET".equals(request.getMethod()) && !navigationLink)  {
        return true;
      }

      if ("GET".equals(request.getMethod()) && session == null) {
        log.error("GET request with no session available: {}", request.getRequestURL());
        response.sendRedirect(getRedirectUrlForFirstScreen(flowName, parsedUrl.get("screen")));
        return false;
      }

      String applicationIdFromParams = request.getParameter(UrlParams.APPLICANT_ID_URL_PARAM);
      // start with this ID equaling the one in session.
      UUID submissionId = getSubmissionIdFromSession(session, flowName);
      Submission submission = null;

      // if an applicantId has been supplied in the URL, that will override the one in session
      if(applicationIdFromParams != null) {
        if (submissionId == null || !submissionId.toString().equals(applicationIdFromParams)) {
          // if the Ids do not match, use the one from the URL.  Then continue on to see
          // if they completed basic information.
          log.warn("Unexpected state - applicantIds do not match, urlParam=%s session=%s".formatted(applicationIdFromParams,
              submissionId));
          updateSubmissionIdInSession(request.getSession(false), flowName, applicationIdFromParams);
          submissionId = UUID.fromString(applicationIdFromParams);

          log.warn(
              "Submission id's do not match and the one from the URL doesn't exist. (session submission id: '{}', url id: '{}')."
                  +
                  "Going forward with the one from the URL",
              submissionId,
              applicationIdFromParams
          );
        }
      }

      // at this point we've tried to get the submission id - either from the session or URL.
      // Fetch it and test the data in it. If it doesn't exist, then redirect to the right spot.
      if (submissionId != null) {
        var submissionMaybe = submissionRepositoryService.findById(submissionId);
        if (submissionMaybe.isPresent()) {
          // we require that some critical data be set, or it shows that the user skipped around
          if (navigationLink) {
            // it's a GET and we don't need to verify anything
            return true;
          } else {
            return checkForMissingData(request, response, submissionMaybe.get(), flowName, parsedUrl.get("screen"));
          }
        } else {
          log.error("Submission '{}' not found in database. Redirecting.", submissionId);
          response.sendRedirect(getRedirectUrlForFirstScreen(flowName, parsedUrl.get("screen")));
          return false;
        }
      } else {
        log.error("No submission ID in session or in URL. Redirecting.");
        response.sendRedirect(getRedirectUrlForFirstScreen(flowName, parsedUrl.get("screen")));
        return false;
      }
    } catch (IllegalStateException e) {
      log.warn("Caught IllegalStateException: '{}'", e.getMessage());
      return true;
    }
  }

  @Override
  public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object o,
      ModelAndView modelAndView) {

    String pathMatchString = PATH_FORMAT;
    if (request.getRequestURL().toString().contains("/navigation")) {
      pathMatchString = PATH_FORMAT_NAV;
    }
    var parsedUrl = new AntPathMatcher().extractUriTemplateVariables(pathMatchString, request.getRequestURI());

    // inject applicant id into model here, so it can give it to the templates.
    // pull the id from session, as that's the most up to date, having come from the ffb library.
    // Not sure if I should use the one from the current session or from the original URL.
    // if the server is botching things, then the one in session will be broken. But if the server
    // is fine, then it should be okay.
    UUID applicantId = getSubmissionIdFromSession(request.getSession(), parsedUrl.get("flow"));
    Map<String, Object> model = modelAndView.getModel();
    modelAndView.getModel().put("applicantId", applicantId);
  }

  /**
   * The session and submission ID are present.
   * Check if there's any data missing and then use the applicantId to determine next steps.
   */
  private boolean checkForMissingData(HttpServletRequest request,
      HttpServletResponse response,
      Submission submission,
      String flowName,
      String screen)
      throws IOException {

    Optional<Entry<String, String>> maybeRequiredFieldData = REQUIRED_DATA.get(flowName).entrySet().stream().findFirst();
    if (maybeRequiredFieldData.isPresent()) {
      Entry<String, String> requiredFieldData = maybeRequiredFieldData.get();
      // if we are not on the screen we gather that data on and the data is not set, redirect
      if (!requiredFieldData.getKey().equals(screen) && submission.getInputData().get(requiredFieldData.getValue()) == null) {
        log.error("Submission '{}' missing field data '{}',  redirecting",
            submission.getId(),
            requiredFieldData.getValue()
        );
        response.sendRedirect(getRedirectUrlForFirstScreen(flowName, screen));
        return false;
      }
    }

    return true;
  }

  /**
   * Check if the applicantId is available - this means that there was another session previously associated with this applicant's data.
   * If it is not available - we don't have any other information on this applicant and should assume it's a new application
   */
  private boolean manageSessionOrRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
   // var applicantId = request.getParameter(UrlParams.APPLICANT_ID_URL_PARAM); // applicantId
    var parsedUrl = new AntPathMatcher().extractUriTemplateVariables(PATH_FORMAT, request.getRequestURI());
    String flowName = parsedUrl.get("flow");
  //  if (applicantId == null) {
      // Middle of the flow without session or applicantId data - have to redirect
      log.warn("Redirecting: no session or applicant id available");
      response.sendRedirect(getRedirectUrlForFirstScreen(flowName, parsedUrl.get("screen")));
      return false;
   // } else {
      // Middle of the flow without session - but we have the applicantId to recover
      // What are the security concerns here? How can we mitigate?
    //  log.warn("Setting session submission id to %s".formatted(applicantId));
     // updateSubmissionIdInSession(request.getSession(false), flowName, applicantId);
     // return true;
   // }
  }

  private String getRedirectUrlForFirstScreen(String flow, String interceptedScreenName) {
    return String.format("/flow/%s/%s?intercepted=%s", flow, landmarkConfiguration.getFirstScreen(), interceptedScreenName);
  }

  /**
   * Update the Submission Id that's located in the session for a particular flow.
   * @param session the session to update the submission id in.
   * @param flow the flow the submission id is a part of
   * @param correctSubmissionId the submission id to store in the request's session
   */
  private void updateSubmissionIdInSession(HttpSession session, String flow, String correctSubmissionId) {
    Map<String, UUID> submissionMap = (Map) session.getAttribute(FormFlowController.SUBMISSION_MAP_NAME);
    if(submissionMap == null) {
      log.info("creating submission map in session");
      submissionMap = new HashMap<>();
    }
    submissionMap.put(flow, UUID.fromString(correctSubmissionId));
    session.setAttribute(FormFlowController.SUBMISSION_MAP_NAME, submissionMap);
  }

  /**
   * Retrieve the Submission Id for a particular flow from the session information
   * @param session the session to pull the data from
   * @param flowName the flow to get data from
   * @return UUID of the submission, or NULL if it doesn't exist
   */
  private UUID getSubmissionIdFromSession(HttpSession session, String flowName) {
    Map<String, UUID> submissionMap = (Map) session.getAttribute(FormFlowController.SUBMISSION_MAP_NAME);
    if (submissionMap != null && submissionMap.get(flowName) != null) {
      return submissionMap.get(flowName);
    }
    return null;
  }
}
