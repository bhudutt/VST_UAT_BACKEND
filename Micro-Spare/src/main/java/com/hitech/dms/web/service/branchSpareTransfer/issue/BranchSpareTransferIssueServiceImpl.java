package com.hitech.dms.web.service.branchSpareTransfer.issue;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.dao.spare.branchTransfer.issue.BranchSpareTransferIssueDao;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.IndentNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.issue.request.BranchSpareTransferIssueRequest;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareTransferIssueHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareTransferIssueResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.IssueDetailsResponse;

@Service
public class BranchSpareTransferIssueServiceImpl implements BranchSpareTransferIssueService {

	@Autowired
	BranchSpareTransferIssueDao branchSpareTransferIssueDao;

	@Override
	public List<IndentNumberDetails> fetchIndentNumberDetails(BigInteger paIndHdrId) {
		return branchSpareTransferIssueDao.fetchIndentNumberDetails(paIndHdrId);
	}

	@Override
	public BranchSpareTransferResponse createBranchSpareTransferIssue(
			BranchSpareTransferIssueRequest branchSpareTransferIssueRequest, String userCode) {
		return branchSpareTransferIssueDao.createBranchSpareTransferIssue(branchSpareTransferIssueRequest, userCode);
	}

	@Override
	public HashMap<BigInteger, String> searchIssueNumber(String searchText, String userCode) {
		return branchSpareTransferIssueDao.searchIssueNumber(searchText, userCode);
	}

	@Override
	public List<IssueDetailsResponse> fetchIssueDetails(String issueNumber, Date fromDate, Date toDate) {
		return branchSpareTransferIssueDao.fetchIssueDetails(issueNumber, null, fromDate, toDate);
	}

	@Override
	public BranchSpareTransferIssueResponse fetchIssueTransferHdrAndDtl(BigInteger paIssueHdrId) {
		return branchSpareTransferIssueDao.fetchIssueTransferHdrAndDtl(paIssueHdrId);
	}

	@Override
	public HashMap<BigInteger, String> searchIndentNumber(String searchText, String userCode) {
		return branchSpareTransferIssueDao.searchIndentNumber(searchText, userCode);
	}

	@Override
	public BranchSpareTransferResponse fetchIndentTransferHdrAndDtl(BigInteger paIndHdrId, String userCode,
			String dealerCode, String page, String size) {
		return branchSpareTransferIssueDao.fetchIndentTransferHdrAndDtl(paIndHdrId, userCode, dealerCode, page, size);
	}

	@Override
	public ApiResponse<List<BranchSpareIssueBinStockResponse>> fetchAvailableStock(BigInteger partBranchId, BigInteger partId,
			BigInteger branchId, BigInteger dealerId, BigInteger stockBinId, String userCode) {
		return branchSpareTransferIssueDao.fetchAvailableStock(partBranchId,partId, branchId, dealerId, stockBinId, userCode);
	}

	@Override
	public BranchSpareTransferResponse fetchIssueTransferHdrAndDtl(BigInteger paIndHdrId, String userCode,
			String dealerCode, String page, String size) {
		
			BranchSpareTransferResponse branchSpareTransferResponse = new BranchSpareTransferResponse();
			branchSpareTransferResponse.setBranchSpareTransferIndentHdrResponse(branchSpareTransferIssueDao.fetchIssueHeader(userCode, dealerCode, paIndHdrId, null, null, 0, 10).get(0));
			branchSpareTransferResponse.setIndentNumberDetails(branchSpareTransferIssueDao.fetchIssueTransferDtl(paIndHdrId, userCode, dealerCode));

			return branchSpareTransferResponse;
		}

	

}
