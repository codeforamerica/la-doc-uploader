package org.ladocuploader.app.cli;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.IOException;

public interface SftpClient {


    void uploadFile(String zipFilename, String filePath) throws JSchException, SftpException;

<<<<<<< HEAD
    void uploadFile(String zipFileName, String filePath, byte [] data) throws IOException, JSchException, SftpException;
=======
    void uploadFile(String filePath, String zipFileName, byte [] data) throws IOException, JSchException, SftpException;
>>>>>>> b663d9a1 (add override for in-memory upload file in sftp impl, encrypt for packages with encryption flag)

}
