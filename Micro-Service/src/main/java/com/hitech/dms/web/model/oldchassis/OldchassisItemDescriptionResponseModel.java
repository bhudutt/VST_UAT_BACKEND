package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;

import lombok.Data;

@Data
public class OldchassisItemDescriptionResponseModel {

	private BigInteger itemId;
	private String itemNumber;
	private String itemDescription;
	
}
