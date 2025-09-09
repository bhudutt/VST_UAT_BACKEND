package com.hitech.dms.web.service.history.card;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.dao.history.card.HistoryCardDao;
import com.hitech.dms.web.model.service.report.request.HistoryCardDtlResponse;
import com.hitech.dms.web.model.service.report.request.HistoryCardResponse;
import com.hitech.dms.web.model.service.report.request.HistoryCardResponseList;

@Service
public class HistoryCardServiceImpl implements HistoryCardService{
	
	@Autowired
	private HistoryCardDao historyCardDao;

	@Override
	public HistoryCardResponseList getsearchDetails(String authorizationHeader, String userCode,
			String chassisNo) {
		//List<HistoryCardResponseList>  list = new ArrayList<HistoryCardResponseList>();
		HistoryCardResponseList response = new HistoryCardResponseList();
		HistoryCardResponse header = historyCardDao.getHistoryCardHdrDetail(userCode,chassisNo);
		List<HistoryCardDtlResponse> detail = historyCardDao.getHistoryCardDetailList(userCode,chassisNo);
		response.setResponse(header);
		response.setSearchList(detail);
		return response;
	}

	

	
		
}
