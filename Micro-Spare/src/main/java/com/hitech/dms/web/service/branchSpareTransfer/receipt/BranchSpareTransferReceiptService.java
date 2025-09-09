package com.hitech.dms.web.service.branchSpareTransfer.receipt;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

public interface BranchSpareTransferReceiptService {

//	public List<IndentNumberDetails> fetchIssueNumberDetails(BigInteger paIssueHdrId);

	public BranchSpareTransferResponse createBranchSpareTransferReceipt(
			BranchSpareTransferReceiptRequest BranchSpareTransferReceiptRequest, String userCode);

	public HashMap<BigInteger, String> searchReceiptNumber(String searchText, String userCode);

	public BranchSpareTransferReceiptResponse fetchReceiptTransferHdrAndDtl(BigInteger paReceiptId);

	public List<BranchSpareTransferReceiptHdrResponse> fetchReceiptDetails(String receiptNumber, Date fromDate,
			Date toDate);

	public HashMap<BigInteger, String> fetchBranchStoreList(int indentToBranchId, String userCode);

	public HashMap<BigInteger, String> searchBinName(String searchText, Integer indentToBranchId, String userCode);

	public HashMap<BigInteger, String> searchIssueNumber(String searchText, String userCode);

}
