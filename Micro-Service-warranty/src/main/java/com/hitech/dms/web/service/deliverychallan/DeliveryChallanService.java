package com.hitech.dms.web.service.deliverychallan;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;

import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.entity.deliverychallan.DeliveryChallanHdr;
import com.hitech.dms.web.model.deliverychallan.WarrantyPartsDCRequestDto;
import com.hitech.dms.web.model.deliverychallan.WcrDispatchRequestDto;

@Transactional
public interface DeliveryChallanService {
	
	ApiResponse<?> wcrDispatchSearchList(String userCode, WcrDispatchRequestDto dispatchRequestDto);
	
	ApiResponse<?> saveDeliveryChallan(String authorizationHeader, String userCode, DeliveryChallanHdr requestModel);

	ApiResponse<?> wcrItemList(String userCode, String wcrIds);
	
	ApiResponse<?> fetchAllTranspoter(String userCode);
	
	ApiResponse<?> autoSearchDcNo(String dcNo);
	
	ApiResponse<?> autoSearchLrNo(String lrNo);
	
	ApiResponse<?> warrantyPartsDCSearchList(String userCode, WarrantyPartsDCRequestDto dispatchRequestDto);
	
	ApiResponse<?> viewDcList(BigInteger id);
	
}
