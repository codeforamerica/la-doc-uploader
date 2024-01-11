package org.ladocuploader.app.config;

import formflow.library.data.SubmissionRepositoryService;
import org.ladocuploader.app.interceptors.DocUploadDisabledInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

@Configuration
public class DocUploadDisabledHandlerConfiguration implements WebMvcConfigurer {
    @Autowired
    SubmissionRepositoryService submissionRepositoryService;

    @Autowired
    MessageSource messageSource;

    Locale locale;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DocUploadDisabledInterceptor(this.submissionRepositoryService, this.messageSource, locale)).addPathPatterns(DocUploadDisabledInterceptor.PATH_FORMAT);
    }
}