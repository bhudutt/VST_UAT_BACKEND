/**
 * 
 */
package com.hitech.dms.web.model.salesman.request;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class SalesmanListFormModel {
	private String userCode;
	private String dealerOrBranch;
	private Long dealerOrBranchID;
}
