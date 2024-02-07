package org.ladocuploader.app.cli;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@Profile({"production", "staging"})
public class SftpClientImpl implements SftpClient {

    String username;
    String password;
    String uploadUrl;

    String environmentPath;

    public SftpClientImpl(@Value("${sftp.username:}") String username, @Value("${sftp.password:}") String password, @Value("${sftp.upload-url:}") String uploadUrl,
    @Value("${sftp.environment-path:}") String environmentPath) {
        this.username = username;
        this.password = password;
        this.uploadUrl = uploadUrl;
        this.environmentPath = environmentPath;
    }

    @Override
    public void uploadFile(String zipFilename, String filePath) throws JSchException, SftpException {
        JSch jsch = new JSch();
        jsch.setKnownHosts("src/main/resources/known_hosts");
        Session jschSession = jsch.getSession(username, uploadUrl);
        jschSession.setPassword(password);
        jschSession.connect(10000);

        Channel sftp = jschSession.openChannel("sftp");
        sftp.connect(5000);

        ChannelSftp channelSftp = (ChannelSftp) sftp;
        String destinationFilePath = String.join("/", List.of(filePath + "-" + this.environmentPath, zipFilename));
        log.info(destinationFilePath);

        channelSftp.put(zipFilename, destinationFilePath );

        channelSftp.exit();
    }
}

