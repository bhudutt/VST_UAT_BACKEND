package com.hitech.dms.web.dao.common;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoStateDTLResponseModel;
import com.hitech.dms.web.model.models.response.ModelByPcIdResponseModel;
import com.hitech.dms.web.model.productModels.ModelsForSeriesSegmentResponseModel;

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
	
	public String getDocumentNumber(String prefix, Integer suffix, Session session);
	public String getDocumentNumberById(String prefix, BigInteger suffix, Session session);
	public void updateDocumentNumber(String lastDocumentNumber, String documentPrefix, String branchId,
			Session session);
	public void updateDocumentNumber(String documentType, BigInteger branchID,
			String billingNo, Session session);
}
