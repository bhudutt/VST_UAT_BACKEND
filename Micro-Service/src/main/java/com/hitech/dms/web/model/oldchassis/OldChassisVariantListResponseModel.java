package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;

import javax.persistence.Column;

import lombok.Data;

@Data
public class OldChassisVariantListResponseModel {

	private BigInteger machineitemID;
	private String item;
	private String itemDescription;
    private String series;
    private String segment;
    private String variant;
	
}
