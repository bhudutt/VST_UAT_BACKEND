package com.hitech.dms.web.service.updateoldchassis;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.Transaction;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.updateoldchassis.UpdateOldChassisDao;
import com.hitech.dms.web.entity.customer.CustomerDTLEntity;
import com.hitech.dms.web.entity.customer.CustomerHDREntity;
import com.hitech.dms.web.entity.customer.CustomerItemDTLEntity;
import com.hitech.dms.web.entity.customer.VinMasterEntity;
import com.hitech.dms.web.model.updateoldchassis.UpdateOldChassisRequest;
import com.hitech.dms.web.model.updateoldchassis.VinAndCustDtlResponse;

@Service
public class UpdateOldChassisServiceImpl implements UpdateOldChassisService{
	private static final Logger logger = LoggerFactory.getLogger(UpdateOldChassisServiceImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private UpdateOldChassisDao updateOldChassisDao;
	
	
	@Override
	public ApiResponse<?> autoSearchChassisNo(String chassisNo) {
	    if (logger.isDebugEnabled()) {
	        logger.debug("autoSearchChassisNo invoked for chassisNo: " + chassisNo);
	    }
	    
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = new ApiResponse<>();
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();
	        data = updateOldChassisDao.autoSearchChassisNo(session, chassisNo);

	        if (data != null && !data.isEmpty()) {
	            apiResponse.setResult(data);
	            apiResponse.setMessage("Auto Search Chassis No Successfully.");
	            apiResponse.setStatus(WebConstants.STATUS_OK_200);
	        } else {
//	            apiResponse.setResult(Collections.emptyList());
	            apiResponse.setMessage("No data found for the given chassis number.");
	            apiResponse.setStatus(WebConstants.STATUS_NO_CONTENT_204); // or another appropriate status
	        }

	    } catch (SQLGrammarException exp) {
	        String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
	        logger.error(errorMessage, exp);
	        apiResponse.setMessage("Database error occurred.");
	        apiResponse.setStatus(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	    } catch (HibernateException exp) {
	        String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
	        logger.error(errorMessage, exp);
	        apiResponse.setMessage("Database error occurred.");
	        apiResponse.setStatus(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	    } catch (Exception exp) {
	        String errorMessage = "An unexpected error occurred: " + exp.getMessage();
	        logger.error(errorMessage, exp);
	        apiResponse.setMessage("An unexpected error occurred.");
	        apiResponse.setStatus(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}

	
	
	@Override
	public ApiResponse<VinAndCustDtlResponse> fetchVinAndCustDetails(String chassisNo) {
	    ApiResponse<VinAndCustDtlResponse> apiResponse = new ApiResponse<>();

	    try (Session session = sessionFactory.openSession()) { // Use try-with-resources for session
	        List<VinAndCustDtlResponse> data = updateOldChassisDao.fetchVinAndCustDetails(session, chassisNo);

	        if (data != null && !data.isEmpty()) {
	            @SuppressWarnings("rawtypes")
	            Map row = (Map) data.get(0); // Get the first (and only) result
	            VinAndCustDtlResponse responseModel = new VinAndCustDtlResponse();
	            responseModel.setVinId((BigInteger)row.get("vinId"));
	            responseModel.setPcId((Integer)row.get("pcId"));
	            responseModel.setPcDesc((String)row.get("pcDesc"));
	            responseModel.setChassisNo((String)row.get("chassisNo"));
	            responseModel.setEngineNo((String)row.get("engineNo"));
	            responseModel.setVinNo((String)row.get("vinNo"));
	            responseModel.setRegistrationNo((String)row.get("registrationNo"));
	            responseModel.setSaleDate((String)row.get("saleDate"));
	            responseModel.setModelId((BigInteger)row.get("modelId"));
	            responseModel.setModelName((String)row.get("modelName"));
	            responseModel.setSeriesName((String)row.get("seriesName"));
	            responseModel.setSegmentName((String)row.get("segmentName"));
	            responseModel.setItemId((BigInteger)row.get("itemId"));
	            responseModel.setItemNo((String)row.get("itemNo"));
	            responseModel.setItemDesc((String)row.get("itemDesc"));
	            responseModel.setVariant((String)row.get("variant"));
	            responseModel.setCustomerId((BigInteger)row.get("customerId"));
	            responseModel.setMobileNo((String)row.get("mobileNo"));
	            responseModel.setFirstName((String)row.get("firstName"));
	            responseModel.setMiddleName((String)row.get("middleName"));
	            responseModel.setLastName((String)row.get("lastName"));
	            responseModel.setWhatsappNo((String)row.get("whatsappNo"));
	            responseModel.setAlternateNo((String)row.get("alternateNo"));
	            responseModel.setEmailId((String)row.get("emailId"));
	            responseModel.setPanNo((String)row.get("panNo"));
	            responseModel.setAadharNo((String)row.get("aadharNo"));
	            responseModel.setAddress1((String)row.get("address1"));
	            responseModel.setAddress2((String)row.get("address2"));
	            responseModel.setAddress3((String)row.get("address3"));
	            responseModel.setDistrictId((BigInteger)row.get("districtId"));
	            responseModel.setDistrict((String)row.get("district"));
	            responseModel.setTehsilId((BigInteger)row.get("tehsilId"));
	            responseModel.setTehsil((String)row.get("tehsil"));
	            responseModel.setCityId((BigInteger)row.get("cityId"));
	            responseModel.setCity((String)row.get("city"));
	            responseModel.setPinId((BigInteger)row.get("pinId"));
	            responseModel.setPinName((String)row.get("pinName"));
	            responseModel.setStateId((BigInteger)row.get("stateId"));
	            responseModel.setState((String)row.get("state"));
	            responseModel.setCountryId((BigInteger)row.get("countryId"));
	            responseModel.setCountry((String)row.get("country"));

	            apiResponse.setResult(responseModel); // Set the single result in the ApiResponse
	            apiResponse.setStatus(WebConstants.STATUS_OK_200);
	        } else {
	        	apiResponse.setStatus(WebConstants.STATUS_NO_CONTENT_204);
	            apiResponse.setMessage("No data found."); // Handle the case when no data is returned
	        }
	    } catch (HibernateException exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<?> updateOldChassis(String userCode, UpdateOldChassisRequest request) throws HibernateException {
		if (logger.isDebugEnabled()) {
			logger.debug("saveBinToBinTransfer invoked.." + userCode);
			logger.debug(request.toString());
		}
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        Transaction transaction = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            
            System.out.println(request);
            
            String chassisNo = request.getVinMasterEntity().getChassisNo();
            System.out.print(chassisNo);


         // Retrieve the VIN master entity based on chassisNo
//            VinMasterEntity existingVin = session.get(VinMasterEntity.class, request.getVinMasterEntity().getVinId());
            VinMasterEntity existingVin = session.createQuery(
            	    "FROM VinMasterEntity WHERE chassisNo = :chassisNo", VinMasterEntity.class)
            	    .setParameter("chassisNo", chassisNo)
            	    .uniqueResult();
            if (existingVin != null) {
                // Optionally update the VIN master entity
            	existingVin.setVinNo(request.getVinMasterEntity().getVinNo());
            	existingVin.setEngineNo(request.getVinMasterEntity().getEngineNo());
            	existingVin.setRegistrationNumber(request.getVinMasterEntity().getRegistrationNumber());
            	existingVin.setMachineItemId(request.getVinMasterEntity().getMachineItemId());
//            	existingVin.setVinNo(request.getVinMasterEntity().getVinNo());
//            	existingVin.setVinNo(request.getVinMasterEntity().getVinNo());
//            	existingVin.setVinNo(request.getVinMasterEntity().getVinNo());
//            	existingVin.setVinNo(request.getVinMasterEntity().getVinNo());
//            	existingVin.setVinNo(request.getVinMasterEntity().getVinNo());
//            	existingVin.setVinNo(request.getVinMasterEntity().getVinNo());
                session.update(existingVin);
                
                // Retrieve customerId from the existing VIN master entity
                BigInteger customerId = existingVin.getCustomermstId(); // Assuming this field exists

                // Check if customer header exists
                CustomerHDREntity existingHeader = session.get(CustomerHDREntity.class, customerId);
                if (existingHeader != null) {
                    // Update the existing customer header entity
//                    existingHeader.setCustomerData(request.getCustomerHDREntity().getCustomerData()); // Update fields as necessary
                	existingHeader.setFirstName(request.getCustomerHDREntity().getFirstName());
                	existingHeader.setMiddleName(request.getCustomerHDREntity().getMiddleName());
                	existingHeader.setLastName(request.getCustomerHDREntity().getLastName());
                	existingHeader.setMobileNo(request.getCustomerHDREntity().getMobileNo());
                	existingHeader.setAlternateNo(request.getCustomerHDREntity().getAlternateNo());
                	existingHeader.setWhatsAppNo(request.getCustomerHDREntity().getWhatsAppNo());
                	existingHeader.setEmailId(request.getCustomerHDREntity().getEmailId());
                	existingHeader.setAddress1(request.getCustomerHDREntity().getAddress1());
                	existingHeader.setAddress2(request.getCustomerHDREntity().getAddress2());
                	existingHeader.setAddress3(request.getCustomerHDREntity().getAddress3());
                	existingHeader.setPanNo(request.getCustomerHDREntity().getPanNo());
                	existingHeader.setAadharCardNo(request.getCustomerHDREntity().getAadharCardNo());
                	existingHeader.setModifiedBy(request.getCustomerHDREntity().getModifiedBy());
                	existingHeader.setModifiedDate(request.getCustomerHDREntity().getModifiedDate());
                    session.update(existingHeader);
                } else {
                    throw new Exception("Customer header not found.");
                }

             // Check if customer details exist based on CUST_HDR_ID
                List<CustomerDTLEntity> existingDetailsList = session.createQuery(
                    "FROM CustomerDTLEntity WHERE CUST_HDR_ID = :customerId", 
                    CustomerDTLEntity.class
                ).setParameter("customerId", customerId).list();

                if (!existingDetailsList.isEmpty()) {
                    // Update each existing customer details entity
                    for (CustomerDTLEntity existingDetails : existingDetailsList) {
                        existingDetails.setDistrictId(request.getCustomerDTLEntity().getDistrictId()); // Update fields as necessary
                        existingDetails.setTehsilId(request.getCustomerDTLEntity().getTehsilId()); 
                        existingDetails.setVillageId(request.getCustomerDTLEntity().getVillageId()); 
                        existingDetails.setStateId(request.getCustomerDTLEntity().getStateId());  
                        existingDetails.setCountryId(request.getCustomerDTLEntity().getCountryId()); 
                        existingDetails.setPinCode(request.getCustomerDTLEntity().getPinCode()); 
                        session.update(existingDetails);
                    }
                } else {
                    throw new Exception("Customer details not found.");
                }


             // Check if customer item details exist based on cust_hdr_id
                List<CustomerItemDTLEntity> existingItemDtls = session.createQuery(
                    "FROM CustomerItemDTLEntity WHERE cust_hdr_id = :customerId", 
                    CustomerItemDTLEntity.class
                ).setParameter("customerId", customerId).list();

                if (!existingItemDtls.isEmpty()) {
                    // Update each existing customer item details entity
                    for (CustomerItemDTLEntity existingItemDtl : existingItemDtls) {
                        existingItemDtl.setPcId(request.getCustomerItemDTLEntity().getPcId()); // Update fields as necessary
                        existingItemDtl.setSeriesId(request.getCustomerItemDTLEntity().getSeriesId());
                        existingItemDtl.setSegmentId(request.getCustomerItemDTLEntity().getSegmentId());
                        existingItemDtl.setModelId(request.getCustomerItemDTLEntity().getModelId());
                        existingItemDtl.setVariantId(request.getCustomerItemDTLEntity().getVariantId());
                        existingItemDtl.setItemId(request.getCustomerItemDTLEntity().getItemId());
                        session.update(existingItemDtl);
                    }
                } else {
                    throw new Exception("Customer item details not found.");
                }

            } else {
                throw new Exception("VIN master not found.");
            }


            // If all checks pass, proceed with updates
//            session.update(request.getVinMstEntity());
//            session.update(request.getCustomerHDREntity());
//            session.update(request.getCustomerDTLEntity());
//            session.update(request.getCustomerItemDTLEntity());

            // Commit the transaction
            transaction.commit();

            apiResponse.setMessage("Data updated successfully.");
            apiResponse.setStatus(WebConstants.STATUS_OK_200);
        } catch (Exception exp) {
            if (transaction != null) {
                transaction.rollback();
            }
            apiResponse.setMessage(exp.getMessage());
            apiResponse.setStatus(WebConstants.STATUS_NO_CONTENT_204);
            logger.error(exp.getMessage(), exp);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return apiResponse;
    }


}
