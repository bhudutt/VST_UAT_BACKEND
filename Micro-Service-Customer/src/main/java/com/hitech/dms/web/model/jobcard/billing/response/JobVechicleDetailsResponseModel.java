package com.hitech.dms.web.model.jobcard.billing.response;

import lombok.Data;

@Data
public class JobVechicleDetailsResponseModel {
	
    private String jobCardNumber;
    private String JobCardStatus;
    private String jobCardCloseDate;
    private String saleDate;
	private String chassisNo;
	private String engineNo;
	private String registrationNo;
	private String modelVariant;
	private String vinNo;
}
