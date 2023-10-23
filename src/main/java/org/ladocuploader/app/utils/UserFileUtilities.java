package org.ladocuploader.app.utils;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import formflow.library.utils.UserFileMap;

public class UserFileUtilities {

    private final ObjectMapper objectMapper = new ObjectMapper();


    public Map<UUID, Map<String, String>> getUserFileMap(String userFileMapString, String flow, String inputName) throws JsonProcessingException {

        UserFileMap userFileMap = objectMapper.readValue(userFileMapString, UserFileMap.class);
        return userFileMap.getFiles(flow, inputName);
    }
}
