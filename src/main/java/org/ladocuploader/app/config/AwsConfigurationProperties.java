package org.ladocuploader.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import static org.ladocuploader.app.config.AwsConfigurationProperties.PREFIX;

@ConfigurationProperties(PREFIX)
@Component
public class AwsConfigurationProperties {

    public static final String PREFIX = "cloud.aws.credentials";

    private String accessKey;

    private String secretKey;

    private String sessionToken;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey){
        this.accessKey = accessKey;
    }

    public String getSecretKey(){
        return secretKey;
    }

    public void setSecretKey(String secretKey){
        this.secretKey = secretKey;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken){
        this.sessionToken = sessionToken;
    }




}
