package com.hitech.dms.web.dao.wcr.search;

import java.util.List;
import java.util.Map;

import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.model.wcr.search.WCRApprovalRequestDto;
import com.hitech.dms.web.model.wcr.search.WcrApprovalResponseDto;
import com.hitech.dms.web.model.wcr.search.WcrSearchRequestDto;
import com.hitech.dms.web.model.wcr.search.WcrSearchResponseDto;

public interface WcrSearchDao {
	
	List<Map<String, Object>> autoSearchWcrNo(String wcrNo);
	
	List<Map<String, Object>> autoSearchPcrNo(String pcrNo);
	
	ApiResponse<List<WcrSearchResponseDto>> wcrSearchList(String userCode, WcrSearchRequestDto requestModel);
	
	WcrApprovalResponseDto approveRejectWCR(String userCode,WCRApprovalRequestDto requestModel);

}
