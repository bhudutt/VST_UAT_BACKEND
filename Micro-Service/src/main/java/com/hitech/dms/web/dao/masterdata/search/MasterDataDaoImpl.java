
package com.hitech.dms.web.dao.masterdata.search;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.master.servicesourcemaster.request.LabourGroupMasterEntity;
import com.hitech.dms.web.entity.master.servicesourcemaster.request.PartyMasterEntity;
import com.hitech.dms.web.entity.master.servicesourcemaster.request.ServiceLabourMasterEntity;
import com.hitech.dms.web.entity.master.servicesourcemaster.request.ServiceSourceEntity;
import com.hitech.dms.web.entity.partmaster.create.request.AdminPartMasterEntity;
import com.hitech.dms.web.model.masterdata.request.MasterDataModelRequest;
import com.hitech.dms.web.model.masterdata.request.TaxDTO;
import com.hitech.dms.web.model.masterdata.request.TaxDetailsDTO;
import com.hitech.dms.web.model.masterdata.response.MasterDataModelResponse;
import com.hitech.dms.web.model.partmaster.create.request.PartDetailsDTO;

@Repository
public class MasterDataDaoImpl implements MasterDataDao {

	private static final Logger logger = LoggerFactory.getLogger(MasterDataDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private CommonDao commonDao;

	@Override
	public List<ServiceSourceEntity> searchSourceList(String userCode) {
		Session session = null;
		List<ServiceSourceEntity> sourceList = null;
		try {
			session = sessionFactory.openSession();
			sourceList = commonDao.fetchServiceSource(session, userCode);
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}

		return sourceList;
	}

	@Override
	public List<?> searchRepairCategoryList(String userCode) {
		Session session = null;
		List<?> repairCatList = null;
		Query query = null;
		try {
			session = sessionFactory.openSession();
			query = session.createQuery(
					"select new SvRepairCategoryEntity(repairCatgId,repairCatgDesc, repairCatgCode) from SvRepairCategoryEntity se "
							+ " where se.isActive='Y'");
			repairCatList = query.list();
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			session.close();
		}

		return repairCatList;
	}

	@Override
	public List<PartyMasterEntity> searchinsuranceList(String userCode, String partyCategoryCode) {
		Session session = null;
		List<PartyMasterEntity> partyList = null;
		Query query = null;
		try {
			session = sessionFactory.openSession();
			query = session.createQuery(
					"select new PartyMasterEntity(pd.partyMasterId,pd.partyName) from PartyMasterEntity pd, CMPartyCategoryEntity cmPC"
							+ " where pd.partyCategoryId=cmPC.partyCategoryId and cmPC.partyCategoryCode=:categoryCode and pd.activeStatus=true")
					.setParameter("categoryCode", partyCategoryCode);
			partyList = query.list();
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			session.close();
		}

		return partyList;
	}

	@Override
	public List<?> serviceCategoryList(String userCode) {
		Session session = null;
		List<?> serviceCatgList = null;
		Query query = null;
		try {
			session = sessionFactory.openSession();
			query = session.createQuery(
					"select new ServiceCategoryEntity(serviceCategoryID,categoryDesc, categoryCode, activeStatus, inQuotation) from ServiceCategoryEntity where activeStatus='Y' and inQuotation = 'Y'");
			serviceCatgList = query.list();
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			session.close();
		}

		return serviceCatgList;
	}

	@Override
	public Map<Integer, String> fetchCustomerVoice(String userCode) {

		Session session = null;
		Map<Integer, String> map = new HashMap<>();
		try {
			session = sessionFactory.openSession();
			List<Object[]> l = session.createSQLQuery(
					"SELECT cust_voice_id, custvoicecode+'('+custvoicedesc+')' voice FROM SV_CUST_VOICE_MST(NOLOCK) where isactive='Y'")
					.list();
			if (l != null) {
				for (Object[] o : l) {
					map.put((Integer) o[0], (String) o[1]);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			session.close();
		}

		return map;

	}

	public List<MasterDataModelResponse> chassisSearchList(String userCode, MasterDataModelRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("Quotation Search List invoked.." + request);
		}
		Query query = null;
		Session session = null;
		MasterDataModelResponse responseObj = null;
		List<MasterDataModelResponse> responseModelList = null;
		Integer recordCount = 0;
		System.out.println("QuotationSearch List invoked ");

		try {
			session = sessionFactory.openSession();
			query = session
					.createSQLQuery("exec SearchVIMDetails :Criteria,:Searchon,:BranchId,:Modelid,:FormName");
			query.setParameter("Criteria", request.getCriteria());
			query.setParameter("Searchon", request.getSearchOnValue());
			query.setParameter("BranchId", null);
			query.setParameter("Modelid", null);
			query.setParameter("FormName", "Quotation");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			responseModelList = new ArrayList<MasterDataModelResponse>();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					responseObj = new MasterDataModelResponse();
					Map row = (Map) object;
					responseObj.setChassisNo((String) row.get("ChassisNo"));
					responseObj.setCustomerName((String) row.get("CustomerName"));
					responseObj.setEngineNo((String) row.get("EngineNo"));
					responseObj.setRegistrationNo((String) row.get("RegistrationNo"));
					responseObj.setDisplayValue((String) row.get("displayValue"));
					System.out.println("diaplayValue ***"+(String) row.get("displayValue"));


					responseModelList.add(responseObj);
				}

			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;

	}

	public MasterDataModelResponse getChassisDetails(String userCode, String chassisNo) { 
																							
		MasterDataModelResponse obj = null;
		Query query = null;
		Session session = null;
		System.out.println("chesis Dao Imple ************************");
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery("exec GetVehicleDetails :chassis ");
			query.setParameter("chassis", chassisNo);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			System.out.println("query exec "+query);
			List data = query.list();
			if (data != null && data.size() > 0) {
				for (Object object : data) {
					Map row = (Map) object;
					obj = new MasterDataModelResponse();
					obj.setChassisNo(chassisNo);
					obj.setVinId((BigInteger) row.get("vin_id"));
					obj.setStatus((String) row.get("status"));
					String status=(String) row.get("status");
					obj.setChassisNo(chassisNo);
					obj.setOmNumber((String) row.get("omNo"));
					obj.setVinNumber(row.get("vinNumber").toString());
					obj.setQuotationNumber((String) row.get("QuotationNumber"));
					obj.setDisplayValue((String) row.get("displayValue"));
					System.out.println("diaplayValue "+(String) row.get("displayValue"));
					obj.setRegistrationNo((String) row.get("RegistrationNo"));
					obj.setDistributionChannel((String) row.get("channel"));
					obj.setEngineNo((String) row.get("EngineNo"));
					obj.setModelGroupCode((String) row.get("ModelGroupCode"));
					obj.setModelGroupDesc((String) row.get("ModelGroupDesc"));
					obj.setModelVariantDesc((String) row.get("ModelVariantDesc"));
					obj.setModelFamilyDesc((String) row.get("ModelFamilyDesc"));
					obj.setFscBooklet((String) row.get("FSC_Booklet"));
					obj.setSeatCapacity((Integer) row.get("SeatCapacity"));
					obj.setOdometerReading((String) row.get("VALTN_ODMETR_REDNG"));
					obj.setMfgInvoiceDate((Date) row.get("MfgInvoiceDate"));
					obj.setOemPriviledgeCust((String) row.get("OEm_PRIVILEGE_CUSt"));
					obj.setSsiCategory((String) row.get("SSICategory"));
					obj.setQsiCategory((String) row.get("IQSCategory"));
					obj.setCsiCategory((String) row.get("CSICategory"));
					obj.setRelationMgr((String) row.get("relationship_mgr"));
					obj.setAvgKMPerDay((String) row.get("AvgKMPerDay"));
					obj.setDeliveryNoteDate((Date) row.get("DeliveryNoteDate"));
					obj.setVehicleId((String) row.get("vehicle_id"));
					obj.setSaleDate((Date) row.get("SaleDate"));
					obj.setSoldBy((String) row.get("soldBy"));
					obj.setColorCode((String) row.get("ColorCode"));
					obj.setModelDesc((String) row.get("ModelDesc"));
					obj.setRetailDate((Date) row.get("RetailDate"));
					obj.setModelFamilyId((Integer) row.get("Model_Family_Id"));
					obj.setLabourDiscountPercentage((BigDecimal) row.get("LabourDiscountPercentage"));
					obj.setPartDiscountPercentage((BigDecimal) row.get("PastDiscountPercentage"));
					obj.setInstallationDate((String) row.get("installationDate"));
					obj.setProfitcenter((String) row.get("profitcenter"));
					System.out.println("prfiltCenter "+(String) row.get("profitcenter"));
					obj.setSeries((String) row.get("series"));
					obj.setSegment((String) row.get("segment"));
					obj.setVariant((String) row.get("variant"));
					obj.setItemNo((String) row.get("itemNo"));
					obj.setItemDesc((String) row.get("itemDesc"));
					obj.setCompanyName((String) row.get("CompanyName"));
					/*
					 * Character character1 = (Character) row.get("IsPDIDone"); if (character1 !=
					 * null && "Y".equals(character1.toString())) { obj.setPDIDoneStatus(true); }
					 * else { obj.setPDIDoneStatus(false); }
					 */
					Boolean isPdiDone=(Boolean) row.get("IsPDIDone");
					obj.setIsPDIdone(isPdiDone==true?"Y":"N");
					/*
					 * Character character2 = (Character) row.get("IsRefurbished"); if (character2
					 * != null && "Y".equals(character2.toString())) {
					 * obj.setRefurbishedStatus(true); } else { obj.setRefurbishedStatus(false); }
					 */

					obj.setRefurbishedStatus((Boolean) row.get("IsRefurbished"));

					obj.setRefurbishedDate((Date) row.get("RefurbishedDate"));

					/*
					 * Character character3 = (Character) row.get("IsGovtVehicle"); if (character3
					 * != null && "Y".equals(character3.toString())) {
					 * obj.setGovtVehicleStatus(true); } else { obj.setGovtVehicleStatus(false); }
					 */
					obj.setGovtVehicleStatus((Boolean) row.get("IsGovtVehicle"));

					/*
					 * Character character4 = (Character) row.get("IsTheftVehicle"); if (character4
					 * != null && "Y".equals(character4.toString())) {
					 * obj.setTheftVehicleStatus(true); } else { obj.setTheftVehicleStatus(false); }
					 */

					obj.setTheftVehicleStatus((Boolean) row.get("IsTheftVehicle"));

					/*
					 * Character character5 = (Character) row.get("IsTotallyDamaged"); if
					 * (character5 != null && "Y".equals(character5.toString())) {
					 * obj.setTotallyDamagedStatus(true); } else {
					 * obj.setTotallyDamagedStatus(false); }
					 */

					obj.setTotallyDamagedStatus((Boolean) row.get("IsTotallyDamaged"));

					obj.setPolicyExpiryDate((Date) row.get("PolicyExpiryDate"));
					obj.setCustomerId((Integer) row.get("Customer_Id"));
					obj.setCustomerName((String) row.get("customername"));

					obj.setCustAddLine1((String) row.get("CustAddLine1"));
					obj.setCustAddLine2((String) row.get("CustAddLine2"));
					obj.setCustAddLine3((String) row.get("CustAddLine3"));
					obj.setPinCode((String) row.get("PinCode"));
					obj.setLocalityName((String) row.get("LocalityName"));
					obj.setDistrict((String) row.get("District"));
					obj.setTehsilDesc((String) row.get("TehsilDesc"));
					obj.setCityDesc((String) row.get("CityDesc"));
					obj.setStateDesc((String) row.get("StateDesc"));
					obj.setMobileNumber((String) row.get("MobileNumber"));
					obj.setFax((String) row.get("Fax"));
					obj.setEmail((String) row.get("Email1"));

					obj.setNextServiceDueDate((String) row.get("nextserviceduedate"));
					obj.setNextDueService((String) row.get("nextservicedue"));
					obj.setModelCode((String) row.get("ModelCode"));
					obj.setDivisionDesc((String) row.get("prod_divDesc"));

					obj.setSWStartDate((Date) row.get("SWStartDate"));
					obj.setSWExpiryDate((Date) row.get("SWExpiryDate"));
					obj.setSWExpiryKm((Integer) row.get("SWExpiryHours"));

					obj.setEwStartDate((Date) row.get("EWStartDate"));
					obj.setEWExpiryDate((Date) row.get("EWExpiryDate"));
					obj.setEWExpiryKm((Integer) row.get("EWExpiryHours"));

					obj.setAmcStartDate((Date) row.get("AMCStartDate"));
					obj.setAmcExpiryDate((Date) row.get("AMCExpiryDate"));
					obj.setAmcExpiryKm((Integer) row.get("AMCExpiryHours"));

					obj.setAmcPlan((String) row.get("AMCDescription"));
					obj.setAmcPolicyNo((String) row.get("S_AMCPolicyNo"));
					obj.setAmcRegNumber((String) row.get("AMCRegistrationNo"));

					obj.setRegistrationstatus((String) row.get("RegistrationStatus"));
					obj.setCustomerCode((String) row.get("Customer_Code"));
					obj.setMfgInvoiceNumber((String) row.get("MfgInvoiceNumber"));
					obj.setFcExpiryDate((Date) row.get("FCExpiryDate"));
					obj.setFcIssueDate((Date) row.get("FCIssueDate"));
					obj.setFcNumber((String) row.get("FCNumber"));
					obj.setApplicationType((String) row.get("ApplicationType"));
					obj.setBodyType((String) row.get("BodyType"));
					
					String totalHoursVal=(String) row.get("totalHours");
					if(totalHoursVal !=null) {
						obj.setTotalHours(totalHoursVal);
					}else {
						obj.setTotalHours("0");				
					}
					obj.setCountry((String) row.get("country"));
					obj.setAlternateMobNo((String) row.get("alternateMobNo"));		
					obj.setWhatsappmobileno((String) row.get("whatsappmobileno"));
					obj.setCustomerType((String) row.get("whatsappmobileno"));
					
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return obj;
	}

	public Map<String, Object> getWarrantyAndAMC(String userCode, MasterDataModelRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("Quotation Search List invoked.." + request);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> resultMap = null;
		MasterDataModelResponse responseObj = null;
		String sqlQuery = "select * from FN_getWarranty_AMC_Details (:VinID,:TotalKM)";
		String sqlQueryForService = "Exec AutoSelectSrvCategoryAndType :VinID,:TotalKM";
		Integer recordCount = 0;
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("VinID", request.getVinId());
			query.setParameter("TotalKM", request.getTotalKMReading());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				for (Object object : data) {
					Map row = (Map) object;
					Character isUnderSWarranty =  (Character) row.get("IsSWApplicable");
					Character isUnderEWarranty = (Character) row.get("IsEWApplicable");
					Character isUnderAMC = (Character) row.get("IsAMCApplicable");
					resultMap = new HashMap<>();
					resultMap.put("warrantyType", (String) row.get("Warranty_Type"));
					resultMap.put("amcStatus", (String) row.get("AMC_Status"));
					resultMap.put("isUnderSWarranty", isUnderSWarranty != null ? isUnderSWarranty.toString() : "N");
					resultMap.put("isUnderEWarranty", isUnderEWarranty != null ? isUnderEWarranty.toString() : "N");
					resultMap.put("isUnderAMC", isUnderAMC != null ? isUnderAMC.toString() : "N");
					resultMap.put("DiscoutPerParts", (BigDecimal) row.get("DiscoutPerParts"));
					resultMap.put("DiscoutPerLabour", (BigDecimal) row.get("DiscoutPerLabour"));
					resultMap.put("DiscoutPerLubricant", (BigDecimal) row.get("DiscoutPerLubricant"));
					resultMap.put("DiscoutPerWashing", (BigDecimal) row.get("DiscoutPerWashing"));
				}
			}
			query = session.createSQLQuery(sqlQueryForService);
			query.setParameter("VinID", request.getVinId());
			query.setParameter("TotalKM", request.getTotalKMReading());
			data = query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			if (data != null && data.size() > 0) {
				Map row = (Map) data.get(0);
				if (resultMap == null)
					resultMap = new HashMap<>();
				resultMap.put("ServiceTypeID", row.get("Service_Type_ID"));
				resultMap.put("ServiceCategoryID", row.get("Service_Category_ID"));
				resultMap.put("ServiceTypeoemId", row.get("Service_Type_oem_Id"));
			}

		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return resultMap;

	}

	public List<MasterDataModelResponse> getServiceType(Integer serviceCategory, Integer modelFamilyId) {

		Session session = null;
		List<Object[]> serviceType = null;
		List<?> serviceTypeId = null;
		Object[] object = null;
		Map<String, Object> map = null;
		List<MasterDataModelResponse> list=new ArrayList<MasterDataModelResponse>();
		try {
			session = sessionFactory.openSession();
			
			
			serviceType = session.createQuery(
					"select serviceTypeOemId,svTypeDesc from OEMServiceTypeEntity where serviceCategoryId='"
							+ serviceCategory + "'")
					.list();
			map = new HashMap<>();
			if (serviceType != null && !serviceType.isEmpty()) {
				Iterator<Object[]> itr = serviceType.iterator();
				while (itr.hasNext()) {
					object = itr.next();
					serviceTypeId = session
							.createQuery("select serviceTypeId from SVServiceTypeEntity where serviceTypeOemId='"
									+ object[0] +"'").list();  //Service_Type_oem_Id
					if (serviceTypeId != null && serviceTypeId.size() > 0) {
						MasterDataModelResponse res=new MasterDataModelResponse();
						res.setServiceId((Integer)serviceTypeId.get(0));
						res.setServiceVal(object[1].toString());
						list.add(res);
					}
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public List<LabourGroupMasterEntity> getLabouGroupDescList(Integer vinId, String userCode) {

		Session session = null;
		List<Object> templabourGroupMasterList = null;
		List<LabourGroupMasterEntity> labourGroupMasterList = null;
		LabourGroupMasterEntity serviceLabourGroupMaster = null;
		Query query = null;
		try {
			session = sessionFactory.openSession();
			String sqlQuery = "exec [SP_GetLabourGroup] :userCode,:vinId";
			query = session.createSQLQuery(sqlQuery).setParameter("userCode", userCode).setParameter("vinId", vinId);
			templabourGroupMasterList = query.list();
			labourGroupMasterList = new ArrayList<>();
			Iterator it = templabourGroupMasterList.iterator();
			while (it.hasNext()) {
				serviceLabourGroupMaster = new LabourGroupMasterEntity();
				Object[] tmpObj = (Object[]) it.next();
				serviceLabourGroupMaster.setLabourGroupId((Integer) tmpObj[0]);
				serviceLabourGroupMaster.setLabourGroupDesc((String) tmpObj[1]);
				serviceLabourGroupMaster.setLabourgroupcode((String) tmpObj[2]);
				labourGroupMasterList.add(serviceLabourGroupMaster);
			}

		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			session.close();
		}

		return labourGroupMasterList;
	}
	 

	public Date convertStringToDate(String date) { Date date1 = null; 
		try { 
				date1= new SimpleDateFormat("dd-MM-yyyy").parse(date);
	
   } catch (ParseException e) {
   // TODO Auto-generated catch block 
	   e.printStackTrace(); 
	   } return date1; 
	 }

	
	public Map<String, String> validateServiceType(MasterDataModelRequest request,String userCode ) {

		if (logger.isDebugEnabled()) {
			logger.debug("Quotation Search List invoked.." + request);
		}
		Query query = null;
		Session session = null;
		Map<String, String> resultMap = null;
		Integer recordCount = 0;
		try {
			session = sessionFactory.openSession();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String strDate = sdf.format(new Date());
			String sqlQuery = "exec ValidateServiceType :BranchID,:VinID,:SrvTypeID,:ROOpeningDate,:warranty ,:amc ,:ModelFamilyID,:TotalKM,:isFor";
			String validatePart = "exec validatePartForJobCard :BranchID,:partNo,:billableType,:SrvTypeID";
			if (request != null && request.getIsFor() != null
					&& request.getIsFor().equalsIgnoreCase("validatePart")) {
				query = session.createSQLQuery(validatePart);
				query.setParameter("BranchID", request.getBranchId());
				query.setParameter("partNo", request.getPartNumber());
				query.setParameter("SrvTypeID", request.getServiceTypeId());
				query.setParameter("billableType", request.getBillabillType());
			}else {
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("BranchID", request.getBranchId());
				query.setParameter("VinID", request.getVinId());
				query.setParameter("SrvTypeID", request.getServiceTypeId());
				query.setParameter("ROOpeningDate", strDate);
				query.setParameter("warranty", request.getWarrantyType());
				query.setParameter("amc", request.getAmcStatus());
				query.setParameter("ModelFamilyID", request.getModelFamilyId());
				query.setParameter("TotalKM", request.getTotalKMReading());
				query.setParameter("isFor", request.getIsFor());
			}
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			resultMap = resultMap = new HashMap<>();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					resultMap.put("allowCreation", (String) row.get("AllowCreation"));
					resultMap.put("errMessage", (String) row.get("ErrMessage"));
				}

			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return resultMap;

	}
  
	public List<ServiceLabourMasterEntity> fetchLabourCode(String userCode, MasterDataModelRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("Quotation Search List invoked.." + request);
		}
		Query query = null;
		Session session = null;
		List<ServiceLabourMasterEntity> lbrMasterList = null;
		String procedureQuery = "exec SearchLabourDetails  :criteria, :searchOn, :labourGrp, :modelFamilyId, :branchId";
		if (request.getDocType()!= null && !request.getDocType().equals("") && (request.getDocType().equalsIgnoreCase("RO") || request.getDocType().equalsIgnoreCase("QUO")
				|| request.getDocType().equalsIgnoreCase("APP") || request.getDocType().equalsIgnoreCase("OUTSIDELBR"))) {
			procedureQuery = "exec SearchLabourDetailsForRO  :criteria, :searchOn, :labourGrp, :vinId, :branchId, :docType";
		}
		Integer recordCount = 0;
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(procedureQuery);
			query.setParameter("searchOn", request.getSearchOnValue());
			query.setParameter("criteria", request.getCriteria());
			query.setParameter("labourGrp", request.getLabourGrp());
			query.setParameter("vinId", request.getModelFamilyId());
			query.setParameter("branchId", request.getBranchId());
			query.setParameter("docType", request.getDocType());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				lbrMasterList = new ArrayList<ServiceLabourMasterEntity>();
				ServiceLabourMasterEntity masterEntity = null;
				for (Object object : data) {
					Map row = (Map) object;
					masterEntity = new ServiceLabourMasterEntity();
					masterEntity.setLabourId((BigInteger) row.get("Labour_id"));
					masterEntity.setLabourCode((String) row.get("LabourCode"));
					masterEntity.setLabourDesc((String) row.get("LabourDesc"));
					masterEntity.setDisplayValue((String) row.get("displayValue"));
					System.out.println("displayValue "+(String) row.get("displayValue"));
					masterEntity.setLabourGroupId((BigInteger) row.get("Labour_Group_Id"));
					lbrMasterList.add(masterEntity);
				}
			}
			

		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return lbrMasterList;

	}
	public Map<String, Object> fetchLbrDetails(String labourCode,Integer labourGroup,String chassisNo,String userCode,String branchId,String doctype){


		if (logger.isDebugEnabled()) {
			logger.debug("Quotation Search List invoked..");
		}
		Query query = null;
		Session session = null;
		Map<String, Object> map = new HashMap<>();
		String procedureQuery = "EXEC SP_LABOUR_DETAILS_QUO :branchId, :LabourCode, :LabourGroup, :Chassis, :doctype";
		
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(procedureQuery);
			query.setParameter("branchId", branchId);
			query.setParameter("LabourCode", labourCode);
			query.setParameter("LabourGroup", labourGroup);
			query.setParameter("Chassis", chassisNo);
			query.setParameter("doctype", doctype);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					


					map.put("labourId", (BigInteger) row.get("Labour_id"));
					map.put("labourDesc", (String) row.get("LabourDesc"));
					map.put("standardHrs", (Integer) row.get("StdHrs"));
					map.put("customerRate", (BigDecimal) row.get("rate"));
					map.put("chargeAmt", (BigDecimal) row.get("Charge"));
					map.put("hsnCode", (String) row.get("HSN_CODE"));

				}
			}
			

		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return map;

	
	}
  
	public TaxDetailsDTO fetchTaxBreakInSingleRow(String userCode,TaxDTO request) {
		if (logger.isDebugEnabled()) {
			logger.debug("Quotation Search List invoked..");
		}
		Query query = null;
		Session session = null;
		TaxDetailsDTO obj=new TaxDetailsDTO();
		String procedureQuery = "exec GetTaxeBreakForParts :branchId,:calcualtionFor ,:partOrLabourBranchId,:partyBranchId,:customerId,:stateId,:saleAmount,:discount";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(procedureQuery);
			query.setParameter("branchId", request.getBranchId());
			query.setParameter("calcualtionFor", request.getCalcualtionFor());
			query.setParameter("partOrLabourBranchId", request.getPartOrLaborBranchId());
			query.setParameter("partyBranchId", request.getParatyBranchId());
			query.setParameter("customerId", request.getCustomerId());
			query.setParameter("stateId", request.getStateId());
			query.setParameter("saleAmount", request.getSaleAmount());
			query.setParameter("discount", request.getDiscount());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					
					obj.setCgst((BigDecimal) row.get("cgst"));
					obj.setSgst((BigDecimal) row.get("sgst"));
					obj.setIgst((BigDecimal) row.get("igst"));
					obj.setCgstPercentage((BigDecimal) row.get("cgstPercentage"));
					obj.setSgstPercentage((BigDecimal) row.get("sgstPercentage"));
					obj.setIgstPercentage((BigDecimal) row.get("igstPercentage"));
					obj.setTotalChargeAmount((BigDecimal) row.get("totalChargeAmount"));
					obj.setTotalAmount((BigDecimal) row.get("totalAmount"));
					
				}
			}
			

		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return obj;
	
	}
	public List<AdminPartMasterEntity> searchPartNumberDetails(String userCode,MasterDataModelRequest request){

		if (logger.isDebugEnabled()) {
			logger.debug("Quotation Search List invoked..");
		}
		Query query = null;
		Session session = null;
		List<AdminPartMasterEntity> partMasterList = null;
		String procedureQuery = "exec [SearchPartDetails] :branchId, :criteria, :searchOn ,:division,:partCategoryCode ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(procedureQuery);
			query.setParameter("branchId", request.getBranchId());
			query.setParameter("searchOn", request.getSearchOnValue());
			query.setParameter("criteria", request.getCriteria());
			query.setParameter("division", request.getPartDivision());
			query.setParameter("partCategoryCode", request.getPartCategoryCode());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			partMasterList = new ArrayList<AdminPartMasterEntity>();
			AdminPartMasterEntity masterEntity = null;
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					masterEntity = new AdminPartMasterEntity();
					masterEntity.setId((Integer) row.get("part_id"));
					masterEntity.setPartNumber((String) row.get("PartNumber"));
					masterEntity.setPartDescription((String) row.get("PartDesc"));
					partMasterList.add(masterEntity);
					
				}
			}
			

		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return partMasterList;
	}
	
	public PartDetailsDTO getPartDetails(Integer branchID,String partNo,String docType,String jobCardId,String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("Quotation Search List invoked..");
		}
		Query query = null;
		Session session = null;
		PartDetailsDTO objVal = new PartDetailsDTO();
		String procedureQuery = "exec [SP_SparesGetPartDetail] :branchId, :partNo, :docType ,:jobCardId ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(procedureQuery);
			query.setParameter("branchId", branchID);
			query.setParameter("partNo", partNo);
			query.setParameter("docType", docType);
			query.setParameter("jobCardId", jobCardId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					objVal.setBranchId((Integer) row.get("branchId"));
					objVal.setPartNo((String) row.get("partNo"));
					objVal.setPartId((Integer) row.get("partID"));
					objVal.setPartBranchId((Integer) row.get("partBranchId"));
					objVal.setMrp((BigDecimal) row.get("mrp"));
					objVal.setNdp((BigDecimal) row.get("ndp"));
					objVal.setSellingPrice((BigDecimal) row.get("sellingPrice"));
					objVal.setPartDescription((String) row.get("partDescription"));
					objVal.setCurrentStock((BigDecimal) row.get("currentStock"));
					objVal.setTotalStock((BigDecimal) row.get("totalStock"));
					objVal.setStockAmount((BigDecimal) row.get("stockAmount"));
					objVal.setUom((String) row.get("uom"));
					objVal.setErrorMsg((String) row.get("errorMsg"));
					objVal.setDocType((String) row.get("docType"));
					objVal.setJobCardId((Integer) row.get("jobCardId"));
					objVal.setMinLevelQty((BigDecimal) row.get("MinLevelQty"));
					objVal.setMaxLevelQty((BigDecimal) row.get("MaxLevelQty"));
					objVal.setReorderLevelQty((BigDecimal) row.get("ReorderLevelQty"));
					objVal.setSafetyStockQty((BigDecimal) row.get("SafetyStockQty"));;
					objVal.setLandedCost((BigDecimal) row.get("landedCost"));
				//	objVal.setAllowDecimalInQty((Character) row.get("allowDecimalInQty"));
				//	objVal.setLockForTranscation((Character) row.get("lockForTranscation"));
					
				}
			}
			

		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return objVal;
	
	}
	
  }
