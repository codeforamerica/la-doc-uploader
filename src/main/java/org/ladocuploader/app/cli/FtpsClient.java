package org.ladocuploader.app.cli;

import java.io.IOException;

public interface FtpsClient {

  void uploadFile(String zipFilename, byte[] data) throws IOException;
}
