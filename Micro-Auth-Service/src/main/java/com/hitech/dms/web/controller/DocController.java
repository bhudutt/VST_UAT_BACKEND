/**
 * 
 */
package com.hitech.dms.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.errors.EntityNotFoundException;
import com.hitech.dms.utils.CommonUtils;
import com.hitech.dms.web.entity.user.UserEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/api/common")
@Slf4j
public class DocController {
	private static final Logger logger = LoggerFactory.getLogger(DocController.class);

	@Autowired
	private MessageSource messageSource;

	@GetMapping("/downloadEcatDocs/{isFor}/{fileName:.+}")
	public ResponseEntity<org.springframework.core.io.Resource> downloadFile(
			@PathVariable(value = "fileName") String fileName, @PathVariable(value = "isFor") String isFor,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			OAuth2Authentication authentication) throws IOException {
		String userCode = null;
		UserEntity user = null;
		if (authentication != null) {
			user = (UserEntity) authentication.getUserAuthentication().getPrincipal();
			userCode = user.getUsername();
		}
		if (userCode != null) {
			String filePath = messageSource.getMessage("ecat.temp.cart.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
			org.springframework.core.io.Resource file = download(fileName, filePath);
			Path path = file.getFile().toPath();

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
					.body(file);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	private org.springframework.core.io.Resource download(String filename, String path) {
		try {
			Path file = Paths.get(path).resolve(filename);
			org.springframework.core.io.Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new EntityNotFoundException("File Name ", filename, " not found!");
			}
		} catch (MalformedURLException e) {
			System.err.println(e.getMessage());
//			throw new RuntimeException("Error: " + e.getMessage());
			throw new EntityNotFoundException("File Name ", filename, e.getMessage());
		}
	}

	@RequestMapping(value = "/download/{isFor}/docs/{fileName}", method = RequestMethod.GET)
	public void documentDownload(@PathVariable(value = "fileName") String fileName,
			@PathVariable(value = "isFor") String isFor, Device device, HttpServletRequest request,
			HttpServletResponse response, OAuth2Authentication authentication) {
		if (logger.isDebugEnabled()) {
			logger.debug(this.getClass().getName() + "is invoking mbomDownload method...");
		}
		System.out.println("fileName : : " + fileName);
		String userCode = null;
		UserEntity user = null;
		if (authentication != null) {
			user = (UserEntity) authentication.getUserAuthentication().getPrincipal();
			userCode = user.getUsername();
		}
		String filePath = messageSource.getMessage("vecv.temp.cart.tempFile.path", new Object[] { userCode },
				LocaleContextHolder.getLocale());
		if (isFor.equalsIgnoreCase("bomGroup")) {
			filePath = messageSource.getMessage("vecv.temp.bomgrp.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("feedBack")) {
			filePath = messageSource.getMessage("vecv.temp.feedBack.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("repPartList")) {
			filePath = messageSource.getMessage("vecv.temp.repPartList.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("repFertList")) {
			filePath = messageSource.getMessage("vecv.temp.repFertList.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("repObsList")) {
			filePath = messageSource.getMessage("vecv.temp.repOBSList.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("repSuppList")) {
			filePath = messageSource.getMessage("vecv.temp.repSUPPList.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("repVinList")) {
			filePath = messageSource.getMessage("vecv.temp.repVin.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("repUserList")) {
			filePath = messageSource.getMessage("vecv.temp.repUSRList.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("repChangeList")) {
			filePath = messageSource.getMessage("vecv.temp.repCHLIST.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("repREVDETList")) {
			filePath = messageSource.getMessage("vecv.temp.reprevDetLIST.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("popendencyList")) {
			filePath = messageSource.getMessage("vecv.temp.popendency.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("pricependencyList")) {
			filePath = messageSource.getMessage("vecv.temp.pricependency.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("dealerMappingTemplatePath")) {
			filePath = messageSource.getMessage("label.dealerMapping.template.path", new Object[] {},
					LocaleContextHolder.getLocale());
			filePath = request.getServletContext().getRealPath(filePath);
		}
		if (isFor.equalsIgnoreCase("sbomQCCheckSheetTemplatePath")) {
			filePath = messageSource.getMessage("label.qccheck.sheet.template.path", new Object[] {},
					LocaleContextHolder.getLocale());
			filePath = request.getServletContext().getRealPath(filePath);
		}
		if (isFor.equalsIgnoreCase("sbomQCCheckSheetPath")) {
			filePath = messageSource.getMessage("vecv.temp.QCCheckSheet.file.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
//			filePath = request.getServletContext().getRealPath(filePath);
		}
		if (isFor.equalsIgnoreCase("dealerMappingFilePath")) {
			filePath = messageSource.getMessage("label.dealerMapping.dealer.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("exportBomModel")) {
			filePath = messageSource.getMessage("vecv.temp.repSUPPList.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("exportGRPMBomModel")) {
			filePath = messageSource.getMessage("vecv.temp.grpBom.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("exportECNMBomModel")) {
			filePath = messageSource.getMessage("vecv.temp.ecnmBom.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("ecnSTGDATATemplatePath")) {
			filePath = messageSource.getMessage("label.ecnmBom.template.path", new Object[] {},
					LocaleContextHolder.getLocale());
			filePath = request.getServletContext().getRealPath(filePath);
		}
		if (isFor.equalsIgnoreCase("popriceTemplatePath")) {
			filePath = messageSource.getMessage("label.PartPOPrice.template.path", new Object[] {},
					LocaleContextHolder.getLocale());
			filePath = request.getServletContext().getRealPath(filePath);
		}
		if (isFor.equalsIgnoreCase("cartTemplatePath")) {
			filePath = messageSource.getMessage("label.cart.template.path", new Object[] {},
					LocaleContextHolder.getLocale());
			filePath = request.getServletContext().getRealPath(filePath);
		}
		if (isFor.equalsIgnoreCase("changeHistoryPath")) {
			filePath = messageSource.getMessage("vecv.temp.chngHistory.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("FERTREPORTPath")) {
			filePath = messageSource.getMessage("vecv.temp.fert.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("ecnTemplatePath")) {
			filePath = messageSource.getMessage("label.ecn.template.path", new Object[] {},
					LocaleContextHolder.getLocale());
			filePath = request.getServletContext().getRealPath(filePath);
		}
		if (isFor.equalsIgnoreCase("NEWFERTLIST")) {
			filePath = messageSource.getMessage("vecv.temp.ecnMbomNewFert.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("IDENTIFYNEWFERTLIST")) {
			filePath = messageSource.getMessage("vecv.temp.ecnMbomidenNewFert.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("FERTDASH")) {
			filePath = messageSource.getMessage("vecv.temp.fertDash.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("ECNMBOMLIST")) {
			filePath = messageSource.getMessage("vecv.temp.ecnDataReport.tempFile.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("matrixDoc")) {
			filePath = messageSource.getMessage("vecv.temp.matrix.tempFile.path", new Object[] {},
					LocaleContextHolder.getLocale());
		}
		if (isFor.equalsIgnoreCase("kitTemplatePath")) {
			filePath = messageSource.getMessage("label.kit.template.path", new Object[] {},
					LocaleContextHolder.getLocale());
			filePath = request.getServletContext().getRealPath(filePath);
		}
		OutputStream outStream = null;
		FileInputStream inStream = null;
		File downloadFile = null;
		byte[] buffer = null;
		try {
			if (filePath != null) {
				/*
				 * System.out.println("Encrypted fileName : "+Encryptor. hexTobytesx(fileName));
				 * filePath = filePath+"\\"+Encryptor.decryptText(Encryptor.hexTobytesx(
				 * fileName), Encryptor.getSecretEncryptionKey());
				 * System.out.println("Decrypted fileName : "+fileName);
				 */
				if (fileName != null) {
					String fileNameExt = CommonUtils.extractExtension(fileName);
					if (fileNameExt != null && !fileNameExt.equals("")) {
						fileName = fileName.replaceFirst("[.][^.]+$", CommonUtils.getExtension(fileName));
					}
				}
				filePath = filePath + "\\" + fileName;
				System.out.println("filePath : : " + filePath);
				downloadFile = new File(filePath + ".xlsx"); // +
																// fileDetails[1]
				inStream = new FileInputStream(downloadFile);

				// if you want to use a relative path to context root:
				String relativePath = request.getServletContext().getRealPath("");
				System.out.println("relativePath = " + relativePath);

				// obtains ServletContext
				ServletContext context = request.getServletContext();

				// gets MIME type of the file
				String mimeType = context.getMimeType(filePath); // +
																	// fileDetails[1]
				if (mimeType == null) {
					// set to binary type if MIME mapping not found
					mimeType = "application/octet-stream";
				}
				System.out.println("MIME type: " + mimeType);

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
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				downloadFile = null;
				buffer = null;
				fileName = null;
				filePath = null;
			}
//			if(outStream != null) {
//				try {
//					outStream.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (Exception e) {
//					e.printStackTrace();
//				} 
//			}
		}
	}
}
