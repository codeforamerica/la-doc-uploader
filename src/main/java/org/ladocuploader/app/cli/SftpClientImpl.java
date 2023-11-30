package org.ladocuploader.app.cli;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("production")
public class SftpClientImpl implements SftpClient {

    String username;
    String password;
    String uploadUrl;

    public SftpClientImpl(@Value("${sftp.username:}") String username, @Value("${sftp.password:}") String password, @Value("${sftp.upload-url:}") String uploadUrl) {
        this.username = username;
        this.password = password;
        this.uploadUrl = uploadUrl;
    }

    @Override
    public void uploadFile(String zipFilename) throws JSchException, SftpException {
        JSch jsch = new JSch();
        jsch.setKnownHosts("src/main/resources/known_hosts");
        Session jschSession = jsch.getSession(username, uploadUrl);
        jschSession.setPassword(password);
        jschSession.connect(10000);

        Channel sftp = jschSession.openChannel("sftp");
        sftp.connect(5000);

        ChannelSftp channelSftp = (ChannelSftp) sftp;
        channelSftp.put(zipFilename, "/inbox/" + zipFilename);

        channelSftp.exit();
    }
}

