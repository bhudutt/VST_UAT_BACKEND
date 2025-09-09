package com.hitech.dms.web.dao.history.card;

import java.util.List;

import com.hitech.dms.web.model.service.report.request.HistoryCardDtlResponse;
import com.hitech.dms.web.model.service.report.request.HistoryCardResponse;

public interface HistoryCardDao {

	List<HistoryCardDtlResponse> getHistoryCardDetailList(String userCode, String chassisNo);

	HistoryCardResponse getHistoryCardHdrDetail(String userCode, String chassisNo);

}
