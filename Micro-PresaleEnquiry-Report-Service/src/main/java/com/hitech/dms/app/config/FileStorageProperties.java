package com.hitech.dms.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class FileStorageProperties {
	@Value("${file.upload-dir.Temp}")
    private String uploadDir;
	
	@Value("${file.upload-dir.Main}")
    private String uploadDirMain;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

	public String getUploadDirMain() {
		return uploadDirMain;
	}

	public void setUploadDirMain(String uploadDirMain) {
		this.uploadDirMain = uploadDirMain;
	}
    
}