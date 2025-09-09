package com.hitech.dms.web.service.branchSpareTransfer.indent;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hitech.dms.web.entity.branchTransfer.indent.IndentHdrEntity;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferIndentHdrResponse;
import com.hitech.dms.web.model.spare.branchTransfer.indent.response.BranchSpareTransferResponse;

public interface BranchSpareTransferIndentService {

	public BranchSpareTransferResponse createBranchSpareTransferIndent(IndentHdrEntity indentHdrEntity, String userCode);
	
	public HashMap<BigInteger, String> searchPartNumber(String searchText, String userCode);
	
	public PartNumberDetails fetchPartNumberDetails(Integer partId, String userCode, Integer branchId);

	public HashMap<BigInteger, String> searchIndentNumber(String searchText, String userCode);

	public List<BranchSpareTransferIndentHdrResponse> fetchIndentDetails(String indentNumber,
			BigInteger paIndHdrId, Date fromDate, Date toDate, Integer page, Integer size,String userCode);

	public BranchSpareTransferResponse fetchIndentTransferHdrAndDtl(BigInteger paIndHdrId, String userCode, String dealerCode);

}
