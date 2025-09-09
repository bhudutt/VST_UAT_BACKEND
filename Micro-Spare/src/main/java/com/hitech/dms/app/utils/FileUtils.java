package com.hitech.dms.app.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.exceptions.docs.FileStorageException;
import com.hitech.dms.constants.WebConstants;

/**
 * @author dinesh.jakhar
 *
 */
public class FileUtils {

	public static Map<String, String> uploadDOCDTLDOCS(String doFilePath, MultipartFile multiPartfile) {
		Map<String, String> fileMapData = null;
		File directory = null;
		try {
			directory = new File(doFilePath);
			if (!directory.exists()) {
				directory.mkdirs();
			} else {
//				FileUtils.cleanDirectory(directory);
			}
			// set file Name also
			if (multiPartfile != null && multiPartfile.getOriginalFilename() != null
					&& !multiPartfile.getOriginalFilename().equals("")) {
				fileMapData = uploadDocs(multiPartfile, doFilePath);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		directory = null;
		return fileMapData;
	}

	public static Map<String, String> uploadDocs(MultipartFile multiPartfile, String filePath) {
		Map<String, String> mapData = new HashMap<String, String>();
		try {

			// Get the file and save it
			byte[] bytes = multiPartfile.getBytes();
			Path path = Paths.get(filePath + multiPartfile.getOriginalFilename());
			if (!Files.exists(path.getParent())) {
				Files.createDirectories(path.getParent());
			}
			// Files.createFile(path);
			Files.write(path, bytes);

			mapData.put("fileName", multiPartfile.getOriginalFilename());
			mapData.put("fileType", multiPartfile.getContentType());
			mapData.put("MSG", WebConstants.SUCCESS);

		} catch (IOException e) {
			mapData.put("MSG", WebConstants.ERROR);
			e.printStackTrace();
		}
		return mapData;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
}
