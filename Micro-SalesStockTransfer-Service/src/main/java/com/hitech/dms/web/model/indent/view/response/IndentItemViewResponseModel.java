/**
 * 
 */
package com.hitech.dms.web.model.indent.view.response;

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
public class IndentItemViewResponseModel {
	private BigInteger indentDtlId;
	private BigInteger machineItemId;
	private String itemNo;
	private String itemDesc;
	private String model;
	private Integer indentQty;
	private Integer issueQty;
	private Integer pendingQty;
}
