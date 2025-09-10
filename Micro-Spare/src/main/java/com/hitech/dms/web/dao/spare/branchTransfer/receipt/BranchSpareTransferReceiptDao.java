package com.hitech.dms.web.dao.spare.branchTransfer.receipt;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hitech.dms.web.entity.branchTransfer.indent.IndentHdrEntity;
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

public interface BranchSpareTransferReceiptDao {

	HashMap<BigInteger, String> fetchBranchStoreList(int indentToBranchId, String userCode);

	HashMap<BigInteger, String> searchBinName(String searchText, Integer indentToBranchId, Integer partId, String userCode);

	BranchSpareTransferResponse createBranchSpareTransferReceipt(
			BranchSpareTransferReceiptRequest BranchSpareTransferReceiptRequest, String userCode);

	HashMap<BigInteger, String> searchReceiptNumber(String searchText, String userCode);

	List<SearchBranchTransferReceiptResponse> searchReceiptDetails(String receiptNumber, BigInteger paReceiptId,
			Date fromDate, Date toDate);

	BranchSpareTransferReceiptResponse fetchReceiptTransferHdrAndDtl(BigInteger paReceiptId);

	HashMap<BigInteger, String> searchIssueNumber(String searchText, String userCode);

}
