package com.hitech.dms.web.partsStock.Service;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.partsStockController.create.response.PartsUploadCreateResponse;

public interface PartsStockUploadService {
	
	/**
	 * @param authorizationHeader
	 * @param userCode
	 * @param branch
	 * @param dealer 
	 * @param file
	 * @return
	 */
	public PartsUploadCreateResponse partsUploadService(String authorizationHeader,String userCode,Integer delaer
			,Integer branch ,MultipartFile file);

}
