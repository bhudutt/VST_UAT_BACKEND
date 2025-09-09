package com.hitech.dms.web.model.retailFollowUp.create.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class RetailFollowUpCashHistoryResponse {
	private String followUpDate;
	private String followedBy;
	private String remarks;
}
