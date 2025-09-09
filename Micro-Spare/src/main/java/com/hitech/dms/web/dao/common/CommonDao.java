package com.hitech.dms.web.dao.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.hitech.dms.web.entity.common.PartyLedgerEntity;
import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;
import com.hitech.dms.web.model.common.PinCodeDetails;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoStateDTLResponseModel;
import com.hitech.dms.web.model.models.response.ModelByPcIdResponseModel;
import com.hitech.dms.web.model.productModels.ModelsForSeriesSegmentResponseModel;
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;
import com.hitech.dms.web.model.spare.sale.invoice.SpareInvoicePriceRequest;
import com.hitech.dms.web.model.spare.sale.invoice.SpareInvoicePriceResponse;
import com.hitech.dms.web.model.spare.sale.invoice.request.SpareSalesInvoiceRequest;

public interface CommonDao {

	public GeoStateDTLResponseModel fetchStateDtlByStateID(String authorizationHeader, BigInteger stateId);

	public DealerDTLResponseModel fetchDealerDTLByDealerId(String authorizationHeader, BigInteger dealerId,
			String isFor);

	public BranchDTLResponseModel fetchBranchDtlByBranchId(String authorizationHeader, BigInteger branchId);

	public Map<String, Object> fetchHOUserDTLByUserCode(Session session, String userCode);

	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode);

	public List<?> fetchApprovalData(Session session, String approvalCode);

	public List<ModelByPcIdResponseModel> fetchModelListByPcId(String authorizationHeader, Integer pcId, String isFor);
	
	public List<ModelsForSeriesSegmentResponseModel> fetchModelsForSeriesSegment(String authorizationHeader,
			Integer pcId, String seriesName, String segment);

	public List<PartNumberDetails> fetchPartNumberDetails(Integer partId, String userCode, Integer branchId, Integer poHdrId, 
			Integer coId, Integer dcId, Integer pickListId, Integer refDocId, String flag);


	public String getDocumentNumber(String prefix, Integer suffix, Session session);
	
	public String getDocumentNumberById(String prefix, BigInteger suffix, Session session);
	
	public void updateDocumentNumber(String lastDocumentNumber, String documentPrefix, String branchId, Session session);
	
	public void updateDocumentNumber(String documentType, BigInteger branchID, String pcrNo, Session session);

	public PartDetailRequest updateStockForInPartBranchAndStockBin(Session session, String flag, PartDetailRequest partDetailRequest,
			BigInteger branchId, String tableName, String userCode);

	public PinCodeDetails fetchPinCodeDetails(Integer pinId);
	
	public SpareInvoicePriceResponse getPartsUnitPrice(SpareInvoicePriceRequest req,String userCode);

	public HashMap<BigInteger, String> searchPinCode(String searchText);

	public String saveInPartyLedger(PartyLedgerEntity partyLedgerEntity, String userCode);
	
	public BigInteger updateStockInStockBin(Session session, BranchSpareIssueBinStockResponse binRequest,
			BigInteger branchId, Integer branchStoreId, BigDecimal InvoiceQty, String tableName, String userCode,
			String flag);
	
	public PartDetailRequest updateStockInPartBranchAndStockStore(Session session, String flag,
			PartDetailRequest partDetailRequest, BigInteger branchId, Integer branchStoreId, String tableName,
			String userCode);

	public List<BigDecimal> fetchMRPList(Integer partId, String userCode);

	
}
