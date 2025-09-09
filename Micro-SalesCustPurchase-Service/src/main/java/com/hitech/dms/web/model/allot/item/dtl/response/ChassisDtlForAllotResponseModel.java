/**
 * 
 */
package com.hitech.dms.web.model.allot.item.dtl.response;

import java.math.BigInteger;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class ChassisDtlForAllotResponseModel {
	private BigInteger machineInventoryId;
	private BigInteger vinId;
	private BigInteger machineItemId;
	private String ItemNo;
	private String itemDesc;
	private String stockType;
	private String vinNo;
	private String chassisNo;
	private String engineNo;
	private String grnNumber;
	private BigInteger grnId;
	private String invoiceNo;
	private String invoiceDate;
	private int ageInDays;
	private String productGroup;
	private Character chassisRequired;
	
	Map<BigInteger,String> grnMap;
	
}
