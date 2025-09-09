package com.hitech.dms.web.service.spare.paymentVoucher;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.validation.Valid;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.spara.creditDebit.note.response.FilterCreditDebitNoteRep;
import com.hitech.dms.web.model.spara.creditDebit.note.response.SaveResponse;
import com.hitech.dms.web.model.spara.customer.order.response.PartyCodeListResponseModel;
import com.hitech.dms.web.model.spara.payment.voucher.request.CreatePayVoucherRequest;
import com.hitech.dms.web.model.spara.payment.voucher.request.FilterPaymentVoucherReq;
import com.hitech.dms.web.model.spara.payment.voucher.request.PVpartyCodeRequestModel;
import com.hitech.dms.web.model.spara.payment.voucher.request.PartyTypeReqest;
import com.hitech.dms.web.model.spara.payment.voucher.request.RefDocForGrnInvReq;
import com.hitech.dms.web.model.spara.payment.voucher.response.GrnInvReferenceDocList;
import com.hitech.dms.web.model.spara.payment.voucher.response.PayVoucherResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentBankList;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentVoucherList;
import com.hitech.dms.web.model.spara.payment.voucher.response.SearchPaymentVoucherResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.viewPayVoucherResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoDealerAndDistributerSearchResponse;
import com.hitech.dms.web.model.spare.create.resquest.SparePoDealerAndDistributorRequest;
import com.hitech.dms.web.model.spare.party.mapping.response.DistributorDetailResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;

public interface PaymentVoucherService {

	PayVoucherResponse savePayment(String authorizationHeader, String userCode,
			@Valid CreatePayVoucherRequest requestModel, Device device);

	List<PaymentVoucherList> fetchPaymentReceiptList(String lookupTypeCode);

	List<PaymentBankList> getBankCategory(String bankCode, String userCode);

	List<PartyCategoryResponse> searchPartyTypeCategory(PartyTypeReqest requestModel, String userCode);


	PartyCodeListResponseModel searchPartyCodeList(String userCode, PVpartyCodeRequestModel requestModel);

	GrnInvReferenceDocList getRefDocDetail(String userCode, RefDocForGrnInvReq requestModel);

	BigDecimal getPartyWiseAmt(String partyCode, String pvTypeCode);

	SearchPaymentVoucherResponse filter(String authorizationHeader, String userCode,
			@Valid FilterPaymentVoucherReq requestModel, Device device);

	List<SaveResponse> searchPaymentVoucherNumber(String searchText, String userCode);

	viewPayVoucherResponse viewPaymentVoucherDetail(BigInteger paymentVoucherId, String userCode);

	List<PartyCategoryResponse> partyCodeList(String searchText, String userCode);

	List<SparePoDealerAndDistributerSearchResponse> getPartyNameList(String userCode,
			SparePoDealerAndDistributorRequest sparePoDealerAndDistributorRequest);

	DistributorDetailResponse getPartyNameDetails(Integer distributorId);

	DistributorDetailResponse getPartyWisePVDetails(Integer distributorId, String flag);

	List<PartyCategoryResponse> getPvCategory(String userCode);


}
