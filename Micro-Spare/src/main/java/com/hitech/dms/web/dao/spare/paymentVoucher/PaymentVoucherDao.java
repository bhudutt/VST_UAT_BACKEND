package com.hitech.dms.web.dao.spare.paymentVoucher;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.validation.Valid;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.spara.creditDebit.note.response.SaveResponse;
import com.hitech.dms.web.model.spara.customer.order.response.PartyCodeListResponseModel;
import com.hitech.dms.web.model.spara.payment.voucher.request.CreatePayVoucherRequest;
import com.hitech.dms.web.model.spara.payment.voucher.request.FilterPaymentVoucherReq;
import com.hitech.dms.web.model.spara.payment.voucher.request.PVpartyCodeRequestModel;
import com.hitech.dms.web.model.spara.payment.voucher.request.PartyTypeReqest;
import com.hitech.dms.web.model.spara.payment.voucher.request.RefDocForGrnInvReq;
import com.hitech.dms.web.model.spara.payment.voucher.response.PVGrnAndInvoiceResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.PayVoucherResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentBankList;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentVoucherList;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentVoucherReponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.SearchPaymentVoucherResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoDealerAndDistributerSearchResponse;
import com.hitech.dms.web.model.spare.create.resquest.SparePoDealerAndDistributorRequest;
import com.hitech.dms.web.model.spare.party.mapping.response.DistributorDetailResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;

public interface PaymentVoucherDao {

	PayVoucherResponse savePayment(String authorizationHeader, String userCode,
			@Valid CreatePayVoucherRequest requestModel, Device device);

	List<PaymentVoucherList> fetchPaymentReceiptList(String lookupTypeCode);

	List<PaymentBankList> getBankCategory(String bankCode, String userCode);

	List<PartyCategoryResponse> searchPartyTypeCategory();

	PartyCodeListResponseModel searchPVpartyCodeList(String userCode, PVpartyCodeRequestModel requestModel);

	List<PVGrnAndInvoiceResponse> getRefDocDetail(String userCode, RefDocForGrnInvReq requestModel);

	BigDecimal getPartyWiseAmt(String partyCode, String pvTypeCode);

	List<?> filter(FilterPaymentVoucherReq requestModel, String userCode);

	List<SaveResponse> searchPaymentVoucherNumber(String searchText, String userCode);

	List<?> viewPaymentVoucherDetail(BigInteger paymentVoucherId, String userCode, Integer flag);

	List<PartyCategoryResponse> partyCodeList(String searchText, String userCode);

	List<SparePoDealerAndDistributerSearchResponse> getPartyNameList(String userCode,
			SparePoDealerAndDistributorRequest sparePoDealerAndDistributorRequest);

	DistributorDetailResponse getPartyNameDetails(Integer distributorId);

	DistributorDetailResponse getPartyWisePVDetails(Integer distributorId, String flag);

	List<PartyCategoryResponse> getPvCategory(String userCode);

}
