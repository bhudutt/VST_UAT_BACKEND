/**
 * 
 */
package com.hitech.dms.web.model.enquiry.edit.request;

import java.math.BigInteger;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryEditAttachImagesRequestModel {
	private BigInteger enquiryAttachImgsId;

	private EnquiryEditRequestModel enquiryHdr;

	private String file_name;
	private MultipartFile file;
}
