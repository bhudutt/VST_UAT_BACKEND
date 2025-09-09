/**
 * 
 */
package com.hitech.dms.web.model.issue.indent.dtl.response;

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
public class IndentMachDtlResponseModel {
//	private BigInteger issueDtlId;
	private BigInteger indentDtlId;
	private BigInteger vinId;
	private BigInteger machineItemId;
	private BigInteger machineInventoryId;
	private String itemNo;
	private String itemDesc;
	private String chassisNo;
	private String engineNo;
	private String model;
	private String variant;
	private String vinNo;
	private Integer indentQty;
	private Integer issueQty;
	private Integer pendingQty;
}
