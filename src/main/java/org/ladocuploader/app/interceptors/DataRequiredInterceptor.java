package org.ladocuploader.app.interceptors;

import formflow.library.FormFlowController;
import formflow.library.data.SubmissionRepositoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.ladocuploader.app.data.RecoverySubmissionIDs;
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
  private final RecoverySubmissionIDs recoveryRecoverySubmissionIDs;

  // Checks at signposts
  private static final Map<String, String> REQUIRED_DATA = Map.of(
      "signPost", "parish",
      "householdSignPost", "parish",
      "incomeSignPost", "parish",
      "expensesSignPost", "parish",
      "finalSignPost", "parish"
  );

  public DataRequiredInterceptor(SubmissionRepositoryService submissionRepositoryService, RecoverySubmissionIDs recoveryRecoverySubmissionIDs) {
    this.submissionRepositoryService = submissionRepositoryService;
    this.recoveryRecoverySubmissionIDs = recoveryRecoverySubmissionIDs;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    try {
      var session = request.getSession(false);
      var recoverySubmissionId = setRecoverySubmissionId(session);

      var parsedUrl = new AntPathMatcher().extractUriTemplateVariables(PATH_FORMAT, request.getRequestURI());
      var requiredData = REQUIRED_DATA.get(parsedUrl.get("screen"));
      var flow = "laDigitalAssister";
      String redirect_url = String.format("/flow/%s/parish?intercepted=%s", flow, parsedUrl.get("screen"));

      if (requiredData == null) {
        return true; // There are no data requirements for this page
      }

      if (session == null) {
        return handleSessionMissing(request, response, flow, redirect_url, recoverySubmissionId);
      }

      UUID submissionId = getSubmissionIdFromSession(session, flow);
      if (submissionId != null && !submissionId.equals(recoverySubmissionId)) {
        // Reconcile
        if (recoverySubmissionId == null) {
          log.info("Setting recoverySubmissionId to " + submissionId);
          recoveryRecoverySubmissionIDs.setFlowSubmissionId(flow, submissionId);
        } else {
          log.warn("submissionId from session and recovery submissionId do not match " + submissionId + " " + recoverySubmissionId);
        }
      }

      if (submissionId == null) {
        log.info("Attempting to recover submissionID with " + recoverySubmissionId);
        if (recoverySubmissionId != null) {
          submissionId = recoverySubmissionId;
          setSubmissionInSession(session, recoverySubmissionId, flow);
        }
      }

      if (submissionId != null) {
        var submissionMaybe = this.submissionRepositoryService.findById(submissionId);
        if (submissionMaybe.isPresent()) {
          var submission = submissionMaybe.get();
          if (submission.getInputData().getOrDefault(requiredData, "").toString().isBlank()) {
            log.error("Submission %s missing field data %s, redirecting to parish page".formatted(submissionId, requiredData));
            response.sendRedirect(redirect_url);
            return false;
          }
        } else {
          return handleMissingSubmissionObject(response, requiredData, redirect_url, submissionId);
        }
      } else {
        return handleUnrecoverableSubmissionId(response, requiredData, redirect_url);
      }

      return true;
    } catch (IllegalStateException e) {
      log.error("Unexpected error", e);
      return true;
    }
  }

  @Nullable
  private static UUID getSubmissionIdFromSession(HttpSession session, String flow) {
    var submissionMap = (Map<String, UUID>) session.getAttribute(FormFlowController.SUBMISSION_MAP_NAME);
    if (submissionMap != null && submissionMap.containsKey(flow)) {
      return submissionMap.get(flow);
    }
    return null;
  }

  /**
   * Initialize recovery submission ID.
   */
  private UUID setRecoverySubmissionId(HttpSession session) {
    String flow = "laDigitalAssister";
    var recoverySubmissionId = recoveryRecoverySubmissionIDs.getFlowSubmissionId(flow);
    if (recoverySubmissionId != null || session == null) {
      return recoverySubmissionId;
    }

    UUID submissionId = getSubmissionIdFromSession(session, flow);
    if (submissionId != null) {
      log.info("Setting recoverySubmissionId to " + submissionId);
      recoveryRecoverySubmissionIDs.setFlowSubmissionId(flow, submissionId);
      return submissionId;
    }

    return recoverySubmissionId;
  }

  private static boolean handleUnrecoverableSubmissionId(HttpServletResponse response, String requiredData, String redirect_url) throws IOException {
    log.error("No submission ID in session (required field %s), redirecting to parish page".formatted(requiredData));
    response.sendRedirect(redirect_url);
    return false;
  }

  private static boolean handleMissingSubmissionObject(HttpServletResponse response, String requiredData, String redirect_url, UUID submissionId) throws IOException {
    log.error("Submission %s not found in database (required field %s), redirecting to parish page".formatted(submissionId, requiredData));
    response.sendRedirect(redirect_url);
    return false;
  }

  private boolean handleSessionMissing(HttpServletRequest request, HttpServletResponse response, String flow, String redirect_url, UUID recoverySubmissionId) throws IOException {
    log.error("Unexpected state: session is null on request.");
    if (recoverySubmissionId != null) {
      log.error("Recovering session with submissionID: " + recoverySubmissionId);
      HttpSession session = request.getSession();
      setSubmissionInSession(session, recoverySubmissionId, flow);
      return true;
    }
    log.info("No session or recovery ID present, redirecting to parish page");
    response.sendRedirect(redirect_url);
    return false;
  }

  private void setSubmissionInSession(HttpSession session, UUID id, String flow) {
    Map<String, UUID> submissionMap = (Map<String, UUID>) session.getAttribute("submissionMap");
    if (submissionMap == null) {
      submissionMap = new HashMap<>();
    }
    submissionMap.put(flow, id);
    session.setAttribute("submissionMap", submissionMap);
  }
}
