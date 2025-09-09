package com.hitech.dms.web.dao.oldchassis.validate;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.oldchassis.validate.request.OldChassisValidateRequestModel;
import com.hitech.dms.web.model.oldchassis.validate.response.OldChassisDTLResponseModel;
import com.hitech.dms.web.model.oldchassis.validate.response.OldChassisValidateResponseModel;
import com.hitech.dms.web.model.oldchassis.validate.response.OldChassisVinDetailsResponseModel;

@Repository
public class OldChassisValidateDaoImpl implements OldChassisValidateDao{

	private static final Logger logger = LoggerFactory.getLogger(OldChassisValidateDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public OldChassisDTLResponseModel fetchOldChassisDTLList(String userCode, BigInteger vinId) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOldChassisDTLList invoked.." + vinId);
		}
		   
		Session session = null;
		OldChassisDTLResponseModel responseModel = new OldChassisDTLResponseModel();
		try {
			session = sessionFactory.openSession();
			OldChassisVinDetailsResponseModel oldChassisViewModel = OldChassisVinDetails(session, userCode, vinId);
			responseModel.setVinDTLResponse(oldChassisViewModel);
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
	public OldChassisVinDetailsResponseModel OldChassisVinDetails(Session session, String userCode,
			BigInteger vinId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnquiryDTL invoked.." + vinId);
		}
		Query query = null;
		OldChassisVinDetailsResponseModel responseModel = null;
		String sqlQuery = "exec [sp_get_fetch_customer_by_vin] :vinId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("vinId", vinId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new OldChassisVinDetailsResponseModel();
					responseModel.setVinId((BigInteger) row.get("vin_id"));
					responseModel.setChassisNo((String) row.get("chassis_no"));
					responseModel.setEngineNo((String) row.get("engine_no"));
					responseModel.setVinNo((String) row.get("vin_no"));
					responseModel.setRegistrationNo((String) row.get("registration_number"));
					responseModel.setSaledate((Date) row.get("Sale_Date"));
					responseModel.setPcId((Integer) row.get("pc_id"));
					responseModel.setPcName((String) row.get("pc_desc"));
					responseModel.setSeriesId((Integer) row.get("Series_Id"));
					responseModel.setSeriesName((String) row.get("series_name"));
					responseModel.setSegementId((Integer) row.get("Segment_Id"));
					responseModel.setSegementName((String) row.get("segment_name"));
					responseModel.setModelId((Integer) row.get("Mdoel_Id"));
					responseModel.setVariant((String) row.get("variant"));
					responseModel.setVariantId((Integer) row.get("Variant_Id"));
					responseModel.setModelName((String) row.get("model_name"));
					responseModel.setStatus((String) row.get("status"));
					responseModel.setItemId((Integer) row.get("Item_Id"));
					responseModel.setItem((String) row.get("item_no"));
					responseModel.setItemDescription((String) row.get("item_description"));
					responseModel.setCustomerId((BigInteger) row.get("customer_id"));
					responseModel.setMobileNo((String) row.get("mobile_no"));
					responseModel.setFirstname((String) row.get("firstname"));
					responseModel.setMiddlename((String) row.get("middlename"));
					responseModel.setLastname((String) row.get("lastname"));
					responseModel.setWhatappNo((String) row.get("whatsappno"));
					responseModel.setAlternateNo((String) row.get("alternate_no"));
					responseModel.setEmailId((String) row.get("email_id"));
					responseModel.setPanNo((String) row.get("pan_no"));
					responseModel.setAadharNo((String) row.get("AADHAR_CARD_NO"));
					responseModel.setAddress1((String) row.get("address1"));
					responseModel.setAddress2((String) row.get("address2"));
					responseModel.setAddress3((String) row.get("address3"));
					responseModel.setStatus1((String) row.get("status1"));
					responseModel.setCid((Integer) row.get("ID"));
					responseModel.setDistrictId((Integer) row.get("DISTRICT_ID"));
					responseModel.setDistrict((String) row.get("districtDesc"));
					responseModel.setTehsilId((Integer) row.get("TEHSIL_ID"));
					responseModel.setTehsil((String) row.get("TehsilDesc"));
					responseModel.setCityId((Integer) row.get("VILLAGE_ID"));
					responseModel.setCity((String) row.get("citydesc"));
					responseModel.setPinId((BigInteger) row.get("pin_id"));
					responseModel.setPinCode((Integer) row.get("pincode"));
					responseModel.setStateId((Integer) row.get("STATE_ID"));
					responseModel.setState((String) row.get("stateDesc"));
					responseModel.setCountryId((Integer) row.get("COUNTRY"));
					responseModel.setCountry((String) row.get("countryDesc"));
					responseModel.setPlantCode((String)row.get("plant_code"));
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

		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public OldChassisValidateResponseModel fetchOldChassisDTLList(String userCode,
			OldChassisValidateRequestModel oldChassisValidateRequestModel) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOldChassisDTLList invoked.." + oldChassisValidateRequestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		OldChassisValidateResponseModel responseModel = new OldChassisValidateResponseModel();
		Query query = null;
		boolean isFailure = false;
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query1 = session.createQuery("UPDATE ChassisMachineVinMstEntity SET chassis_no =:chassisNo, engine_no=:engineNo, vin_no=:vinNo, registration_number=:registrationNumber, Sale_Date=:saleDate, Status=:status, plant_code=:plantCode WHERE vin_id = :vinId");
			query1.setParameter("chassisNo", oldChassisValidateRequestModel.getChassisNo());
			query1.setParameter("engineNo", oldChassisValidateRequestModel.getEngineNo());
			query1.setParameter("vinNo", oldChassisValidateRequestModel.getVinNo());
			query1.setParameter("registrationNumber", oldChassisValidateRequestModel.getRegistrationNo());
			query1.setParameter("saleDate", oldChassisValidateRequestModel.getSaledate());
			query1.setParameter("status", oldChassisValidateRequestModel.getStatus());
			query1.setParameter("vinId", oldChassisValidateRequestModel.getVinId());
			query1.setParameter("plantCode", oldChassisValidateRequestModel.getPlantCode());
			int ChassisMachineVinMstEntity = query1.executeUpdate();
			
			Query query2 = session.createQuery("UPDATE CustomerHDREntity SET mobile_no =:mobileNo, Firstname=:firstName, Lastname=:lastName, Middlename=:middleName,  WhatsappNo=:whatsAppNo, Alternate_No=:alternateNo, email_id=:emailId,PAN_NO=:panNo, AADHAR_CARD_NO=:aadharCardNo,address1=:address1, address2=:address2,address3=:address3,Status=:status1 WHERE Customer_Id = :customerId");
			query2.setParameter("mobileNo", oldChassisValidateRequestModel.getMobileNo());
			query2.setParameter("firstName", oldChassisValidateRequestModel.getFirstName());
			query2.setParameter("lastName", oldChassisValidateRequestModel.getLastName());
			query2.setParameter("middleName", oldChassisValidateRequestModel.getMiddleName());
			query2.setParameter("whatsAppNo", oldChassisValidateRequestModel.getWhatsappNo());
			query2.setParameter("alternateNo", oldChassisValidateRequestModel.getAlternateNo());
			query2.setParameter("emailId", oldChassisValidateRequestModel.getEmailId());
			query2.setParameter("panNo", oldChassisValidateRequestModel.getPanNo());
			query2.setParameter("aadharCardNo", oldChassisValidateRequestModel.getAadharNo());
			query2.setParameter("address1", oldChassisValidateRequestModel.getAddress1());
			query2.setParameter("address2", oldChassisValidateRequestModel.getAddress2());
			query2.setParameter("address3", oldChassisValidateRequestModel.getAddress3());
			query2.setParameter("status1", oldChassisValidateRequestModel.getStatus1());
			query2.setParameter("customerId", oldChassisValidateRequestModel.getCustomerId());
			int CustomerHDREntity = query2.executeUpdate();
			
			Query query3 = session.createQuery("UPDATE CustomerDTLEntity SET DISTRICT_ID =:districtId, TEHSIL_ID=:tehsilId, VILLAGE_ID=:cityId, PINCODE=:pinId, STATE_ID=:stateId, COUNTRY=:countryId WHERE ID = :cid");
			query3.setParameter("districtId", oldChassisValidateRequestModel.getDistrictId());
			query3.setParameter("tehsilId", oldChassisValidateRequestModel.getTehsilId());
			query3.setParameter("cityId", oldChassisValidateRequestModel.getCityId());
			query3.setParameter("pinId", oldChassisValidateRequestModel.getPinId());
			query3.setParameter("stateId", oldChassisValidateRequestModel.getStateId());
			query3.setParameter("countryId", oldChassisValidateRequestModel.getCountryId());
			query3.setParameter("cid", oldChassisValidateRequestModel.getCid());
			int CustomerDTLEntity = query3.executeUpdate();
			
			Query query4 = session.createQuery("UPDATE CustomerItemDTLEntity SET  PC_Id=:pcId, Series_Id=:seriesId, Segment_Id=:segmentId, Mdoel_Id=:modelId, Variant_Id=:variantId, Item_Id=:itemId WHERE id = :id");
			query4.setParameter("pcId", oldChassisValidateRequestModel.getPcId());
			query4.setParameter("seriesId", oldChassisValidateRequestModel.getSeriesId());
			query4.setParameter("segmentId", oldChassisValidateRequestModel.getSegmentId());
			query4.setParameter("modelId", oldChassisValidateRequestModel.getModelId());
			query4.setParameter("variantId", oldChassisValidateRequestModel.getVariantId());
			query4.setParameter("itemId", oldChassisValidateRequestModel.getItemId());
			query4.setParameter("id", oldChassisValidateRequestModel.getId());
			int CustomerItemDTLEntity = query4.executeUpdate();
			
			
		if (isSuccess) {
			transaction.commit();
			session.close();
			//sessionFactory.close();
			responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			
			msg = "Validate Details Successfully";
			responseModel.setMsg(msg);
		}else if(isFailure){
			responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
			msg = "Resource Not Found";
		}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
	
      }
     return responseModel;
	}
}
