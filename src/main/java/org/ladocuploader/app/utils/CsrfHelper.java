package org.ladocuploader.app.utils;

import java.util.UUID;

public class CsrfHelper {

  public static String generateCsrf() {
    return UUID.randomUUID().toString();
  }

}
