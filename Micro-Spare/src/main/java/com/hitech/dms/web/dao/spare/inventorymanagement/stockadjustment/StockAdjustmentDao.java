package com.hitech.dms.web.dao.spare.inventorymanagement.stockadjustment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;

import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.ApprovalAdjPartDetail;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.SearchStockAdjustmentRequestDto;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.StockAdjustmentApprovalRequestDto;

@Transactional
public interface StockAdjustmentDao {
	
	List<?> checkIfPartOrMrpExist(Session session, BigInteger branchId, String storeName, String binName,
			String partNo, BigDecimal partMrp, String adjustmentType);
	
	List<?> getApprovalHierarchy(Session session, boolean dealerFlag);
	
	List<?> autoSearchAdjustmentNo(Session session, String adjustmentNo);
	
	List<?> searchStockAdjustment(Session session, String userCode, SearchStockAdjustmentRequestDto requestModel);
	
	List<?> viewStockAdjustment(Session session, BigInteger adjustmentId, Integer flag);
	
	List<?> approveRejectStockAdj(Session session, String userCode, BigInteger hoUserId, StockAdjustmentApprovalRequestDto requestModel);

	List<?> getMrplist(Session session, BigInteger partId);
	
	List<?> updatePartMrp(Session session, BigInteger adjustmentId);
	
	List<?> autoCompletePartNo(Session session, String partNo, BigInteger branchId);
	
	List<?> getStorelist(Session session, BigInteger partBranchId, BigInteger branchId);
	
	void insertDataFromStoreTemptoUpload(Session session, BigInteger adjustmentId);
	
	void uploadStockDataToMain(Session session);

	void uploadStockAdjustmentDetail(Session session, List<ApprovalAdjPartDetail> list);


}
