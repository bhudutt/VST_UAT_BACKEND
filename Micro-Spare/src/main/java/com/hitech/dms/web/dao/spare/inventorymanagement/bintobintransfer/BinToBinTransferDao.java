package com.hitech.dms.web.dao.spare.inventorymanagement.bintobintransfer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;

import com.hitech.dms.web.model.spare.inventorymanagement.bintobintransfer.SearchBinToBinTransferRequestDto;

@Transactional
public interface BinToBinTransferDao {
	
	List<?> getSpareEmployee(Session session, BigInteger branchId);
	
	List<?> autoCompletePartNo(Session session, String partNo, BigInteger branchId);
	
	List<?> getStorelist(Session session, BigInteger partBranchId, BigInteger branchId);
	
	List<?> getStoreBinlist(Session session, String binName, BigInteger branchId, BigInteger stockStoreId, BigInteger partBranchId);
	
	List<?> createBin(Session session, BigInteger branchId, BigInteger partBranchId, BigInteger storeId, String binName, String createdBy);
	
	String updateStock(Session session, String flag, BigInteger branchId, BigInteger partBranchId, 
			BigInteger stockStoreId, BigInteger stockBinId, BigDecimal qtyToBeAdded, BigDecimal qtyToBeSubtracted, 
			BigDecimal basicUnitPrice, String tableName, String modifiedBy);
	
	BigDecimal getBasicUnitPrice(Session session, String userCode, BigInteger branchId, BigInteger partBranchId);
	
	List<?> autoSearchIssueNo(Session session, String issueNo);
	
	List<?> autoSearchReceiptNo(Session session, String receiptNo);
	
	List<?> searchBinToBinTransfer(Session session, String userCode, SearchBinToBinTransferRequestDto requestModel);
	
	List<?> viewBinToBinTransfer(Session session, BigInteger issueId, Integer flag);
	
	List<?> getStockStores(Session session, BigInteger branchId, BigInteger partBranchId, BigInteger storeId);

}
