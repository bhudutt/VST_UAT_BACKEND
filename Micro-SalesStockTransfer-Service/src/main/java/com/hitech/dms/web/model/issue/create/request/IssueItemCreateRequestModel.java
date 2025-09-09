/**
 * 
 */
package com.hitech.dms.web.model.issue.create.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class IssueItemCreateRequestModel {
	private BigInteger machineItemId;
	private Integer indentQty;
	private Integer issueQty;
}
