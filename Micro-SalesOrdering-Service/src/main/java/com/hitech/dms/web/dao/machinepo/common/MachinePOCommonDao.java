package com.hitech.dms.web.dao.machinepo.common;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.hitech.dms.web.entity.machinepo.MachinePOStatusEntity;
import com.hitech.dms.web.model.grn.type.response.GrnTypeResponseModel;
import com.hitech.dms.web.model.machinepo.orderTo.request.MachinePOOrderToRequestModel;
import com.hitech.dms.web.model.machinepo.orderTo.response.MachinePOOrderToResponseModel;
import com.hitech.dms.web.model.machinepo.plant.response.PoPlantRequstModel;
import com.hitech.dms.web.model.machinepo.plant.response.PoPlantResponseModel;

public interface MachinePOCommonDao {
	public List<MachinePOOrderToResponseModel> fetchPOTOTypeList(String userCode,
			MachinePOOrderToRequestModel requestModel);

	public MachinePOOrderToResponseModel fetchPOTOTypeDTL(String userCode, Integer poOrderToId);

	public MachinePOOrderToResponseModel fetchPOTOTypeDTL(Session session, String userCode,Integer poOrderToId);
	
	public MachinePOStatusEntity fetchMachinePOStatusDTL(String userCode, Integer poStatusId);
	
	public List<MachinePOStatusEntity> fetchMachinePOStatusList(Session session, String userCode);

	public List<PoPlantResponseModel> fetchPOPlantList(String userCode, PoPlantRequstModel requestModel);

	public Map<String, Object> fetchHOUserDTLByUserCode(Session session, String userCode);

	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode);
	
	public List<?> fetchApprovalData(Session session, String approvalCode);
	
	public GrnTypeResponseModel fetchGrnTypeDtl(Integer grnTypeId);
	
	public Integer fetchB2CFlag(String invoiceNo);
}
