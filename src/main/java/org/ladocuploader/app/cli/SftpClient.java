package org.ladocuploader.app.cli;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.IOException;

public interface SftpClient {


    void uploadFile(String zipFilename, String filePath) throws JSchException, SftpException;

    void uploadFile(String zipFileName, String filePath, byte [] data) throws IOException, JSchException, SftpException;

}
