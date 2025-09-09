package com.hitech.dms.web.dao.goodwill;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;

import com.hitech.dms.web.model.goodwill.GoodwillApprovalRequestDto;
import com.hitech.dms.web.model.goodwill.GoodwillSearchRequestDto;

@Transactional
public interface GoodwillDao {
	
	List<?> getApprovalHierarchy(Session session);
	
	List<?> autoSearchGoodwillNo(Session session, String goodwillNo);
	
	List<?> autoSearchJcNo(Session session, String roNo);
	
	List<?> autoSearchPcrNo(Session session, String pcrNo);
	
	List<?> autoSearchChassisNo(Session session, String chassisNo);
	
	List<?> goodwillSearchList(Session session, String userCode, GoodwillSearchRequestDto requestModel);
	
	List<?> approveRejectGoodwill(Session session, String userCode, GoodwillApprovalRequestDto requestModel);

	List<?> getViewData(Session session, BigInteger goodwillId, int flag);
	
}
