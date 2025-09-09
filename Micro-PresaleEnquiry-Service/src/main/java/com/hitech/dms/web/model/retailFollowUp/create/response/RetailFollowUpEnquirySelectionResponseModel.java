package com.hitech.dms.web.model.retailFollowUp.create.response;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class RetailFollowUpEnquirySelectionResponseModel {
	private RetailFollowUpEnquiryDtlResponse enqDtl;
	private List<RetailFollowUpCashHistoryResponse> cashHistory;
	private List<RetailFollowUpHistoryResponse> loanHistory;
	private List<RetailPaymentReceiptHistory> retailPaymentHistory;
	private List<RetailNextStageResponseModel> nextStage;
	private Map<String, Object>  followUpStages;

}
