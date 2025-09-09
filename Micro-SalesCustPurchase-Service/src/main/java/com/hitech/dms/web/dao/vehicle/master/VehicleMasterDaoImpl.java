package com.hitech.dms.web.dao.vehicle.master;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import com.hitech.dms.web.model.vehicle.master.VehicleMasterDetailsModel;
import com.hitech.dms.web.model.vehicle.master.VehicleMasterRequestModel;
import com.hitech.dms.web.model.vehicle.master.VehicleMasterSearchListResponseModel;
import com.hitech.dms.web.model.vehicle.master.VehicleMasterSearchListResultResponseModel;

@Repository
public class VehicleMasterDaoImpl implements VehicleMasterDao {

	private static final Logger logger = LoggerFactory.getLogger(VehicleMasterDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	
	@Override
	public VehicleMasterDetailsModel vehicleMasterDTL(String userCode, VehicleMasterRequestModel requestModel) {

	    Session session = null;
	    VehicleMasterDetailsModel response = null;
	    Query query = null;

	    try {
	        session = sessionFactory.openSession();
	        String sqlQuery = "exec [SP_Vehicle_MasterDTL] :ChassisNo";
	        query = session.createNativeQuery(sqlQuery)
	            .addScalar("ChassisNo", StandardBasicTypes.STRING)
	            .addScalar("EngineNo", StandardBasicTypes.STRING)
	            .addScalar("vin_no", StandardBasicTypes.STRING)
	            .addScalar("MfgInvoiceNumber", StandardBasicTypes.STRING)
	            .addScalar("MfgInvoiceDate", StandardBasicTypes.DATE)
	            .addScalar("ProfitCenter", StandardBasicTypes.STRING)
	            .addScalar("Model", StandardBasicTypes.STRING)
	            .addScalar("series_name", StandardBasicTypes.STRING)
	            .addScalar("segment_name", StandardBasicTypes.STRING)
	            .addScalar("variant", StandardBasicTypes.STRING)
	            .addScalar("ItemNumber", StandardBasicTypes.STRING)
	            .addScalar("ItemDescription", StandardBasicTypes.STRING)
	            .addScalar("IN PDI NO.", StandardBasicTypes.STRING)
	            .addScalar("grn_date", StandardBasicTypes.DATE)
	            .addScalar("IN PDI DATE", StandardBasicTypes.DATE)
	            .addScalar("installationnumber", StandardBasicTypes.STRING)
	            .addScalar("OUT PDI NO.", StandardBasicTypes.STRING)
	            .addScalar("DealerName", StandardBasicTypes.STRING)
	            .addScalar("InstallationDate", StandardBasicTypes.STRING)
	            .addScalar("Sale_Date", StandardBasicTypes.DATE)
	            .addScalar("OperatorManualNumber", StandardBasicTypes.STRING)
	            .addScalar("LASTKMREPORTED", StandardBasicTypes.STRING)
	            .addScalar("registration_number", StandardBasicTypes.STRING)
	            .addScalar("LAST_HR_REPORTED", StandardBasicTypes.STRING)
	            .addScalar("PreviousServiceDate", StandardBasicTypes.DATE)
	            .addScalar("NEXTDUESERVICE", StandardBasicTypes.STRING)
	            .addScalar("CustomerCode", StandardBasicTypes.STRING)
	            .addScalar("custname", StandardBasicTypes.STRING)
	            .addScalar("custmobile", StandardBasicTypes.STRING)
	            .addScalar("ProspectType", StandardBasicTypes.STRING)
	            .addScalar("address1", StandardBasicTypes.STRING)
	            .addScalar("WhatsappNo", StandardBasicTypes.STRING)
	            .addScalar("MobileNo", StandardBasicTypes.STRING)
	            .addScalar("address2", StandardBasicTypes.STRING)
	            .addScalar("CustomerDistrict", StandardBasicTypes.STRING)
	            .addScalar("email_id", StandardBasicTypes.STRING)
	            .addScalar("address3", StandardBasicTypes.STRING)
	            .addScalar("Tehsil", StandardBasicTypes.STRING)
	            .addScalar("City", StandardBasicTypes.STRING)
	            .addScalar("PinCode", StandardBasicTypes.STRING)
	            .addScalar("StateDesc", StandardBasicTypes.STRING)
	            .addScalar("BatteryMakeNumber", StandardBasicTypes.STRING)
	            .addScalar("Fipmakenumber", StandardBasicTypes.STRING)
	            .addScalar("startermotormakenumber", StandardBasicTypes.STRING)
	            .addScalar("AlternatorMakeNumber", StandardBasicTypes.STRING)
	            .addScalar("RearTyerMakeRHNumber", StandardBasicTypes.STRING)
	            .addScalar("FrontTyerMakeLHNumber", StandardBasicTypes.STRING)
	            .addScalar("RearTyerMakeLHNumber", StandardBasicTypes.STRING)
	            .addScalar("SWStartDate", StandardBasicTypes.DATE)
	            .addScalar("SWExpiryDate", StandardBasicTypes.DATE)
	            .addScalar("EXStartDate", StandardBasicTypes.STRING)
	            .addScalar("EXEnddate", StandardBasicTypes.STRING)
	            .addScalar("AMC_POLICY", StandardBasicTypes.STRING)
	            .addScalar("AMCPOLICYNO", StandardBasicTypes.STRING)
	            .addScalar("AMCSTATUS", StandardBasicTypes.STRING)
	            .addScalar("AMCExpiryDate", StandardBasicTypes.STRING)
	            .addScalar("INSURANCECOMPANY", StandardBasicTypes.STRING)
	            .addScalar("INSURANCEDATE", StandardBasicTypes.STRING)
	            .addScalar("INSURANCECOVERNOTE", StandardBasicTypes.STRING)
	            .addScalar("INSURANCEEXPIRY", StandardBasicTypes.STRING)
	            .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
	        query.setParameter("ChassisNo", requestModel.getChassisNo());

	        List resultList = query.getResultList();
	        if (resultList != null && !resultList.isEmpty()) {
	            Map row = (Map) resultList.get(0); // Just pick first entry
	            response = new VehicleMasterDetailsModel();

	            response.setChassisNo((String) row.get("ChassisNo"));
	            response.setEngineNo((String) row.get("EngineNo"));
	            response.setVinNo((String) row.get("vin_no"));
	            response.setMfgInvoiceNo((String) row.get("MfgInvoiceNumber"));
	            response.setMfgInvoiceDate((Date) row.get("MfgInvoiceDate"));
	            response.setProfitCenter((String) row.get("ProfitCenter"));
	            response.setModel((String) row.get("Model"));
	            response.setSeries((String) row.get("series_name"));
	            response.setSegment((String) row.get("segment_name"));
	            response.setVariant((String) row.get("variant"));
	            response.setItemNo((String) row.get("ItemNumber"));
	            response.setItemDescription((String) row.get("ItemDescription"));
	            response.setInPdiNo((String) row.get("IN PDI NO."));
	            response.setGrnDate((Date) row.get("grn_date"));
	            response.setInPdiDate((Date) row.get("IN PDI DATE"));
	            response.setInstallationNo((String) row.get("installationnumber"));
	            response.setOutPdiNo((String) row.get("OUT PDI NO."));
	            response.setSoldBy((String) row.get("DealerName"));
	            response.setDateOfInstallation((String) row.get("InstallationDate"));
	            response.setSaleDate((Date) row.get("Sale_Date"));
	            response.setOperatorManualNo((String) row.get("OperatorManualNumber"));
	            response.setLastKmReported((String) row.get("LASTKMREPORTED"));
	            response.setRegistrationNo((String) row.get("registration_number"));
	            response.setLastHrReported((String) row.get("LAST_HR_REPORTED"));
	            response.setLastServiceDate((Date) row.get("PreviousServiceDate"));
	            response.setNextDueService((String) row.get("NEXTDUESERVICE"));
	            response.setCustomerCode((String) row.get("CustomerCode"));
	            response.setCustomerName((String) row.get("custname"));
	            response.setMobileNo((String) row.get("custmobile"));
	            response.setProspectCategory((String) row.get("ProspectType"));
	            response.setAddress1((String) row.get("address1"));
	            response.setWhatsappNo((String) row.get("WhatsappNo"));
	            response.setAlternateMobileNo((String) row.get("MobileNo"));
	            response.setAddress2((String) row.get("address2"));
	            response.setDistrict((String) row.get("CustomerDistrict"));
	            response.setEmailId((String) row.get("email_id"));
	            response.setAddress3((String) row.get("address3"));
	            response.setTehsilTalukaMandal((String) row.get("Tehsil"));
	            response.setCityVillage((String) row.get("City"));
	            response.setPincode((String) row.get("PinCode"));
	            response.setState((String) row.get("StateDesc"));
	            response.setBatteryNo((String) row.get("BatteryMakeNumber"));
	            response.setFipNo((String) row.get("Fipmakenumber"));
	            response.setStarterNo((String) row.get("startermotormakenumber"));
	            response.setAlternatorNo((String) row.get("AlternatorMakeNumber"));
	            response.setFrontTyreRhNo((String) row.get("RearTyerMakeRHNumber"));
	            response.setFrontTyreLhNo((String) row.get("FrontTyerMakeLHNumber"));
	            response.setRearTyreRhNo((String) row.get("RearTyerMakeRHNumber"));
	            response.setRearTyreLhNo((String) row.get("RearTyerMakeLHNumber"));
	            response.setStandardWarrantyStartDate((Date) row.get("SWStartDate"));
	            response.setStandardWarrantyEndDate((Date) row.get("SWExpiryDate"));
	            response.setExtendedWarrantyStartDate((String) row.get("EXStartDate"));
	            response.setExtendedWarrantyEndDate((String) row.get("EXEnddate"));
	            response.setAmcPolicy((String) row.get("AMC_POLICY"));
	            response.setAmcPolicyNo((String) row.get("AMCPOLICYNO"));
	            response.setAmcStatus((String) row.get("AMCSTATUS"));
	            response.setAmcExpiry((String) row.get("AMCExpiryDate"));
	            response.setInsuranceCompany((String) row.get("INSURANCECOMPANY"));
	            response.setInsuranceDate((String) row.get("INSURANCEDATE"));
	            response.setInsuranceCoverNoteNo((String) row.get("INSURANCECOVERNOTE"));
	            response.setInsuranceExpiry((String) row.get("INSURANCEEXPIRY"));
	        }
	    } catch (SQLGrammarException sqlge) {
	        sqlge.printStackTrace();
	    } catch (HibernateException exp) {
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	        logger.error(this.getClass().getName(), exp);
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }
	    return response;
	}


	
	@Override
	public VehicleMasterSearchListResultResponseModel VehicleMasterSearchList(String userCode,
			VehicleMasterRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"VehicleMasterSearchList invoked.." + userCode + " " + requestModel.toString());
		}
		Query query = null;
		Session session = null;
		VehicleMasterSearchListResultResponseModel responseListModel = null;
	    List<VehicleMasterSearchListResponseModel> responseModelList = null;
	    Integer recordCount = 0;
		String sqlQuery = "exec [SP_GETVEHICLE_MASTER_SEARCHBY_LIST] :branchId, :chassisNo, :EngnieNo, :registrationNo, :custMobileNo, :CustomerName, :CustomerCode, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("chassisNo", requestModel.getChassisNo());
			query.setParameter("EngnieNo", requestModel.getEngineNo());
			query.setParameter("registrationNo", requestModel.getRegistrationNo());
			query.setParameter("custMobileNo", requestModel.getMobileNo());	
			query.setParameter("CustomerName", requestModel.getCustomerName());
			query.setParameter("CustomerCode", requestModel.getCustomerCode());
			query.setParameter("page", requestModel.getPage());	
			query.setParameter("size", requestModel.getSize());	
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel=new VehicleMasterSearchListResultResponseModel();
				responseModelList = new ArrayList<VehicleMasterSearchListResponseModel>();
				VehicleMasterSearchListResponseModel response=null;
				for (Object object : data) {
					Map row = (Map) object;
					response = new VehicleMasterSearchListResponseModel();
					
					response.setModel((String) row.get("model"));
					response.setVariant((String) row.get("varriant"));
					response.setItemNo((String) row.get("item_no"));
					response.setItemDescription((String) row.get("item_description"));
					response.setChassisNo((String) row.get("chassis_no"));
					response.setEngineNo((String) row.get("engine_no"));
					response.setVinNo((String) row.get("vin_no"));
					response.setMfgInvoiceNo((String) row.get("MfgInvoiceNumber"));
					response.setMfgInvoiceDate((Date) row.get("MfgInvoiceDate"));
					response.setProfitCenter((String) row.get("pc_desc"));;
					response.setRegistrationNo((String) row.get("registration_number"));
					response.setSaleDate((Date) row.get("saleDate"));
					response.setCustomerName((String) row.get("customername"));
					response.setCustomerMobile((String) row.get("customermobile"));
					response.setCustomerCode((String) row.get("CustomerCode"));
					
					
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					responseModelList.add(response);
				}
				responseListModel.setRecordCount(recordCount);
				responseListModel.setSearchResult(responseModelList);
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseListModel;
	}
		
}
