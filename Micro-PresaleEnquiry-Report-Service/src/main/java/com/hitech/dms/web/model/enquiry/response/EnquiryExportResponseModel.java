package com.hitech.dms.web.model.enquiry.response;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryExportResponseModel {
	private String msg;
	private String fileName;
	private boolean isFileCreated;
}
