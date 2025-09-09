package com.hitech.dms.web.model.retailFollowUp.create.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class RetailFollowUpHistoryResponse {
	
	private String followUpDate;
	private String followedBy;
	private String financier;
	private String retailFinancierStage;
//	private String loanStatus;
	private String reasonForRejection;
	private String remarks;
	
	

}
