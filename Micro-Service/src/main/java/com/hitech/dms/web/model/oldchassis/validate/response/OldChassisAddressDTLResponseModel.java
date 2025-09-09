package com.hitech.dms.web.model.oldchassis.validate.response;

import lombok.Data;

@Data
public class OldChassisAddressDTLResponseModel {

	private String district;
	private String tehsil;
	private String city;
	private String pinCode;
	private String state;
	private String country;
}
