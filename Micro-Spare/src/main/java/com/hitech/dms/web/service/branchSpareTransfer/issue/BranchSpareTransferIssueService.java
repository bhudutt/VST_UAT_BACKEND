package com.hitech.dms.web.service.branchSpareTransfer.issue;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferIndentHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.IndentNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.issue.request.BranchSpareTransferIssueRequest;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareTransferIssueHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareTransferIssueResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.IssueDetailsResponse;

public interface BranchSpareTransferIssueService {

	List<IndentNumberDetails> fetchIndentNumberDetails(BigInteger paIndHdrId);

	BranchSpareTransferResponse createBranchSpareTransferIssue(BranchSpareTransferIssueRequest
			branchSpareTransferIssueRequest, String userCode);

	HashMap<BigInteger, String> searchIssueNumber(String searchText, String userCode);

	List<IssueDetailsResponse> fetchIssueDetails(String issueNumber, Date fromDate,
			Date toDate);

	BranchSpareTransferIssueResponse fetchIssueTransferHdrAndDtl(BigInteger paIssueHdrId);

	HashMap<BigInteger, String> searchIndentNumber(String searchText, String userCode);

	BranchSpareTransferResponse fetchIndentTransferHdrAndDtl(BigInteger paIndHdrId, String userCode, String dealerCode, 
			String page, String size);

	ApiResponse<List<BranchSpareIssueBinStockResponse>> fetchAvailableStock(BigInteger partBranchId, BigInteger partId, 
			BigInteger branchId, BigInteger dealerId, BigInteger stockBinId, String userCode);

	BranchSpareTransferResponse fetchIssueTransferHdrAndDtl(BigInteger paIndHdrId, String userCode, String dealerCode,
			String page, String size);

}
