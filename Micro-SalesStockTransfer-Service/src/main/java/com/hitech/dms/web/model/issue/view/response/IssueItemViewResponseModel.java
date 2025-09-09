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
public class IssueItemViewResponseModel {
	private BigInteger issueItemId;
	private BigInteger indentDtlId;
	private BigInteger machineItemId;
	private String itemNo;
	private String itemDesc;
	private Integer issueQty;
}
