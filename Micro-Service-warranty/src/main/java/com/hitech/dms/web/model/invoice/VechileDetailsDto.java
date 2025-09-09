package com.hitech.dms.web.model.invoice;

import java.util.Date;

import lombok.Data;

@Data
public class VechileDetailsDto {
	private String chassisNo;
	private String engineNo;
	private String registrationNo;
	private String modelVariant;
	private String vinNo;
	private Date saleDate;
	

}
