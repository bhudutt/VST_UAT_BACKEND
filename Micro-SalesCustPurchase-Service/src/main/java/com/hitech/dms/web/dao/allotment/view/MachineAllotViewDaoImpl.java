/**
 * 
 */
package com.hitech.dms.web.dao.allotment.view;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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

import com.hitech.dms.web.model.allot.view.request.MachineAllotViewRequestModel;
import com.hitech.dms.web.model.allot.view.response.MachineAllotMachDtlViewResponseModel;
import com.hitech.dms.web.model.allot.view.response.MachineAllotViewResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class MachineAllotViewDaoImpl implements MachineAllotViewDao {
	private static final Logger logger = LoggerFactory.getLogger(MachineAllotViewDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public MachineAllotViewResponseModel fetchMachineAllotDtl(String userCode,
			MachineAllotViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachineAllotDtl invoked.." + requestModel.toString());
		}
		Session session = null;
		MachineAllotViewResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchMachineAllotDtl(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new MachineAllotViewResponseModel();
					responseModel.setDealerId((BigInteger) row.get("dealerId"));
					responseModel.setDealerCode((String) row.get("dealerCode"));
					responseModel.setDealerName((String) row.get("dealerName"));
					responseModel.setBranchId((BigInteger) row.get("branchId"));
					responseModel.setBranchCode((String) row.get("branchCode"));
					responseModel.setBranchName((String) row.get("branchName"));
					responseModel.setPcId((Integer) row.get("PcId"));
					responseModel.setPcDesc((String) row.get("PcDesc"));
					responseModel.setMachineAllotmentId((BigInteger) row.get("MachineAllotmentId"));
					responseModel.setAllotNumber((String) row.get("AllotNumber"));
					responseModel.setAllotStatus((String) row.get("AllotStatus"));
					responseModel.setAllotDate((String) row.get("AllotDate"));
					//responseModel.setAllotDate1((Date) row.get("AllotDate1"));
					responseModel.setDeAllotBy((String) row.get("DeAllotBy"));
					responseModel.setDeAllotDate((String) row.get("DeAllotDate"));
					responseModel.setDeAllotReason((String) row.get("DeAllotReason"));
					responseModel.setMobileNo((String) row.get("Mobile_No"));
					responseModel.setAction((String) row.get("action"));
					responseModel.setCustomerId((BigInteger) row.get("CustomerId"));
					responseModel.setCustomerCode((String) row.get("CustomerCode"));
					responseModel.setCustomerType((String) row.get("ProspectCategory"));
					responseModel.setCustomerCategoryId((BigInteger) row.get("CustomerCategoryId"));
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
					responseModel.setCity((String) row.get("city"));
					responseModel.setState((String) row.get("state"));
					responseModel.setCountry((String) row.get("country"));
					responseModel.setPinId((BigInteger) row.get("pin_id"));
					responseModel.setCityId((BigInteger) row.get("city_id"));
					responseModel.setTehsilId((BigInteger) row.get("tehsil_id"));
					responseModel.setDistrictId((BigInteger) row.get("district_id"));
					responseModel.setStateId((BigInteger) row.get("state_id"));
					responseModel.setCountryId((BigInteger) row.get("country_id"));
					responseModel.setOnlyImplementFlag((Boolean)row.get("only_implement_flag"));
					responseModel.setEnquiryNumber((String)row.get("enquiryNumber"));
					//System.out.println("only_implement_flag "+row.get("only_implement_flag"));
					responseModel.setSalesmanName((String)row.get("salesmanName"));
					
					
				}
				requestModel.setFlag(2);
				data = fetchMachineAllotDtl(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<MachineAllotMachDtlViewResponseModel> enqMachDtlList = new ArrayList<MachineAllotMachDtlViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						MachineAllotMachDtlViewResponseModel dtlForMachineResponseModel = new MachineAllotMachDtlViewResponseModel();
						dtlForMachineResponseModel.setVinId((BigInteger) row.get("VinId"));
						dtlForMachineResponseModel.setMachineInventoryId((BigInteger) row.get("machine_inventory_id"));
						dtlForMachineResponseModel.setMachineAllotmentDtlId((BigInteger) row.get("MachineAllotmentDtlId"));
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
						if(row.get("AgeInDays") !=null) {
							dtlForMachineResponseModel.setAgeInDays((Integer) row.get("AgeInDays"));
						}
						dtlForMachineResponseModel.setProductGroup((String) row.get("ProductGroup"));
						
						enqMachDtlList.add(dtlForMachineResponseModel);
					}

					responseModel.setMachineAllotDtlList(enqMachDtlList);
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
	public List fetchMachineAllotDtl(Session session, String userCode, MachineAllotViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachineAllotDtl invoked...");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_ALLOT_VIEW] :userCode, :allotNo, :allotId, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("allotNo", requestModel.getAllotNumber());
			query.setParameter("allotId", requestModel.getMachineAllotmentId());
			query.setParameter("flag", requestModel.getFlag());
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
