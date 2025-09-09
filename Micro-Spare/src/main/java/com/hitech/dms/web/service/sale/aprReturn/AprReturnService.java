package com.hitech.dms.web.service.sale.aprReturn;

import java.math.BigInteger;
import java.util.List;

import javax.validation.Valid;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.report.model.InvoicePartNoRequest;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.AprReturnCancelRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.AprReturnSearchRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.CreateAprReturnRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprAppointmentSearchList;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprReturnNumber;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprReturnSearchList;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.InvoicePartDetailResponse;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.PartyInvoiceList;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.SpareAprRetunCreateResponse;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.ViewAprReturnResponse;

public interface AprReturnService {

	List<PartyInvoiceList> fetchPartyInvoiceList(Integer partyBranchId, String userCode);

	List<InvoicePartDetailResponse> getInvoicePartDetail(Integer saleInvoiceId, String userCode);

	SpareAprRetunCreateResponse createAprReturn(String authorizationHeader, String userCode,
			@Valid CreateAprReturnRequest requestModel, Device device);

	List<AprReturnNumber> fetchAprReturnNoList(String userCode, InvoicePartNoRequest requestModel);

	List<PartyInvoiceList> fetchInvoiceNumList(String userCode, InvoicePartNoRequest requestModel);

	AprReturnSearchList search(String userCode, AprReturnSearchRequest resquest, Device device);

	ViewAprReturnResponse viewAprReturnDetail(BigInteger aprReturnId, String userCode);

	List<PartyCategoryResponse> aprPartyCodeList(String searchText, String userCode);

	AprAppointmentSearchList aprAppointmentSearch(String userCode, AprReturnSearchRequest resquest, Device device);

	SpareAprRetunCreateResponse cancelAprReturn(String userCode, AprReturnCancelRequest request, Device device);

	AprAppointmentSearchList aprMappingReportSearch(String userCode, AprReturnSearchRequest resquest, Device device);

}
