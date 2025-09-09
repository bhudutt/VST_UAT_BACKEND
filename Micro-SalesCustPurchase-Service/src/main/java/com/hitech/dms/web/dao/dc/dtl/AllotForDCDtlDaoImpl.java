/**
 * 
 */
package com.hitech.dms.web.dao.dc.dtl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.dc.dtl.request.AllotForDCDtlRequestModel;
import com.hitech.dms.web.model.dc.dtl.response.AllotForDCDtlResponseModel;
import com.hitech.dms.web.model.dc.dtl.response.AllotMachDtlForDCResponseModel;
import com.hitech.dms.web.model.dc.dtl.response.CheckListForDCResponseModel;
import com.hitech.dms.web.model.dc.dtl.response.CustDtlResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class AllotForDCDtlDaoImpl implements AllotForDCDtlDao {
	private static final Logger logger = LoggerFactory.getLogger(AllotForDCDtlDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public AllotForDCDtlResponseModel fetchAllotDtlForDc(String userCode, AllotForDCDtlRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchAllotDtlForDc invoked.." + requestModel.toString());
		}
		Session session = null;
		AllotForDCDtlResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchAllotDtlForDc(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new AllotForDCDtlResponseModel();
					responseModel.setMachineAllotmentId((BigInteger) row.get("MachineAllotmentId"));
					responseModel.setAllotNumber((String) row.get("AllotNumber"));
					responseModel.setAllotDate((String) row.get("AllotDate"));
					responseModel.setEnquiryHdrId((BigInteger) row.get("EnquiryId"));
					responseModel.setEnquiryNo((String) row.get("EnquiryNumber"));
					responseModel.setEnquiryType((String) row.get("EnquiryType"));
					responseModel.setEnquiryDate((String) row.get("EnquiryDate"));
				}

				requestModel.setFlag(2);
				data = fetchAllotDtlForDc(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					CustDtlResponseModel customerDetail = null;
					for (Object object : data) {
						Map row = (Map) object;
						customerDetail = new CustDtlResponseModel();						
						customerDetail.setCustomerId((BigInteger) row.get("CustomerId"));
						customerDetail.setCustomerCode((String) row.get("CustomerCode"));
						customerDetail.setCustomerType((String) row.get("ProspectCategory"));
						customerDetail.setCustomerCategoryId((BigInteger) row.get("CustomerCategoryId"));
						customerDetail.setOrganizationName((String) row.get("CompanyName"));
						customerDetail.setCustomerName((String) row.get("CustomerName"));
						customerDetail.setContactTitle((String) row.get("Title"));
						customerDetail.setFirstName((String) row.get("FirstName"));
						customerDetail.setMiddleName((String) row.get("MiddleName"));
						customerDetail.setLastName((String) row.get("LastName"));
						customerDetail.setWhatsAppNo((String) row.get("WhatsappNo"));
						customerDetail.setAlternateNo((String) row.get("Alternate_No"));
						customerDetail.setMobileNo((String) row.get("PhoneNumber"));
						customerDetail.setEmailId((String) row.get("Email_id"));
						customerDetail.setAddress1((String) row.get("address1"));
						customerDetail.setAddress2((String) row.get("address2"));
						customerDetail.setAddress3((String) row.get("address3"));
						customerDetail.setDateOfBirth((String) row.get("DateOfBirth"));
						customerDetail.setAnniversaryDate((String) row.get("AnniversaryDate"));
						customerDetail.setPanNo((String) row.get("PAN_NO"));
						customerDetail.setGstIN((String) row.get("GSTIN"));
						customerDetail.setPinId((BigInteger) row.get("PinId"));
						customerDetail.setPincode((String) row.get("pincode"));
						customerDetail.setVillage((String) row.get("village"));
						customerDetail.setTehsil((String) row.get("tehsil"));
						customerDetail.setDistrict((String) row.get("district"));
						customerDetail.setCity((String) row.get("city"));
						customerDetail.setState((String) row.get("state"));
						customerDetail.setCountry((String) row.get("country"));
						customerDetail.setPinId((BigInteger) row.get("pin_id"));
						customerDetail.setCityId((BigInteger) row.get("city_id"));
						customerDetail.setTehsilId((BigInteger) row.get("tehsil_id"));
						customerDetail.setDistrictId((BigInteger) row.get("district_id"));
						customerDetail.setStateId((BigInteger) row.get("state_id"));
						customerDetail.setCountryId((BigInteger) row.get("country_id"));
					}

					responseModel.setCustomerDetail(customerDetail);
				}
				requestModel.setFlag(3);
				data = fetchAllotDtlForDc(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<AllotMachDtlForDCResponseModel> machineDetailList = new ArrayList<AllotMachDtlForDCResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						AllotMachDtlForDCResponseModel dtlForMachineResponseModel = new AllotMachDtlForDCResponseModel();
						dtlForMachineResponseModel.setVinId((BigInteger) row.get("VinId"));
						dtlForMachineResponseModel.setMachineInventoryId((BigInteger) row.get("machine_inventory_id"));
						dtlForMachineResponseModel.setItemNo((String) row.get("ItemNo"));
						dtlForMachineResponseModel.setItemDesc((String) row.get("ItemDesc"));
						dtlForMachineResponseModel.setChassisNo((String) row.get("ChassisNo"));
						dtlForMachineResponseModel.setVinNo((String) row.get("VinNo"));
						dtlForMachineResponseModel.setEngineNo((String) row.get("EngineNo"));
						dtlForMachineResponseModel.setModel((String) row.get("Model"));
						dtlForMachineResponseModel.setQty((Integer) row.get("Qty"));
						dtlForMachineResponseModel.setProductType((String) row.get("product_type"));
						dtlForMachineResponseModel.setItemId((BigInteger) row.get("MachineItemId"));

						machineDetailList.add(dtlForMachineResponseModel);
					}

					responseModel.setDcDtlList(machineDetailList);
				}
				requestModel.setFlag(4);
				data = fetchAllotDtlForDc(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<CheckListForDCResponseModel> checkList = new ArrayList<CheckListForDCResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						CheckListForDCResponseModel checkResponseModel = new CheckListForDCResponseModel();
						checkResponseModel.setCheckListId((Integer) row.get("CheckListId"));
						checkResponseModel.setPcId((Integer) row.get("PcId"));
						checkResponseModel.setDeliverableChecklist((String) row.get("DeliverableChecklist"));

						checkList.add(checkResponseModel);
					}

					responseModel.setDcCheckList(checkList);
				}
			}

		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List fetchAllotDtlForDc(Session session, String userCode, AllotForDCDtlRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchAllotDtlForDc invoked...");
		}
		logger.info(requestModel.toString());
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_DC_ALLOT_DTL] :userCode, :dealerId, :branchId, :pcId, :allotNumber, :machineAllotmentId, :flag, :isFor";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("allotNumber", requestModel.getAllotNumber());
			query.setParameter("machineAllotmentId", requestModel.getMachineAllotmentId());
			query.setParameter("flag", requestModel.getFlag());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			data = query.list();
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return data;
	}
}
