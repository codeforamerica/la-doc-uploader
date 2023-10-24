package org.ladocuploader.app.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import formflow.library.utils.UserFileMap;
import org.springframework.util.unit.DataSize;

public class UserFileUtilities {


    /**
     * Gets the files for a particular flow and input from a UserFileMap.
     *
     * @param userFileMapString the UserFileMap to extract files from.
     * @param flow the flow the files of interest are in
     * @param inputName name of input the files are associated with
     * @return
     * @throws JsonProcessingException
     */
    public static Map<UUID, Map<String, String>> getUserFileMap(String userFileMapString, String flow, String inputName) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserFileMap userFileMap = objectMapper.readValue(userFileMapString, UserFileMap.class);
        return userFileMap.getFiles(flow, inputName);
    }

    /**
     * Returns a string value of the `bytes` converted to MB and rounded to one decimal place.
     *
     * @param bytes number of bytes to convert and round
     * @return String of bytes in MB, rounded to one decimal place
     */
    public static String getFileMbFromBytes(String bytes){
        DataSize megaBytes = DataSize.ofMegabytes(1L);
        BigDecimal bd = BigDecimal.valueOf(Double.parseDouble(bytes) / megaBytes.toBytes())
            .setScale(1, RoundingMode.CEILING);
        return bd.toString();
    }
}
