package com.hitech.dms.web.service.spare.creditDebit;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.spara.creditDebit.note.request.CreateCrDrNoteRequest;
import com.hitech.dms.web.model.spara.creditDebit.note.request.FilterCreditDebitNoteReq;
import com.hitech.dms.web.model.spara.creditDebit.note.response.CreditDebitNoteReponse;
import com.hitech.dms.web.model.spara.creditDebit.note.response.FilterCreditDebitNoteRep;
import com.hitech.dms.web.model.spara.creditDebit.note.response.SaveResponse;
import com.hitech.dms.web.model.spara.creditDebit.note.response.ViewResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.PayVoucherResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentVoucherList;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;

public interface CreditDebitNoteService {

	List<PaymentVoucherList> getCreditAndDebitType(String lookupTypeCode);

	SaveResponse save(String authorizationHeader, String userCode, @Valid CreateCrDrNoteRequest requestModel,
			Device device)throws ParseException;

	List<PartyCategoryResponse> partyTypeList(Integer dealerTypeId);

	List<SaveResponse> searchCreditDebitNumber(String searchText, String userCode);

	FilterCreditDebitNoteRep filter(String authorizationHeader, String userCode,
			@Valid FilterCreditDebitNoteReq requestModel, Device device);

	ViewResponse viewCreditDebitDetail(BigInteger creditDebitNoteId, String userCode);

}
