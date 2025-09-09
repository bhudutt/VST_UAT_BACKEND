package com.hitech.dms.web.dao.oldchassis;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.oldchassis.OldChassisCreateRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisCreateResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisCustomerDTLByCustIDRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisCustomerDTLByCustIDResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisCustomerDTLByMobileRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisModelListResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisNumberRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisNumberResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisSegmentListResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisSeriesListResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisVariantListResponseModel;
import com.hitech.dms.web.model.oldchassis.OldchassisItemDescriptionResponseModel;
import com.hitech.dms.web.model.oldchassis.PoPlantRequstModel;
import com.hitech.dms.web.model.oldchassis.PoPlantResponseModel;
import com.hitech.dms.web.model.oldchassis.list.response.OldchassisListResponseModel;
import com.hitech.dms.web.model.oldchassis.reponse.OldChassisEngVinRegDTLResponseModel;
import com.hitech.dms.web.model.oldchassis.reponse.OldChassisMachineItemIdDetailModel;
import com.hitech.dms.web.model.oldchassis.request.OldChassisEngVinRegDTLRequestModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingModelDTLResponseModel;

import reactor.core.publisher.Mono;

import com.hitech.dms.web.model.oldchassis.OldChassisCustomerDTLByMobileResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisFetchDTLRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisFetchDTLResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisItemDescriptionRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisModelItemListResponseModel;

public interface OldChassisCreateDao {
	
	
	public OldChassisCreateResponseModel createOldChassis(String userCode,
			OldChassisCreateRequestModel requestModel, Device device);
	
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode);
	
	public Map<String, Object> fetchcustByCustomerId(Session session, String mobileNo);
	
	public Map<String, Object> fetchcustDTLByCustomerId(Session session, String mobileNo);
	
	public List<OldChassisSeriesListResponseModel> fetchSeriesList(String userCode, Device device, Integer pcId);
	
	public List<OldChassisSegmentListResponseModel> fetchSegmentList(String userCode,Device device, BigInteger modelId);
	
	public List<OldChassisModelListResponseModel> fetchMachineItemByList(String userCode, Device device, Integer machineItemId);
	
	public List<OldChassisVariantListResponseModel> fetchVariantList(String userCode, Device device, BigInteger modelId);
	
	public List<OldChassisCustomerDTLByMobileResponseModel> fetchCustomerDTLByMobileNo(String userCode, String mobileNumber,
			String isFor, Long dealerID);
	
	public List<OldChassisCustomerDTLByMobileResponseModel> fetchCustomerDTLByMobileNo(
			OldChassisCustomerDTLByMobileRequestModel customerDTLByMobileRequestModel);

	public List<OldChassisCustomerDTLByCustIDResponseModel> fetchCustomerDTLByCustID(String userCode, Long custID);

	public List<OldChassisCustomerDTLByCustIDResponseModel> fetchCustomerDTLByCustID(String userCode,
			OldChassisCustomerDTLByCustIDRequestModel customerDTLByCustIDRequestModel);
	
	public List<OldchassisItemDescriptionResponseModel> fetchOldChassisItemDTLList(String userCode,
			OldChassisItemDescriptionRequestModel ssRequestModel);
	
	public List<OldChassisNumberResponseModel> fetchOldChassisDTLList(String userCode,
			OldChassisNumberRequestModel ssRequestModel);
	
	public List<OldChassisFetchDTLResponseModel> fetchOldChassisMultiDTLList(String userCode,
			OldChassisFetchDTLRequestModel ssRequestModel);
	
	public List<OldchassisListResponseModel> fetchOldchassisList(String userCode);

	public List<OldChassisEngVinRegDTLResponseModel> fetchOldChassisEnginVinRegistrationDTLList(String userCode,
			OldChassisEngVinRegDTLRequestModel ssRequestModel);

	public List<ServiceBookingModelDTLResponseModel> fetchModelAllList(String userCode, Device device);

	public List<OldChassisMachineItemIdDetailModel> fetchModelDetailsAllList(String userCode, Device device,
			BigInteger machineItemId);

	public Mono<Map<String, Object>> oldChassisMail(String userCode, String string, BigInteger customerId, String status);

	public List<PoPlantResponseModel> fetchPOPlantList(String userCode, PoPlantRequstModel requestModel);


}
