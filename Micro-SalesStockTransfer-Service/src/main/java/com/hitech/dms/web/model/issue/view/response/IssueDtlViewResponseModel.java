/**
 * 
 */
package com.hitech.dms.web.model.issue.view.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class IssueDtlViewResponseModel {
	private BigInteger issueDtlId;
	private BigInteger indentDtlId;
	private BigInteger machineItemId;
	private BigInteger machineInventoryId;
	private BigInteger vinId;
	private String itemNo;
	private String itemDesc;
	private String model;
	private String chassisNo;
	private String variant;
}
