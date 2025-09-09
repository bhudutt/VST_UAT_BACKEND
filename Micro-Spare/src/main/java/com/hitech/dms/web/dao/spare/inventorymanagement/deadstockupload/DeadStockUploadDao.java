package com.hitech.dms.web.dao.spare.inventorymanagement.deadstockupload;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;

import com.hitech.dms.web.model.spare.inventorymanagement.deadstockupload.DeadStockSearchRequest;

@Transactional
public interface DeadStockUploadDao {
	List<?> uploadDeadStock(Session session,BigInteger branchId,String partNo);
	
	List<?> autoCompletePartNo(Session session, String partNo, BigInteger branchId);
	
	List<?> searchDeadStockUpload(Session session, String userCode, DeadStockSearchRequest requestModel);

}
