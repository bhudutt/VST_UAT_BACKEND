package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;

import javax.persistence.Column;

import lombok.Data;

@Data
public class OldChassisVariantListRequestModel {
   
	@Column(name="machine_item_id")
	private BigInteger machineitemID;
	@Column(name="variant")
	private String variant;
	
}
