package org.ladocuploader.app.cli;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public interface SftpClient {

    void uploadFile(String zipFilename, String filePath) throws JSchException, SftpException;
}
