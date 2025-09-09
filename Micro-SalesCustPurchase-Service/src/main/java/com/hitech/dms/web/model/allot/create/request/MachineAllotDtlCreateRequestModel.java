/**
 * 
 */
package com.hitech.dms.web.model.allot.create.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class MachineAllotDtlCreateRequestModel {
	private Boolean isAlloted;
	private BigInteger machineInventoryId;
	private BigInteger vinId;
	private String itemNo;
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
	private BigInteger machineItemId;
	private BigInteger quantity;
}
