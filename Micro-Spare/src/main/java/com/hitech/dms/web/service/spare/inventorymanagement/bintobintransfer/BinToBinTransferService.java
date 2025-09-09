package com.hitech.dms.web.service.spare.inventorymanagement.bintobintransfer;

import java.math.BigInteger;

import javax.transaction.Transactional;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.entity.spare.inventorymanagement.bintobintransfer.BinToBinTransferHdr;
import com.hitech.dms.web.model.spare.inventorymanagement.bintobintransfer.SearchBinToBinTransferRequestDto;


@Transactional
public interface BinToBinTransferService {
	
	ApiResponse<?> saveBinToBinTransfer(String userCode, BinToBinTransferHdr requestModel);
	
	ApiResponse<?> getSpareEmployee(BigInteger branchId);
	
	ApiResponse<?>  autoCompletePartNo(String partNo, BigInteger branchId);
	
	ApiResponse<?> getStorelist(BigInteger partBranchId, BigInteger branchId);
	
	ApiResponse<?> getStoreBinlist(String binName, BigInteger branchId, BigInteger stockStoreId, BigInteger partBranchId);
	
	ApiResponse<?> autoSearchIssueNo(String issueNo);
	
	ApiResponse<?> autoSearchReceiptNo(String receiptNo);
	
	ApiResponse<?>  searchBinToBinTransfer(String userCode, SearchBinToBinTransferRequestDto requestModel);
	
	ApiResponse<?> viewBinToBinTransfer(BigInteger issueId);
	
	ApiResponse<?> getStockStores(BigInteger branchId, BigInteger partBranchId, BigInteger storeId);

}
