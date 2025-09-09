package com.hitech.dms.web.dao.spare.sale.aprReturn;

import java.math.BigInteger;
import java.util.List;

import javax.validation.Valid;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.report.model.InvoicePartNoRequest;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.AprReturnCancelRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.AprReturnDtl;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.AprReturnSearchRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.CreateAprReturnRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprAppointmentSearchList;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprReturnNumber;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprReturnSearchList;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.PartyInvoiceList;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.SpareAprRetunCreateResponse;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.ViewAprReturnDtlResponse;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.ViewAprReturnResponse;

public interface AprReturnDao {

	List<PartyInvoiceList> fetchPartyInvoiceList(Integer partyBranchId, String userCode);

	List<?> getInvoicePartDetail(Integer saleInvoiceId, String userCode);

	SpareAprRetunCreateResponse createAprReturn(String userCode, @Valid CreateAprReturnRequest requestModel);

	List<AprReturnNumber> fetchAprReturnNoList(String userCode, InvoicePartNoRequest requestModel);

	List<PartyInvoiceList> fetchInvoiceNumList(String userCode, InvoicePartNoRequest requestModel);

	AprReturnSearchList search(String userCode, AprReturnSearchRequest resquest, Device device);

	

	ViewAprReturnResponse viewAprReturnHeader(BigInteger aprReturnId, String userCode);

	List<ViewAprReturnDtlResponse> viewAprReturnDetail(BigInteger aprReturnId, String userCode);

	Integer getQtyAndReturnQtyForApr(List<AprReturnDtl> sparePartDetails, String userCode);

	List<PartyCategoryResponse> aprPartyCodeList(String searchText, String userCode);

	AprAppointmentSearchList aprAppointmentSearch(String userCode, AprReturnSearchRequest resquest, Device device);

	Integer cancelAprReturn(String userCode, AprReturnCancelRequest request, Device device);

	Integer cancelAprReturnStatus(String userCode, AprReturnCancelRequest request, Device device);
	
	AprAppointmentSearchList aprMappingReportSearch(String userCode, AprReturnSearchRequest resquest, Device device);

}
