package org.ladocuploader.app.config;

import formflow.library.data.SubmissionRepositoryService;
import org.ladocuploader.app.data.RecoverySubmissionIDs;
import org.ladocuploader.app.interceptors.DataRequiredInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DataRequiredHandlerConfiguration implements WebMvcConfigurer {
    final SubmissionRepositoryService submissionRepositoryService;
    final RecoverySubmissionIDs recoverySubmissionIDs;

    public DataRequiredHandlerConfiguration(SubmissionRepositoryService submissionRepositoryService, RecoverySubmissionIDs recoverySubmissionIDs) {
        this.submissionRepositoryService = submissionRepositoryService;
        this.recoverySubmissionIDs = recoverySubmissionIDs;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DataRequiredInterceptor(this.submissionRepositoryService, recoverySubmissionIDs)).addPathPatterns(DataRequiredInterceptor.PATH_FORMAT);
    }
}