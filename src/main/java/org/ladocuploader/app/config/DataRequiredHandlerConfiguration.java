package org.ladocuploader.app.config;

import formflow.library.config.FlowConfiguration;
import formflow.library.data.SubmissionRepositoryService;
import java.util.List;
import org.ladocuploader.app.interceptors.DataRequiredInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DataRequiredHandlerConfiguration implements WebMvcConfigurer {
    @Autowired
    SubmissionRepositoryService submissionRepositoryService;

    @Autowired
    List<FlowConfiguration> flowConfigurations;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
            .addInterceptor(new DataRequiredInterceptor(this.submissionRepositoryService, this.flowConfigurations))
            .addPathPatterns(DataRequiredInterceptor.PATH_FORMAT);
    }
}