/**
 * 
 */
package com.hitech.dms.web.dao.allotment.enq.dtl;

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

import com.hitech.dms.web.model.allot.enq.dtl.request.MachineEnqDtlForAllotRequestModel;
import com.hitech.dms.web.model.allot.enq.dtl.response.MachineEnqDtlForAllotResponseModel;
import com.hitech.dms.web.model.allot.enq.dtl.response.MachineEnqMachDtlForAllotResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class MachineEnqDtlForAllotDaoImpl implements MachineEnqDtlForAllotDao {
	private static final Logger logger = LoggerFactory.getLogger(MachineEnqDtlForAllotDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public MachineEnqDtlForAllotResponseModel fetchEnqDtlForAllot(String userCode,
			MachineEnqDtlForAllotRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqDtlForAllot invoked.." + requestModel.toString());
		}
		Session session = null;
		MachineEnqDtlForAllotResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchEnqDtlForAllot(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new MachineEnqDtlForAllotResponseModel();
					responseModel.setMsg((String) row.get("msg"));
					responseModel.setCustomerId((BigInteger) row.get("CustomerId"));
					responseModel.setCustomerCode((String) row.get("CustomerCode"));
					responseModel.setCustomerType((String) row.get("ProspectCategory"));
					responseModel.setCustomerCategoryId((BigInteger) row.get("CustomerCategory_Id"));
					responseModel.setOrganizationName((String) row.get("CompanyName"));
					responseModel.setCustomerName((String) row.get("CustomerName"));
					responseModel.setContactTitle((String) row.get("Title"));
					responseModel.setFirstName((String) row.get("FirstName"));
					responseModel.setMiddleName((String) row.get("MiddleName"));
					responseModel.setLastName((String) row.get("LastName"));
					responseModel.setWhatsAppNo((String) row.get("WhatsappNo"));
					responseModel.setAlternateNo((String) row.get("Alternate_No"));
					responseModel.setMobileNo((String) row.get("PhoneNumber"));
					responseModel.setEmailId((String) row.get("Email_id"));
					responseModel.setAddress1((String) row.get("address1"));
					responseModel.setAddress2((String) row.get("address2"));
					responseModel.setAddress3((String) row.get("address3"));
					responseModel.setDateOfBirth((String) row.get("DateOfBirth"));
					responseModel.setAnniversaryDate((String) row.get("AnniversaryDate"));
					responseModel.setPanNo((String) row.get("PAN_NO"));
					responseModel.setGstIN((String) row.get("GSTIN"));
					responseModel.setPinId((BigInteger) row.get("PinId"));
					responseModel.setPincode((String) row.get("pincode"));
					responseModel.setVillage((String) row.get("village"));
					responseModel.setTehsil((String) row.get("tehsil"));
					responseModel.setDistrict((String) row.get("district"));
					responseModel.setState((String) row.get("state"));
					responseModel.setCountry((String) row.get("country"));
//					responseModel.setPinId((BigInteger) row.get("pin_id"));
					responseModel.setCityId((BigInteger) row.get("city_id"));
					responseModel.setTehsilId((BigInteger) row.get("tehsil_id"));
					responseModel.setDistrictId((BigInteger) row.get("district_id"));
					responseModel.setStateId((BigInteger) row.get("state_id"));
					responseModel.setCountryId((BigInteger) row.get("country_id"));
					responseModel.setQuantity((Integer) row.get("receipt_quantity"));
					responseModel.setSalesMan((String) row.get("salesmanName"));				}
				requestModel.setFlag(2);
				data = fetchEnqDtlForAllot(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<MachineEnqMachDtlForAllotResponseModel> enqMachDtlList = new ArrayList<MachineEnqMachDtlForAllotResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						MachineEnqMachDtlForAllotResponseModel dtlForMachineResponseModel = new MachineEnqMachDtlForAllotResponseModel();
						dtlForMachineResponseModel.setVinId((BigInteger) row.get("VinId"));
						dtlForMachineResponseModel.setMachineInventoryId((BigInteger) row.get("machine_inventory_id"));
						dtlForMachineResponseModel.setItemNo((String) row.get("ItemNo"));
						dtlForMachineResponseModel.setItemDesc((String) row.get("ItemDesc"));
						dtlForMachineResponseModel.setStockType((String) row.get("StockType"));
						dtlForMachineResponseModel.setChassisNo((String) row.get("ChassisNo"));
						dtlForMachineResponseModel.setVinNo((String) row.get("VinNo"));
						dtlForMachineResponseModel.setEngineNo((String) row.get("EngineNo"));
						dtlForMachineResponseModel.setGrnId((BigInteger) row.get("GrnId"));
						dtlForMachineResponseModel.setGrnNumber((String) row.get("GrnNumber"));
						dtlForMachineResponseModel.setInvoiceNo((String) row.get("InvoiceNo"));
						dtlForMachineResponseModel.setInvoiceDate((String) row.get("InvoiceDate"));
						dtlForMachineResponseModel.setAgeInDays((Integer) row.get("AgeInDays"));
						dtlForMachineResponseModel.setProductGroup((String) row.get("product_group"));
						dtlForMachineResponseModel.setMachineItemId((BigInteger) row.get("machine_item_id"));
						dtlForMachineResponseModel.setQuantity((Integer) row.get("receipt_quantity"));
						enqMachDtlList.add(dtlForMachineResponseModel);
					}

					responseModel.setEnqMachineDtlList(enqMachDtlList);
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
	public List fetchEnqDtlForAllot(Session session, String userCode, MachineEnqDtlForAllotRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqDtlForAllot invoked...");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_ALLOT_ENQ_DTL] :userCode, :dealerId, :branchId, :pcId, :enqNo, :enqId, :flag, :isFor,:bussinessType";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("enqNo", requestModel.getEnquiryNo());
			query.setParameter("enqId", requestModel.getEnquiryHdrId());
			query.setParameter("flag", requestModel.getFlag());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setParameter("bussinessType", requestModel.getBussinessType());
			
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
