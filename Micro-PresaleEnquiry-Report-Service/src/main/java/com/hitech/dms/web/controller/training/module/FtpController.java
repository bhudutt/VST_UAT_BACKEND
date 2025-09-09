package com.hitech.dms.web.controller.training.module;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.web.model.training.module.FileItem;
import com.hitech.dms.web.service.sftp.TraningModuleFtpIntegrationService;
import java.nio.file.*;

@RestController
@RequestMapping("/ftp")
public class FtpController {
	
	@Autowired
	TraningModuleFtpIntegrationService ftpService;
	
	/*
	 * @GetMapping("/list") public ResponseEntity<List<FileItem>>
	 * listFiles(@RequestParam String directory) { List<FileItem> files=null; try {
	 * files = ftpService.listFiles(directory);
	 * 
	 * } catch (IOException e) { return new
	 * ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); }
	 * 
	 * return ResponseEntity.ok(files); }
	 */
	
	
	
	
	
	@GetMapping("/getOsName")
	public Map<String,String> getOSName() {
		Map<String,String> map=new LinkedHashMap<String, String>();
		String osName="Windows";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			osName="Windows";
			map.put("name", osName);
		}else {
			osName="linux";
			map.put("name", osName);
		}
		return map;
	}
	
	@GetMapping("/list")
    public ResponseEntity<List<FileItem>> listFiles(@RequestParam String directory) {
        File rootDir = new File(directory);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            return ResponseEntity.badRequest().body(null);
        }

        List<FileItem> files = Arrays.stream(rootDir.listFiles())
                .map(file -> new FileItem(file.getName(), file.isDirectory()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(files);
    }
	
	/*
	 * @GetMapping("/download") public ResponseEntity<byte[]>
	 * downloadFile(@RequestParam String directory, @RequestParam String fileName) {
	 * try { byte[] fileData = ftpService.downloadFile(directory, fileName);
	 * HttpHeaders headers = new HttpHeaders();
	 * headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
	 * fileName + "\""); return new ResponseEntity<>(fileData, headers,
	 * HttpStatus.OK); } catch (IOException e) { return new
	 * ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); } }
	 */
    
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String directory, @RequestParam String fileName) {
        try {
            File file = new File(directory, fileName);
            if (!file.exists() || file.isDirectory()) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = file.toPath();
            Resource resource = new UrlResource(filePath.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
