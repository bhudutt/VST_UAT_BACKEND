package com.hitech.dms.web.service.common;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.exceptions.docs.FileStorageException;

@Service
public class Utils {
	
	private Path fileStorageLocation;
	
	@Value("${file.upload-dir.Template}")
    private String uploadDir;
	
	
	public void store(MultipartFile file, String moduleName, BigInteger id, String fileName) {
		String filePath = "";
		
		if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			filePath = uploadDir + moduleName + "/Image/" + id + "/";
		}else {
			filePath = "/var/VST-DMS-APPS/FILES/Template/"+ moduleName + "/Image/" + id + "/";
		}
		
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
	
	/**
     * Returns the name of the operating system on which the JVM is running.
     * 
     * @return the OS name (Windows, Linux/Unix, macOS, or Unknown)
     */
    public String getOsName() {
        String osName = System.getProperty("os.name").toLowerCase();
        
        if (osName.contains("win")) {
            return "Windows";
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac")) {
            return "Linux/Unix/Mac";
        } else {
            return "Unknown";
        }
    }
    
    /**
     * Returns detailed information about the operating system.
     * 
     * @return the detailed OS information (name, architecture, version)
     */
    public Map<String, String> getDetailedOsInfo() {
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");

//        return String.format("OS Name: %s, OS Arch: %s, OS Version: %s", osName, osArch, osVersion);
     // Create a map to store the OS details
        Map<String, String> osDetails = new HashMap<>();
        osDetails.put("OS Name", osName);
        osDetails.put("OS Arch", osArch);
        osDetails.put("OS Version", osVersion);

        return osDetails;
    }
}
