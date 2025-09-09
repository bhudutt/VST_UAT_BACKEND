package com.hitech.dms.web.model.digitalUpload.create.request;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


/**
 * @author vinay.gautam
 *
 */
@Data
public class DigitalEnquiReportModel {
	
	@JsonProperty("digitalHdrId")
	private BigInteger Digital_HDR_ID;
	
	@JsonProperty("digitalEnqNo")
	private String Digital_Enq_No;
	
	@JsonProperty("uploadDate")
	private String UploadDate;
	
	@JsonProperty("digitalSourceName")
	private String DigitalSourceName;
	
	@JsonProperty("pcDesc")
	private String pc_desc;
	

}
