package com.hitech.dms.web.dao.deliverychallan;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.hitech.dms.web.model.deliverychallan.WarrantyPartsDCRequestDto;
import com.hitech.dms.web.model.deliverychallan.WcrDispatchRequestDto;

public interface DeliveryChallanDao  {

    List<?> wcrDispatchSearchList(Session session, String userCode, WcrDispatchRequestDto dispatchRequestDto);
    
    List<Map<String, Object>> wcrItemList(Session session, String wcrIds);
    
    List<Map<String, Object>> fetchAllTranspoter(Session session);
    
    List<?> autoSearchDcNo(Session session, String dcNo);
    
    List<?> autoSearchLrNo(Session session, String lrNo);
    
    List<?> warrantyPartsDCSearchList(Session session, String userCode,WarrantyPartsDCRequestDto requestModel);
    
    List<?> viewDcList(Session session, BigInteger id, Integer flag);
	
}
