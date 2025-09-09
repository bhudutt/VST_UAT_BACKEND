package com.hitech.dms.web.model.enquiry.digitalReport.response;

import java.math.BigInteger;


import lombok.Data;
@Data
public class DigitalUploadResponseModel {
	private BigInteger Dtl_id;
	private BigInteger Digital_Enq_HDR_ID;
	private String Digital_Enq_No;
	private String DigitalSourceName;
	private String pc_desc;
	private String Customer_Name;
	private String Customer_Mobile_No;
	private String Customer_Email_ID;
	private String Model;
	private String Customer_State;
	private String Customer_District;
	private String Customer_Tehsil;
	private String segment;
	private String Status;
	private String Error_Detail;
}
