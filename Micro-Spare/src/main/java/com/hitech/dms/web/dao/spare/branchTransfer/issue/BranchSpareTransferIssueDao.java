package com.hitech.dms.web.dao.spare.branchTransfer.issue;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.entity.branchTransfer.indent.IndentHdrEntity;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferIndentHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.IndentNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.issue.request.BranchSpareTransferIssueRequest;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareTransferIssueHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareTransferIssueResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.IssueDetailsResponse;

public interface BranchSpareTransferIssueDao {

	public List<IndentNumberDetails> fetchIndentNumberDetails(BigInteger paIndHdrId);

	public BranchSpareTransferResponse createBranchSpareTransferIssue(
			BranchSpareTransferIssueRequest branchSpareTransferIssueRequest,
			String userCode);

	public HashMap<BigInteger, String> searchIssueNumber(String searchText, String userCode);

	public List<IssueDetailsResponse> fetchIssueDetails(String issueNumber, 
			BigInteger paIssueId, Date fromDate, Date toDate);

	public BranchSpareTransferIssueResponse fetchIssueTransferHdrAndDtl(BigInteger paIssueHdrId);

	public HashMap<BigInteger, String> searchIndentNumber(String searchText, String userCode);

	public BranchSpareTransferResponse fetchIndentTransferHdrAndDtl(BigInteger paIndHdrId, String userCode,
			String dealerCode, String page, String size);

	public ApiResponse<List<BranchSpareIssueBinStockResponse>> fetchAvailableStock(BigInteger partBranchId, BigInteger partId, 
			BigInteger branchId, BigInteger dealerId, BigInteger stockBinId, String userCode);

	public List<BranchSpareTransferIndentHdrResponse> fetchIssueHeader(String usercode,String dealerCode, BigInteger paIndHdrId,
			Date fromDate, Date toDate, Integer page, Integer size);

	public List<IndentNumberDetails> fetchIssueTransferDtl(BigInteger paIndHdrId, String userCode, String dealerCode);

}
