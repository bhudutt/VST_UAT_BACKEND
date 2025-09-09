package com.hitech.dms.web.service.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.exceptions.docs.FileStorageException;

@Service
public class Utils {
	
private Path fileStorageLocation;
	
	@Value("${file.upload-dir.C:\\VST-DMS-APPS\\Micro-Frontend_Apps\\dms-service-requisition\\assets\\images\\}")
    private String uploadDir;
	
	
	public void store(MultipartFile file, String moduleName, String fileName) {
		
		String filePath = uploadDir + moduleName + "/";
		
		this.fileStorageLocation = Paths.get(filePath).toAbsolutePath().normalize();
		
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",ex);
		}
		
		try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, this.fileStorageLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file " + filePath, e);
        }
    }

}
