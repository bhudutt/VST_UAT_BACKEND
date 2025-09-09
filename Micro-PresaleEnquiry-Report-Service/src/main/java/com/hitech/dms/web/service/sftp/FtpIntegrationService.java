package com.hitech.dms.web.service.sftp;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.FileInputStream;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

@Service
public class FtpIntegrationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FtpIntegrationService.class);

	@Value("${ftp.host}")
	private String remoteHost;

	@Value("${ftp.username}")
	private String username;

	@Value("${ftp.password}")
	private String password;

	@Value("${ftp.port:21}")
	private int port;

	@Value("${ftp.remoteDir}")
	private String remoteWorkingDir;

	@Value("${file.upload-dir.TocReport}")
	private String localWorkingDir;

	@Value("${ftp.connection.timeout:30000}")
	private int conectionTimeout;

	@Value("${base.local.directory:/tmp/}")
	private String tmpDirectoryLocal;

	public boolean connectWithFtp(String fileName) {

		String FTPHOST = remoteHost;
		int FTPPORT = port;
		String FTPUSER = username;
		String FTPPASS = password;
		String FTPWORKINGDIR = remoteWorkingDir;
		int timeout = conectionTimeout; // 30 sec

		String prodFilePath = "/var/VST-DMS-APPS/FILES/REPORTS/TOC/";

		String property = System.getProperty("os.name");

		String filePath = "";
		if (property.contains("Windows")) {
			filePath = localWorkingDir;

		} else {
			filePath = prodFilePath;
		}

		FTPClient ftpClient = new FTPClient();
		FileInputStream localFileStream = null;

			LOGGER.info("preparing the host information for ftp.");

		try {
			ftpClient.connect(FTPHOST, FTPPORT);
			ftpClient.login(FTPUSER, FTPPASS);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			LOGGER.info("Host connected.");

			ftpClient.changeWorkingDirectory(remoteWorkingDir);
			localFileStream = new FileInputStream(filePath + fileName);
			boolean uploaded = ftpClient.storeFile(fileName, localFileStream);

			if (uploaded) {
				LOGGER.info("File transfer successful.");
			} else {
				LOGGER.info("File transfer failed.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (localFileStream != null) {
					localFileStream.close();
				}
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		return true;
	}

}