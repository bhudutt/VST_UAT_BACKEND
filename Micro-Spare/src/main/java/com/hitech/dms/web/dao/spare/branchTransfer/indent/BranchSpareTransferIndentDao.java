package com.hitech.dms.web.dao.spare.branchTransfer.indent;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hitech.dms.web.entity.branchTransfer.indent.IndentHdrEntity;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferIndentHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferResponse;

public interface BranchSpareTransferIndentDao {

	HashMap<BigInteger, String> searchPartNumber(String searchText, String userCode);

	PartNumberDetails fetchPartNumberDetails(Integer partId, String userCode, Integer branchId);

	BranchSpareTransferResponse createBranchSpareTransferIndent(IndentHdrEntity indentHdrEntity, String userCode);

	HashMap<BigInteger, String> searchIndentNumber(String searchText, String userCode);

	List<BranchSpareTransferIndentHdrResponse> fetchIndentDetails(String indentNumber, BigInteger paIndHdrId,
			Date fromDate, Date toDate, Integer page, Integer size, String userCode);

	BranchSpareTransferResponse fetchIndentTransferHdrAndDtl(BigInteger paIndHdrId, String userCode, String dealerCode);

}
