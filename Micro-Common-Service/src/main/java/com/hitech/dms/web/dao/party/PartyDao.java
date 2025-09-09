package com.hitech.dms.web.dao.party;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.party.request.FinancePartyByBranchRequestModel;
import com.hitech.dms.web.model.party.request.FinancePatyByPartyCodeRequestModel;
import com.hitech.dms.web.model.party.request.PartyDTLRequestModel;
import com.hitech.dms.web.model.party.request.PartyForInvoiceRequestModel;
import com.hitech.dms.web.model.party.request.PartyListRequestModel;
import com.hitech.dms.web.model.party.response.FinancePartyByBranchResponseModel;
import com.hitech.dms.web.model.party.response.FinancePatyByPartyCodeResponseModel;
import com.hitech.dms.web.model.party.response.PartyDTLResponseModel;
import com.hitech.dms.web.model.party.response.PartyListResponseModel;

public interface PartyDao {
	public List<FinancePartyByBranchResponseModel> fetchFinancePatyList(String userCode, BigInteger branchID,
			String code);

	public List<FinancePartyByBranchResponseModel> fetchFinancePatyList(String userCode,
			FinancePartyByBranchRequestModel financePartyByBranchRequestModel);

	public List<FinancePartyByBranchResponseModel> fetchPatyList(String userCode,
			FinancePartyByBranchRequestModel financePartyByBranchRequestModel);

	public List<PartyListResponseModel> fetchPatyList(String userCode, PartyListRequestModel requestModel);

	public PartyDTLResponseModel fetchPatyDTL(String userCode, PartyDTLRequestModel requestModel);

	public FinancePatyByPartyCodeResponseModel fetchFinancePatyDTL(String userCode,
			FinancePatyByPartyCodeRequestModel requestModel);

	public List<PartyListResponseModel> fetchPatyListForInvoice(String userCode,
			PartyForInvoiceRequestModel requestModel);
}
