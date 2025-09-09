package com.hitech.dms.web.service.history.card;

import java.util.List;

import com.hitech.dms.web.model.service.report.request.HistoryCardResponse;
import com.hitech.dms.web.model.service.report.request.HistoryCardResponseList;

public interface HistoryCardService {

	HistoryCardResponseList getsearchDetails(String authorizationHeader, String userCode, String chassisNo);

}
