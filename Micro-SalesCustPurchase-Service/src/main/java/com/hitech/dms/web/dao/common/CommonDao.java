package com.hitech.dms.web.dao.common;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.hitech.dms.web.model.dc.common.response.CancelReasonResponseModel;
import com.hitech.dms.web.model.dc.common.response.DcTypeResponModel;
import com.hitech.dms.web.model.inv.common.response.InvoiceTypeResponseModel;

public interface CommonDao {
	public Map<String, Object> updateEnquiryStatus(Session session, String userCode, BigInteger userId, Date currDate,
			BigInteger enquiryHdrId, String status);
	
	public Map<String, Object> updateAllotmentStatus(Session session, String userCode, BigInteger userId, Date currDate,
			BigInteger allotmentHdrId, String status);

	public Map<String, Object> fetchHOUserDTLByUserCode(Session session, String userCode);

	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode);

	public List<?> fetchApprovalData(Session session, String approvalCode);
	
	public List<DcTypeResponModel> fetchDcTypeList(String userCode);
	
	public List<InvoiceTypeResponseModel> fetchInvoiceTypeList(String userCode);
	
	public InvoiceTypeResponseModel fetchInvoiceTypeDtl(Session session, String userCode, Integer invoiceTypeId);
	
	public List<CancelReasonResponseModel> fetchCancelReasonList(String userCode, String code);
	
	public String getDocumentNumber(String prefix, Integer suffix, Session session);
	
	public String getDocumentNumberById(String prefix, BigInteger suffix, Session session);
	
	public void updateDocumentNumber(String lastDocumentNumber, String documentPrefix, String branchId, Session session);
	
	public void updateDocumentNumber(String documentType, BigInteger branchID, String pcrNo, Session session);
}
