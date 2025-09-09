package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadDisagreeDocumentModel {
	
	private String grnId;
	private MultipartFile document1;
	private String isFor;
	private int statusCode;
	private String statusMessage;

}
