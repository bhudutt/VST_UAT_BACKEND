package com.hitech.dms.web.service.docs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.exceptions.docs.FileStorageException;
import com.hitech.dms.app.exceptions.docs.MyFileNotFoundException;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Service
public class FileStorageService {

	private Path fileStorageLocation;

	@Autowired
	private FileStorageProperties fileStorageProperties;

//	public FileStorageService(FileStorageProperties fileStorageProperties) {
//		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDirEnq()).toAbsolutePath().normalize();
//
//		try {
//			Files.createDirectories(this.fileStorageLocation);
//		} catch (Exception ex) {
//			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
//					ex);
//		}
//	}

	public String storeFile(MultipartFile file, String filePath) {
		if (filePath == null) {
			filePath = fileStorageProperties.getUploadDir();
		}
		this.fileStorageLocation = Paths.get(filePath).toAbsolutePath().normalize();
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	public Resource loadFileAsResource(String fileName, String docPath, Long id) {
		try {
			if (docPath == null) {
				docPath = fileStorageProperties.getUploadDir();
			} else {
				if(id != null) {
					docPath = docPath + "/" + id ;
				}
			}
			this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDirMain()+docPath).toAbsolutePath().normalize();
//			System.out.println(this.fileStorageLocation);
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
	}
}
