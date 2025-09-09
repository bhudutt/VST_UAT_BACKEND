package com.hitech.dms.web.service.common;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import com.hitech.dms.web.model.common.PartyLedgerRequest;
import com.hitech.dms.web.model.common.PinCodeDetails;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.sale.invoice.SpareInvoicePriceRequest;
import com.hitech.dms.web.model.spare.sale.invoice.SpareInvoicePriceResponse;

public interface CommonService {

	public List<PartNumberDetails> fetchPartNumberDetails(Integer partId, String userCode, Integer branchId, Integer poHdrId,
			Integer coId, Integer dcId, Integer pickListId, Integer refDocId, String flag);

	public PinCodeDetails fetchPinCodeDetails(Integer pinId);

	public HashMap<BigInteger, String> searchPinCode(String searchText);

	public String saveInPartyLedger(PartyLedgerRequest partyLedgerRequest, String userCode);
	
	public SpareInvoicePriceResponse getPartUnitPrice(SpareInvoicePriceRequest req,String userCode);

}
