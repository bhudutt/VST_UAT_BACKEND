package com.hitech.dms.web.controller.docs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.web.model.doc.response.UploadFileResponse;
import com.hitech.dms.web.service.docs.FileStorageService;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@RestController
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private FileStorageProperties fileStorageProperties;

	@PostMapping("/uploadFile")
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("isFor") String isFor) {
		String filePath = fileStorageProperties.getUploadDir();
		if (isFor != null && isFor.equalsIgnoreCase("Enquiry")) {
			filePath = fileStorageProperties.getUploadDirMain();
		}
		String fileName = fileStorageService.storeFile(file, filePath);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
				.path(fileName).toUriString();

		return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}

	@PostMapping("/uploadMultipleFiles")
	public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
			@RequestParam("isFor") String isFor) {
		return Arrays.asList(files).stream().map((file) -> {
			String filePath = fileStorageProperties.getUploadDir();
			if (isFor != null && isFor.equalsIgnoreCase("Enquiry")) {
				filePath = fileStorageProperties.getUploadDirMain();
			}
			return uploadFile(file, filePath);
		}).collect(Collectors.toList());
	}

	@GetMapping("/downloadFile/{docPath}/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, @PathVariable("docPath") String docPath,
			@RequestParam(required = false) Long id, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName, docPath, id);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@RequestMapping(value = "/download/{isFor}/docs/{fileName}", method = RequestMethod.GET)
	public void documentDownload(@PathVariable(value = "fileName") String fileName,
			@PathVariable(value = "isFor") String isFor, HttpServletRequest request,
			OAuth2Authentication authentication, HttpServletResponse response, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug(this.getClass().getName() + "is invoking download method...");
		}
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		logger.debug("fileName : : " + fileName);
		String filePath = fileStorageProperties.getUploadDir() + messageSource.getMessage("label.temp.enq.path",
				new Object[] { userCode }, LocaleContextHolder.getLocale());
		OutputStream outStream = null;
		FileInputStream inStream = null;
		File downloadFile = null;
		byte[] buffer = null;
		if (isFor != null && isFor.equals("QUO")) {
			filePath = fileStorageProperties.getUploadDir() + messageSource.getMessage("label.temp.quo.path",
					new Object[] { userCode }, LocaleContextHolder.getLocale());
		}
		try {
			if (filePath != null) {
				if (fileName != null) {
					String fileNameExt = CommonUtils.extractExtension(fileName);
					if (fileNameExt != null && !fileNameExt.equals("")) {
						fileName = fileName.replaceFirst("[.][^.]+$", CommonUtils.getExtension(fileName));
					}
				}
				filePath = filePath + fileName;
				logger.debug("filePath : : " + filePath);
				downloadFile = new File(filePath + ".xlsx"); // +
																// fileDetails[1]
				printPaths(downloadFile);
				validatePath(filePath + ".xlsx");
				inStream = new FileInputStream(downloadFile);

				// if you want to use a relative path to context root:
				String relativePath = request.getServletContext().getRealPath("");
				logger.debug("relativePath = " + relativePath);

				// obtains ServletContext
				ServletContext context = request.getServletContext();

				// gets MIME type of the file
				String mimeType = context.getMimeType(filePath); // +
																	// fileDetails[1]
				if (mimeType == null) {
					// set to binary type if MIME mapping not found
					mimeType = "application/octet-stream";
				}
				logger.debug("MIME type: " + mimeType);

				// modifies response
				response.setContentType(mimeType);
				response.setContentLength((int) downloadFile.length());

				// forces download
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
				if (device.isMobile() || device.isTablet()) {
					headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
				}
				response.setHeader(headerKey, headerValue);
				headerKey = null;
				headerValue = null;
				mimeType = null;
				relativePath = null;
				// obtains response's output stream
				outStream = response.getOutputStream();

				buffer = new byte[6144];
				int bytesRead = -1;

				while ((bytesRead = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error(this.getClass().getName(), e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(this.getClass().getName(), e);
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e);
				} catch (Exception e) {
					logger.error(this.getClass().getName(), e);
				}
				downloadFile = null;
				buffer = null;
				fileName = null;
				filePath = null;
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e);
				} catch (Exception e) {
					logger.error(this.getClass().getName(), e);
				}
			}
			try {
				recursiveDeleteFilesOlderThanNDays(2, filePath);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(this.getClass().getName(), e);
			} catch (Exception e) {
				logger.error(this.getClass().getName(), e);
			}
		}
	}

	private static void printPaths(File file) throws IOException {
		logger.info("Absolute Path: " + file.getAbsolutePath());
		logger.info("Canonical Path: " + file.getCanonicalPath());
		logger.info("Path: " + file.getPath());
	}

	private static void validatePath(String filePath) {
		Path path = Paths.get(filePath);
		// Verifying if the file is readable
		boolean bool = Files.isReadable(path);
		if (bool) {
			logger.info("readable");
		} else {
			logger.info("not readable");
		}
		bool = Files.isWritable(path);
		if (bool) {
			logger.info("writable");
		} else {
			logger.info("not writable");
		}
		bool = Files.isExecutable(path);
		if (bool) {
			logger.info("executable");
		} else {
			logger.info("not executable");
		}
	}

	public static void recursiveDeleteFilesOlderThanNDays(int days, String dirPath) throws IOException {
		long cutOff = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000);
		Files.list(Paths.get(dirPath)).forEach(path -> {
			if (Files.isDirectory(path)) {
				try {
					recursiveDeleteFilesOlderThanNDays(days, path.toString());
				} catch (IOException e) {
					// log here and move on
				}
			} else {
				try {
					if (Files.getLastModifiedTime(path).to(TimeUnit.MILLISECONDS) < cutOff) {
						Files.delete(path);
					}
				} catch (IOException ex) {
					// log here and move on
				}
			}
		});
	}

}
