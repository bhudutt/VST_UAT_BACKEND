package com.hitech.dms.web.service.branchSpareTransfer.receipt;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.dao.spare.branchTransfer.receipt.BranchSpareTransferReceiptDao;
import com.hitech.dms.web.entity.branchTransfer.indent.IndentHdrEntity;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferIndentHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.IndentNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.issue.request.BranchSpareTransferIssueRequest;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareTransferIssueHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareTransferIssueResponse;
import com.hitech.dms.web.model.spare.branchTransfer.receipt.request.BranchSpareTransferReceiptRequest;
import com.hitech.dms.web.model.spare.branchTransfer.receipt.response.BranchSpareTransferReceiptHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.receipt.response.BranchSpareTransferReceiptResponse;
import com.hitech.dms.web.model.spare.branchTransfer.receipt.response.SearchBranchTransferReceiptResponse;

@Service
public class BranchSpareTransferReceiptServiceImpl implements BranchSpareTransferReceiptService {

	@Autowired
	BranchSpareTransferReceiptDao branchSpareTransferReceiptDao;

	@Override
	public BranchSpareTransferResponse createBranchSpareTransferReceipt(
			BranchSpareTransferReceiptRequest branchSpareTransferReceiptRequest, String userCode) {
			return branchSpareTransferReceiptDao
				.createBranchSpareTransferReceipt(branchSpareTransferReceiptRequest, userCode);
	}

	@Override
	public HashMap<BigInteger, String> fetchBranchStoreList(int indentToBranchId, String userCode) {
		return branchSpareTransferReceiptDao.fetchBranchStoreList(indentToBranchId, userCode);
	}

	@Override
	public HashMap<BigInteger, String> searchBinName(String searchText,Integer indentToBranchId, Integer partId, String userCode) {
		return branchSpareTransferReceiptDao.searchBinName(searchText, indentToBranchId, partId, userCode);
	}


	@Override
	public HashMap<BigInteger, String> searchReceiptNumber(String searchText, String userCode) {
		return branchSpareTransferReceiptDao.searchReceiptNumber(searchText, userCode);
	}

	@Override
	public BranchSpareTransferReceiptResponse fetchReceiptTransferHdrAndDtl(BigInteger paReceiptId) {
		return branchSpareTransferReceiptDao.fetchReceiptTransferHdrAndDtl(paReceiptId);
	}

	@Override
	public List<SearchBranchTransferReceiptResponse> fetchReceiptDetails(String receiptNumber, Date fromDate,
			Date toDate) {
		return branchSpareTransferReceiptDao.searchReceiptDetails(receiptNumber, null, fromDate, toDate);
	}

	@Override
	public HashMap<BigInteger, String> searchIssueNumber(String searchText, String userCode) {
		return branchSpareTransferReceiptDao.searchIssueNumber(searchText, userCode);
	}	
	
}
