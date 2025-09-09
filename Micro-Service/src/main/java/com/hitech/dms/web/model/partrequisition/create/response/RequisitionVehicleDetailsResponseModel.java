package com.hitech.dms.web.model.partrequisition.create.response;

import java.math.BigInteger;

import lombok.Data;

@Data
public class RequisitionVehicleDetailsResponseModel {

	private BigInteger vinId;
	private BigInteger customerId;
	private String chassisnNo;
	private String registrationNumber;
	private String modelVariant;
	private String customerName;
}
