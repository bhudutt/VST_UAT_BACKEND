package com.hitech.dms.web.dao.spare.creditDebit;

import java.math.BigInteger;
import java.util.List;

import javax.validation.Valid;

import com.hitech.dms.web.entity.spare.creditDebit.note.CreditDebitNoteEntity;
import com.hitech.dms.web.model.spara.creditDebit.note.request.FilterCreditDebitNoteReq;
import com.hitech.dms.web.model.spara.creditDebit.note.response.CreditDebitNoteReponse;
import com.hitech.dms.web.model.spara.creditDebit.note.response.FilterCreditDebitNoteRep;
import com.hitech.dms.web.model.spara.creditDebit.note.response.SaveResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentVoucherList;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;

public interface CreditDebitNoteDao {

	List<PaymentVoucherList> getCreditAndDebitType(String lookupTypeCode);

	BigInteger save(CreditDebitNoteEntity entity, String userCode);

	List<PartyCategoryResponse> partyTypeList();

	List<SaveResponse> searchCreditDebitNumber(String searchText, String userCode);

	List<?> filter(@Valid FilterCreditDebitNoteReq requestModel, String userCode);

	List<?> viewCreditDebitDetail(BigInteger creditDebitNoteId, String userCode);

}
