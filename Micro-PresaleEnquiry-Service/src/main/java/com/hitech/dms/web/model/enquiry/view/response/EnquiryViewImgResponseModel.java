package com.hitech.dms.web.model.enquiry.view.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryViewImgResponseModel {
	private BigInteger enqAttachImgId;
	private String fileName;
	private String docPath;
}
