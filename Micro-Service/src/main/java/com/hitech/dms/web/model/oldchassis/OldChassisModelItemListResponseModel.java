package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;

import javax.persistence.Column;


import lombok.Data;

@Data
public class OldChassisModelItemListResponseModel {

    
	private BigInteger machineItemID;
	private String itemNo;
	private String itemDescription;
 }
