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
                            // TODO: the output flash map could use the same key for all of them - would simplify the template and logic a bit. But would make it harder to isolate conditions in testing.
                            if (inputData.containsKey("addDocuments")){
                                // user tried to go back via browser after indicating they didn't want to add documents
                                if (inputData.get("addDocuments").equals("false")){
                                    outputFlashMap.put("addDocumentsSkipped", messageSource.getMessage("general.locked-submission", null, locale));
                                    RequestContextUtils.saveOutputFlashMap(redirectUrl, request, response);
                                    response.sendRedirect(redirectUrl);
                                } else {
                                    // user indicated to add documents and the time has expired (2 hours post submission)
                                    if(!SubmissionUtilities.isDocUploadActive(submission)){
                                        outputFlashMap.put("docUploadExpired", messageSource.getMessage("general.locked-submission", null, locale));
                                        RequestContextUtils.saveOutputFlashMap(redirectUrl, request, response);
                                        response.sendRedirect(redirectUrl);
                                    }

                                    // the user already added documents and submitted them (cannot go back)
                                    else if (inputData.containsKey("docUploadFinalized")){
                                        outputFlashMap.put("docUploadFinalized", messageSource.getMessage("general.locked-submission", null, locale));
                                        RequestContextUtils.saveOutputFlashMap(redirectUrl, request, response);
                                        response.sendRedirect(redirectUrl);
                                    }
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
            }
            return true;
        } catch (IllegalStateException e){
            return true;
        }
    }
}
