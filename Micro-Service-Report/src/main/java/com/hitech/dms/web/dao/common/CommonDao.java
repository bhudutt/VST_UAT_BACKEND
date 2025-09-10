package com.hitech.dms.web.dao.common;

import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoStateDTLResponseModel;
import com.hitech.dms.web.model.models.response.ModelByPcIdResponseModel;
import com.hitech.dms.web.model.productModels.ModelsForSeriesSegmentResponseModel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

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
	/**
	 * @param jasperPrint
	 * @param format
	 * @param printStatus
	 * @param outputStream
	 * @throws JRException 
	 */
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream) throws JRException;

}
