/**
 * 
 */
package com.hitech.dms.web.model.dc.create.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class AllotMachDtlForDCCreateRequestModel {
	private BigInteger machineInventoryId;
	private BigInteger vinId;
	private String itemNo;
	private String itemDesc;
	private String model;
	private String vinNo;
	private String chassisNo;
	private String engineNo;
	private Integer qty;
	private String productType;
	private BigInteger itemId;
}
