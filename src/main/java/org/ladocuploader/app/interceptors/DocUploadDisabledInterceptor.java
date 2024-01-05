package org.ladocuploader.app.interceptors;

import formflow.library.FormFlowController;
import formflow.library.data.SubmissionRepositoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ladocuploader.app.utils.SubmissionUtilities;
import org.springframework.context.MessageSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class DocUploadDisabledInterceptor implements HandlerInterceptor {

    public static final String PATH_FORMAT = "/flow/{flow}/{screen}";

    private final SubmissionRepositoryService submissionRepositoryService;

    private final MessageSource messageSource;

    private final Locale locale;

    private final String redirectUrl = "/flow/laDigitalAssister/confirmation";
    public static List<String> docUploadScreens = List.of(
            "docUploadIntro", "docUploadSignpost", "docUploadInstructions",
            "docUploadRecommendations", "docUpload", "docUploadType", "docUploadReview",
            "docUploadSubmit"
    );


    public DocUploadDisabledInterceptor(SubmissionRepositoryService submissionRepositoryService, MessageSource messageSource, Locale locale) {
        this.submissionRepositoryService = submissionRepositoryService;
        this.messageSource = messageSource;
        this.locale = locale;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        try {
            var parsedUrl = new AntPathMatcher().extractUriTemplateVariables(PATH_FORMAT, request.getRequestURI());
            FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request); // use this to populate messages
            if (!parsedUrl.get("flow").equals("laDigitalAssister")){
                return true;
            }
            if (docUploadScreens.contains(parsedUrl.get("screen"))){
                var session = request.getSession(false);
                if (session == null) {
                    return false;
                }
                Map<String, UUID> submissionMap = (Map)session.getAttribute(FormFlowController.SUBMISSION_MAP_NAME);
                UUID submissionId = null;

                if (submissionMap != null && submissionMap.get(parsedUrl.get("flow")) != null) {
                    submissionId = submissionMap.get(parsedUrl.get("flow"));
                    if (submissionId != null) {
                        var submissionMaybe = this.submissionRepositoryService.findById(submissionId);
                        if (submissionMaybe.isPresent()) {
                            var submission = submissionMaybe.get();
                            var inputData = submission.getInputData();
                            if (inputData.containsKey("addDocuments")){
                                if (inputData.get("addDocuments").equals("false")){
                                    outputFlashMap.put("addDocumentsSkipped", messageSource.getMessage("doc-upload.not-selected", null, locale)); //TODO: get message from properties
                                    RequestContextUtils.saveOutputFlashMap(redirectUrl, request, response);
                                    response.sendRedirect(redirectUrl);
                                } else {
                                    if(!SubmissionUtilities.isDocUploadActive(submission)){
                                        outputFlashMap.put("docUploadExpired", messageSource.getMessage("doc-upload.expired", null, locale));
                                        RequestContextUtils.saveOutputFlashMap(redirectUrl, request, response);
                                        response.sendRedirect(redirectUrl);
                                    }
                                    // check if already uploaded here
                                }
                            }

                        } else {
                            log.error("Submission %s not found in database".formatted(submissionId));
                            return false;
                        }
                    } else {
                        log.error("No submission ID in session");
                        return false;
                    }
                }
                // TODO: check if client already uploaded documents (How to confirm it is final?). If so, add note and don't allow going back.
            }
            return true;
        } catch (IllegalStateException e){
            return true;
        }
    }
}
