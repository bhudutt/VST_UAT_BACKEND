package com.hitech.dms.web.service.branchSpareTransfer.indent;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.dao.spare.branchTransfer.indent.BranchSpareTransferIndentDao;
import com.hitech.dms.web.entity.branchTransfer.indent.IndentHdrEntity;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferIndentHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferResponse;

@Service
public class BranchSpareTransferIndentServiceImpl implements BranchSpareTransferIndentService {

	@Autowired
	BranchSpareTransferIndentDao branchSpareTransferIndentDao;

	public BranchSpareTransferResponse createBranchSpareTransferIndent(IndentHdrEntity indentHdrEntity,
			String userCode) {
		return branchSpareTransferIndentDao.createBranchSpareTransferIndent(indentHdrEntity, userCode);
	}

	public HashMap<BigInteger, String> searchPartNumber(String searchText, String userCode) {
		return branchSpareTransferIndentDao.searchPartNumber(searchText, userCode);
	}

	public PartNumberDetails fetchPartNumberDetails(Integer partId, String userCode, Integer branchId) {
		return branchSpareTransferIndentDao.fetchPartNumberDetails(partId, userCode, branchId);
	}

	public HashMap<BigInteger, String> searchIndentNumber(String searchText, String userCode) {
		return branchSpareTransferIndentDao.searchIndentNumber(searchText, userCode);
	}

	public List<BranchSpareTransferIndentHdrResponse> fetchIndentDetails(String indentNumber, BigInteger paIndHdrId,
			Date fromDate, Date toDate, Integer page, Integer size, String userCode) {
		return branchSpareTransferIndentDao.fetchIndentDetails(indentNumber, paIndHdrId, fromDate, toDate, page, size,userCode);
	}

	public BranchSpareTransferResponse fetchIndentTransferHdrAndDtl(BigInteger paIndHdrId, String userCode,
			String dealerCode) {
		return branchSpareTransferIndentDao.fetchIndentTransferHdrAndDtl(paIndHdrId, userCode, dealerCode);
	}

}
