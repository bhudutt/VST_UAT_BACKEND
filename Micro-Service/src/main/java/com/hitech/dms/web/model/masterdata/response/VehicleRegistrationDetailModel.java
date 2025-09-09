package com.hitech.dms.web.model.masterdata.response;

import lombok.Data;

@Data
public class VehicleRegistrationDetailModel {

	private Integer vinNvrDetailId;
	private String registrationNo;
	private String bookletNumber;
	private String nvrMake;	
	private String nvrSerialNo;	
	private String aggregateName;
	private Integer tcardAggregateId;
}
