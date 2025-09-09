/**
 * 
 */
package com.hitech.dms.web.model.enquiry.create.request;

import java.math.BigInteger;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryAttachImagesRequestModel {
	private BigInteger enquiryAttachImgsId;

	private EnquiryCreateRequestModel enquiryHdr;

	private String file_name;
	private MultipartFile file;
}
