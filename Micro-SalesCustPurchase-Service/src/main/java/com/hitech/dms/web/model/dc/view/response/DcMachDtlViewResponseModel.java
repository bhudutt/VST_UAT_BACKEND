/**
 * 
 */
package com.hitech.dms.web.model.dc.view.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcMachDtlViewResponseModel {
	private BigInteger machineDcDtlId;
	private BigInteger machineInventoryId;
	private BigInteger vinId;
	private String itemNo;
	private String itemDesc;
	private String model;
	private String vinNo;
	private String chassisNo;
	private String engineNo;
	private int qty;
}
