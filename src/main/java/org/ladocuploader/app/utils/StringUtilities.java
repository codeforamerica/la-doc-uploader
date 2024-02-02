package org.ladocuploader.app.utils;

import formflow.library.data.Submission;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StringUtilities {
  public static String joinWithCommaAndSpace(List<String> strings) {
    return strings.stream()
        .map(s -> s + ", ")
        .collect(Collectors.joining())
        .replaceAll(", $", "");
  }
}