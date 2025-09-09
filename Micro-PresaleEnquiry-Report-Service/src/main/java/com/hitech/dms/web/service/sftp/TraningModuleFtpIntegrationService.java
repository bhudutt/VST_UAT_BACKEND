package com.hitech.dms.web.service.sftp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.model.training.module.FileItem;

@Service
public class TraningModuleFtpIntegrationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TraningModuleFtpIntegrationService.class);

	@Value("${ftp.host}")
	private String remoteHost;

	@Value("${ftp.username}")
	private String username;

	@Value("${ftp.password}")
	private String password;

	@Value("${ftp.port:21}")
	private int port;
	
	@Value("${ftp.remoteDirTraining}")
	private String remoteWorkingDir;
	
	@Value("${ftp.connection.timeout:30000}")
	private int conectionTimeout;
	
	private FTPClient connectToFtp() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(remoteHost);
        ftpClient.login(username, password);
        ftpClient.enterLocalPassiveMode();
        return ftpClient;
    }
	
	
	public List<FileItem> listFiles(String directory) throws IOException {
        FTPClient ftpClient = connectToFtp();
        List<FileItem> files = new ArrayList<>();
        try {
            ftpClient.changeWorkingDirectory(directory);
            FTPFile[] ftpFiles = ftpClient.listFiles(directory);
            for (FTPFile file : ftpFiles) {
                files.add(new FileItem(file.getName(), file.isDirectory()));
            }
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }
        return files;
    }
	
	public byte[] downloadFile(String directory, String fileName) throws IOException {
        FTPClient ftpClient = connectToFtp();
        try {
            ftpClient.changeWorkingDirectory(directory);
            byte[] fileContent = ftpClient.retrieveFileStream(fileName).readAllBytes();
            ftpClient.completePendingCommand();
            return fileContent;
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }
	
	
}
