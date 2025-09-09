package com.hitech.dms.web.service.common;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.common.PartyLedgerEntity;
import com.hitech.dms.web.model.common.PartyLedgerRequest;
import com.hitech.dms.web.model.common.PinCodeDetails;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.sale.invoice.SpareInvoicePriceRequest;
import com.hitech.dms.web.model.spare.sale.invoice.SpareInvoicePriceResponse;

@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	CommonDao commonDao;

	public List<PartNumberDetails> fetchPartNumberDetails(Integer partId, String userCode, Integer branchId,
			Integer poHdrId, Integer coId, Integer dcId, Integer pickListId, Integer refDocId, String flag) {
		return commonDao.fetchPartNumberDetails(partId, userCode, branchId, poHdrId, coId, dcId, pickListId, refDocId, flag);
	}

	@Override
	public PinCodeDetails fetchPinCodeDetails(Integer pinId) {
		return commonDao.fetchPinCodeDetails(pinId);
	}

	@Override
	public HashMap<BigInteger, String> searchPinCode(String searchText) {
		return commonDao.searchPinCode(searchText);
	}

	@Override
	public String saveInPartyLedger(PartyLedgerRequest partyLedgerRequest, String userCode) {
		if(partyLedgerRequest != null) {
			Date todayDate = new Date();
			
			PartyLedgerEntity partyLedgerEntity = new PartyLedgerEntity();
			partyLedgerEntity.setPartyId(partyLedgerRequest.getPartyId());
			partyLedgerEntity.setTransactionName(partyLedgerRequest.getTransactionName());
			partyLedgerEntity.setTransactionId(partyLedgerRequest.getTransactionId());
			partyLedgerEntity.setTransactionDate(partyLedgerRequest.getTransactionDate());
			partyLedgerEntity.setTransactionAmount(partyLedgerRequest.getTransactionAmount());
			partyLedgerEntity.setTransactionType(partyLedgerRequest.getTransactionType());
			partyLedgerEntity.setCreatedDate(todayDate);
			partyLedgerEntity.setCreatedBy(userCode);
			return commonDao.saveInPartyLedger(partyLedgerEntity, userCode);
		} else {
			return "Request is empty";
		}
	}

	@Override
	public SpareInvoicePriceResponse getPartUnitPrice(SpareInvoicePriceRequest req, String userCode) {
		return commonDao.getPartsUnitPrice(req, userCode);
	}
}
