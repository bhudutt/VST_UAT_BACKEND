/**
 * 
 */
package com.hitech.dms.web.model.pcforbranchDealer.request;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class PcForBranchDealerRequestModel {
	private String isApplicableFor;
	private Long dealerOrBranchID;
	private String forSalesFlag;
}
