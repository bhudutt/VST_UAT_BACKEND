/**
 * 
 */
package com.hitech.dms.web.dao.enquiry.edit;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.utils.FileUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.common.SystemLookUpEntity;
import com.hitech.dms.web.entity.customer.CustomerAddDTLEntity;
import com.hitech.dms.web.entity.customer.CustomerHDREntity;
import com.hitech.dms.web.entity.enquiry.EnquiryAttachImagesEntity;
import com.hitech.dms.web.entity.enquiry.EnquiryCustCropEntity;
import com.hitech.dms.web.entity.enquiry.EnquiryCustFleetDTLEntity;
import com.hitech.dms.web.entity.enquiry.EnquiryCustSoilTypeEntity;
import com.hitech.dms.web.entity.enquiry.EnquiryExchangeDTLEntity;
import com.hitech.dms.web.entity.enquiry.EnquiryHdrEntity;
import com.hitech.dms.web.entity.oldvehicle.OldVehicleInvEntity;
import com.hitech.dms.web.model.enquiry.edit.request.CustomerHDREditRequestModel;
import com.hitech.dms.web.model.enquiry.edit.request.EnquiryEditAttachImagesRequestModel;
import com.hitech.dms.web.model.enquiry.edit.request.EnquiryEditRequestModel;
import com.hitech.dms.web.model.enquiry.edit.response.EnquiryEditResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class EnquiryEditDaoImpl implements EnquiryEditDao {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryEditDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private FileStorageProperties fileStorageProperties;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public EnquiryEditResponseModel updateEnquiry(String userCode, EnquiryEditRequestModel enquiryEditRequestModel,
			List<EnquiryEditAttachImagesRequestModel> enquiryAttachImgsList, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateEnquiry invoked.." + userCode);
			logger.debug(enquiryEditRequestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		EnquiryHdrEntity enquiryHdrEntity = null;
		CustomerHDREntity customerHDREntity = null;
		EnquiryEditResponseModel enquiryCreateResponseModel = new EnquiryEditResponseModel();
		CustomerHDREditRequestModel customerHDRRequestModel = enquiryEditRequestModel.getCustomerHDRRequestModel();
		List<EnquiryAttachImagesEntity> enquiryAttachImgsEList = null;
		List<EnquiryExchangeDTLEntity> enquiryExchangeDTLList = null;
		List<EnquiryCustSoilTypeEntity> enquiryCustSoilTypeList = null;
		List<EnquiryCustFleetDTLEntity> enquiryCustFleetDTLList = null;
		List<EnquiryCustCropEntity> enquiryCustCropList = null;
		boolean isSuccess = true;
		String sqlQuery = "Select BranchCode from ADM_BP_DEALER_BRANCH where branch_id =:branchId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			enquiryHdrEntity = mapper.map(enquiryEditRequestModel, EnquiryHdrEntity.class, "EnquiryEditMapId");
			System.out.println(enquiryHdrEntity.toString());

			BigInteger userId = null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				query = session.createNativeQuery(sqlQuery);
				query.setParameter("branchId", enquiryEditRequestModel.getBranchId());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					String branchCode = null;
					String customerProspectType = null;
					for (Object object : data) {
						Map row = (Map) object;
						branchCode = (String) row.get("BranchCode");
					}
					// current date
					Date currDate = new Date();
					sqlQuery = "Select * from SA_ENQ_HDR where enquiry_id =:enquiryHdrId";
					query = session.createNativeQuery(sqlQuery);
					query.setParameter("enquiryHdrId", enquiryHdrEntity.getEnquiryHdrId());
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					data = query.list();
					if (data != null && !data.isEmpty()) {
						EnquiryHdrEntity enquiryHdrDBEntity = new EnquiryHdrEntity();
						for (Object object : data) {
							Map row = (Map) object;
							enquiryHdrDBEntity.setEnquiryNo((String) row.get("enquiry_number"));
							enquiryHdrDBEntity.setEnquiryStatus((String) row.get("enquiry_status"));
						}
						// fetch Source Code to validate
						sqlQuery = "Select * from SA_MST_ENQ_SOURCE where Enq_Source_Id =:enqSourceId";
						query = session.createNativeQuery(sqlQuery);
						query.setParameter("enqSourceId", enquiryEditRequestModel.getEnquirySource());
						query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
						data = query.list();
						if (data != null && !data.isEmpty()) {
							String sourceCode = null;
							for (Object object : data) {
								Map row = (Map) object;
								sourceCode = (String) row.get("SourceCode");
							}
							if (sourceCode == null) {
								// Source Code is null
								enquiryCreateResponseModel.setMsg("Enquiry Source is Required");
								isSuccess = false;
							} else if (sourceCode != null && sourceCode.equals("ACTIVITY")) {
								// If Source of Enquiry is selected as FIELD ACTIVITY, then Field Activity
								// Details data will be displayed and user need to fill the details
								if (enquiryEditRequestModel.getActivitySourceID() == null) {
									// must not be null
									enquiryCreateResponseModel.setMsg("Field Activity Source is Required");
									isSuccess = false;
								}
								if (enquiryEditRequestModel.getActivityDate() == null) {
									// must not be null
									enquiryCreateResponseModel.setMsg("Field Activity Date is Required");
									isSuccess = false;
								} else if (enquiryEditRequestModel.getActivityDate().compareTo(currDate) > 0) {
									// must not be greater than Current date
									enquiryCreateResponseModel
											.setMsg("Field Activity Date must not be greater than Current Date.");
									isSuccess = false;
								}
								if (enquiryEditRequestModel.getActivityPlanHDRId() == null || enquiryEditRequestModel
										.getActivityPlanHDRId().compareTo(BigInteger.ZERO) <= 0) {
									// must not be null
									enquiryCreateResponseModel.setMsg("Field Activity Plan is Required");
									isSuccess = false;
								}
							} else if (sourceCode != null && sourceCode.equals("DIGITAL")) {
								if (enquiryEditRequestModel.getDigitalSourceId() == null) {
									// must not be null
									enquiryCreateResponseModel.setMsg("Digital Source is Required");
									isSuccess = false;
								} else if (enquiryEditRequestModel.getDigitalEnqDate() == null) {
									// must not be null
									enquiryCreateResponseModel.setMsg("Digital Enq. Date is Required");
									isSuccess = false;
								} else if (enquiryEditRequestModel.getDigitalValidationBy() == null
										|| enquiryEditRequestModel.getDigitalValidationBy().equals("")) {
									// must not be null
									enquiryCreateResponseModel.setMsg("Digital Validation By is Required");
									isSuccess = false;
								}
							}
							if (isSuccess) {
								// fetch Customer Details using mobile number
								mapData = fetchCustomerDTLBtMobileNo(session, customerHDRRequestModel.getMobileNo());
								if (mapData != null && mapData.get("SUCCESS") != null) {
									customerHDREntity = (CustomerHDREntity) mapData.get("customerHDREntity");
									List<CustomerAddDTLEntity> customerAddDTLList = null;
									CustomerAddDTLEntity customerAddDTLEntity = null;
									boolean isNewCust = false;
									String customerCode = null;
									if (customerHDREntity == null) {
										customerHDREntity = new CustomerHDREntity();
										customerAddDTLList = new ArrayList<CustomerAddDTLEntity>();
										customerAddDTLEntity = new CustomerAddDTLEntity();
										customerCode = (String) session
												.createSQLQuery("select Customer_Code from Generate_CustomerCodeNo()")
												.uniqueResult();
										isNewCust = true;
									} else {
										customerAddDTLList = customerHDREntity.getCustomerAddDTLList();
										customerProspectType = customerHDREntity.getProspectType();

										// validate Customer whether enquiry creation is avilable for him/her or not
										mapData = validateForCreatingEnqForExistingCust(session, userCode,
												enquiryHdrEntity.getBranchId(), enquiryHdrEntity.getModelId(),
												customerHDREntity.getCustomerId(), enquiryHdrEntity.getEnquiryHdrId());
										if (mapData != null && mapData.get("SUCCESS") != null) {
											String successFlag = (String) mapData.get("successFlag");
											if (successFlag == null || successFlag.equalsIgnoreCase("N")) {
												isSuccess = false;
												enquiryCreateResponseModel.setMsg((String) mapData.get("msg"));
											}
										} else {
											isSuccess = false;
											enquiryCreateResponseModel.setMsg((String) mapData.get("ERROR"));
										}
									}
									// if customer's prospectType is customer, thn no updation in customer master
									// if customer's prospectType is Prospect thn updation allowed
									// Or create New Customer
									if (((customerProspectType != null
											&& customerProspectType.equalsIgnoreCase(WebConstants.CUST_TYPE_PROSPECT))
											|| isNewCust) && isSuccess) {
										// setting fields value in customerHDREntity from customerHDRRequestModel
										customerHDREntity
												.setCustomerCategoryId(customerHDRRequestModel.getCustomerCategoryId());
										customerHDREntity.setAddress1(customerHDRRequestModel.getAddress1());
										customerHDREntity.setAddress2(customerHDRRequestModel.getAddress2());
										customerHDREntity.setAddress3(customerHDRRequestModel.getAddress3());
										customerHDREntity.setAlternateNo(customerHDRRequestModel.getAlternateNo());
										customerHDREntity
												.setOrganizationName(customerHDRRequestModel.getOrganizationName());
										customerHDREntity.setContactTitle(customerHDRRequestModel.getContactTitle());
										customerHDREntity.setProspectType(WebConstants.CUST_TYPE_PROSPECT);
										customerHDREntity.setFirstName(customerHDRRequestModel.getFirstName());
										customerHDREntity.setMiddleName(customerHDRRequestModel.getMiddleName());
										customerHDREntity.setLastName(customerHDRRequestModel.getLastName());
										customerHDREntity.setMobileNo(customerHDRRequestModel.getMobileNo());
										customerHDREntity.setWhatsAppNo(customerHDRRequestModel.getWhatsAppNo());
										customerHDREntity.setPhoneNo(customerHDRRequestModel.getPhoneNo());
										customerHDREntity.setEmailId(customerHDRRequestModel.getEmailId());
										customerHDREntity.setPinId(customerHDRRequestModel.getPinId());
										customerHDREntity.setDateOfBirth(customerHDRRequestModel.getDateOfBirth());
										customerHDREntity
												.setAnniversaryDate(customerHDRRequestModel.getAnniversaryDate());

										if (isNewCust) {
											mapData = fetchSysLookupDTLByCode(session, WebConstants.CUST_ADD_TYPE,
													WebConstants.REGISTERED);
											if (mapData != null && mapData.get("SUCCESS") != null) {
												SystemLookUpEntity systemLookUpEntity = (SystemLookUpEntity) mapData
														.get("systemLookUpEntity");
												customerAddDTLEntity.setAddressTypeID(systemLookUpEntity.getLookUpId());
											}
											customerAddDTLEntity.setContactNo(customerHDRRequestModel.getMobileNo());
											customerAddDTLEntity.setCreatedBy(userId);
											customerAddDTLEntity.setCreatedDate(currDate);
											customerAddDTLEntity.setCustAddLine1(customerHDRRequestModel.getAddress1());
											customerAddDTLEntity.setCustAddLine2(customerHDRRequestModel.getAddress2());
											customerAddDTLEntity.setCustAddLine3(customerHDRRequestModel.getAddress3());
											customerAddDTLEntity.setCustomerHdr(customerHDREntity);
											customerAddDTLEntity.setEmail(customerHDRRequestModel.getEmailId());
											customerAddDTLEntity.setGstNo(customerHDRRequestModel.getGstIN());

											customerHDREntity.setCustomerAddDTLList(customerAddDTLList);
										}

										// save customer if not exits
										if (isNewCust) {
											if (customerCode == null) {
												// customer code not generated
												enquiryCreateResponseModel
														.setMsg("Error While generating Customer Code.");
												isSuccess = false;
											} else {
												// set Customer code
												customerHDREntity.setCustomerCode(customerCode);
												customerHDREntity.setCreatedBy(userId);
												customerHDREntity.setCreatedDate(currDate);
												session.save(customerHDREntity);
											}
										} else {
											// merger Customer
											customerHDREntity.setModifiedBy(userId);
											customerHDREntity.setModifiedDate(currDate);

											session.merge(customerHDREntity);
										}

									}
								} else if (mapData.get("ERROR") != null) {
									// ERROR WHILE FETCHING CUSTOMER DTLs
									enquiryCreateResponseModel.setMsg("Error While Fetching Customer Details.");
									isSuccess = false;
								}
								if (isSuccess) {
									// set Customer in Enquiry
									enquiryHdrEntity.setCustomerId(customerHDREntity.getCustomerId());

									// get Exchange DTL List from view
									enquiryExchangeDTLList = enquiryHdrEntity.getEnquiryExchangeDTLList();
									if (enquiryHdrEntity.getIsExchangeRequired() != null
											&& enquiryHdrEntity.getIsExchangeRequired()) {
										if (enquiryExchangeDTLList != null && !enquiryExchangeDTLList.isEmpty()) {
											
											// Delete existing records from both tables
										    deleteExistingExchangeRecords(session, enquiryHdrEntity.getEnquiryHdrId());
											
											for (EnquiryExchangeDTLEntity enquiryExchangeDTLEntity : enquiryExchangeDTLList) {
												 enquiryExchangeDTLEntity.setEnquiryHdr(enquiryHdrEntity);
										            enquiryExchangeDTLEntity.setInvInDate(currDate);
										            enquiryExchangeDTLEntity.setCreatedBy(userId);
										            enquiryExchangeDTLEntity.setCreatedDate(currDate);
										            enquiryExchangeDTLEntity.setEnquiryExcDTLId(null); // Ensure it's treated as new record
												if (enquiryExchangeDTLEntity.getMachineReceived() != null
														&& enquiryExchangeDTLEntity.getMachineReceived()) {
													// insert into old vehicle inventory table
													mapData = saveOldVehilceInventory(session, userId, enquiryHdrEntity,
															enquiryExchangeDTLEntity);
													if (mapData != null && mapData.get("SUCCESS") != null) {
														// inserted
													} else if (mapData.get("ERROR") != null) {
														// ERROR WHILE Inserting Data into Old Veh Inv Table.
														enquiryCreateResponseModel
																.setMsg((String) mapData.get("ERROR"));
														isSuccess = false;
														break;
													}
												} else {
													enquiryExchangeDTLEntity.setMachineReceived(false);
													;
												}
											}
										}
									} else {
										// Case 2: Exchange is not required - Delete all existing records from both tables
									    deleteExistingExchangeRecords(session, enquiryHdrEntity.getEnquiryHdrId());
									    
									    // Clear the list in entity
									    enquiryHdrEntity.setEnquiryExchangeDTLList(null);
									}
									if (isSuccess) {
										enquiryCustSoilTypeList = enquiryHdrEntity.getEnquiryCustSoilTypeList();
										if (enquiryCustSoilTypeList != null && !enquiryCustSoilTypeList.isEmpty()) {
											for (EnquiryCustSoilTypeEntity enquiryCustSoilTypeEntity : enquiryCustSoilTypeList) {
												enquiryCustSoilTypeEntity.setEnquiryHdr(enquiryHdrEntity);
												if (enquiryCustSoilTypeEntity.getDeleteFlag() == null) {
													enquiryCustSoilTypeEntity.setDeleteFlag(false);
												}
												enquiryCustSoilTypeEntity.setLastupdatedon(currDate);
											}
										}
										enquiryCustFleetDTLList = enquiryHdrEntity.getEnquiryCustFleetDTLList();
										if (enquiryCustFleetDTLList != null && !enquiryCustFleetDTLList.isEmpty()) {
											for (EnquiryCustFleetDTLEntity enquiryCustFleetDTLEntity : enquiryCustFleetDTLList) {
												if (enquiryCustFleetDTLEntity.getDeleteFlag() == null) {
													enquiryCustFleetDTLEntity.setDeleteFlag(false);
												}
												enquiryCustFleetDTLEntity.setLastupdatedon(currDate);
												enquiryCustFleetDTLEntity.setEnquiryHdr(enquiryHdrEntity);
											}
										}
										enquiryCustCropList = enquiryHdrEntity.getEnquiryCustCropList();
										if (enquiryCustCropList != null && !enquiryCustCropList.isEmpty()) {
											for (EnquiryCustCropEntity custCropEntity : enquiryCustCropList) {
												if (custCropEntity.getDeleteFlag() == null) {
													custCropEntity.setDeleteFlag(false);
												}
												custCropEntity.setLastupdatedon(currDate);
												custCropEntity.setEnquiryHdr(enquiryHdrEntity);
											}
										}
										enquiryHdrEntity.setModifiedBy(userId);
										enquiryHdrEntity.setModifiedDate(currDate);
										// set Enq Status
										if (device != null) {
											if (device.isMobile()) {
											} else if (device.isNormal()) {
												if (enquiryHdrEntity.getEnquiryStatus() == null) {
													enquiryHdrEntity
															.setEnquiryStatus(enquiryHdrDBEntity.getEnquiryStatus());
												}
											} else if (device.isTablet()) {
											}
										}
										if (enquiryHdrDBEntity.getEnquiryStatus() != null && enquiryHdrDBEntity
												.getEnquiryStatus().equalsIgnoreCase(WebConstants.DIGITAL_OPEN)) {
											enquiryHdrEntity.setEnquiryStatus(WebConstants.PENDING_FOR_VST_VALIDATION);
										}
										if (enquiryHdrEntity.getEnquiryNo() == null) {
											enquiryHdrEntity.setEnquiryNo(enquiryHdrDBEntity.getEnquiryNo());
										}
										session.saveOrUpdate(enquiryHdrEntity);

										// upload Enquiry Files
//										enquiryAttachImgsEList = enquiryHdrEntity.getEnquiryAttachImgsList();
										// db image files
//										List<EnquiryAttachImagesEntity> enquiryAttachImgsDBList = enquiryHdrDBEntity
//												.getEnquiryAttachImgsList();
										if (enquiryAttachImgsList != null && !enquiryAttachImgsList.isEmpty()) {
											enquiryAttachImgsEList = new ArrayList<EnquiryAttachImagesEntity>();
//											if (enquiryAttachImgsDBList != null && !enquiryAttachImgsDBList.isEmpty()) {
											// delete from table
//												session.delete(enquiryAttachImgsDBList);
											sqlQuery = "delete from SA_ENQ_ATTACH_IMAGES where enquiry_id =:enquiryId";
											query = session.createNativeQuery(sqlQuery);
											query.setParameter("enquiryId", enquiryHdrEntity.getEnquiryHdrId());
											query.executeUpdate();
//											}
											String enqFilePath = fileStorageProperties.getUploadDirMain()
													+ messageSource.getMessage("enq.file.upload.dir",
															new Object[] { enquiryHdrEntity.getEnquiryHdrId() },
															LocaleContextHolder.getLocale());
											for (EnquiryEditAttachImagesRequestModel attachImagesModel : enquiryAttachImgsList) {
												Map<String, String> fileMapData = FileUtils
														.uploadDOCDTLDOCS(enqFilePath, attachImagesModel.getFile());
												if (fileMapData != null && fileMapData.get("MSG").toString()
														.equals(WebConstants.SUCCESS)) {
													EnquiryAttachImagesEntity attachImagesEntity = new EnquiryAttachImagesEntity();

													attachImagesEntity.setFile_name(fileMapData.get("fileName"));
													attachImagesEntity.setEnquiryHdr(enquiryHdrEntity);
													
													enquiryAttachImgsEList.add(attachImagesEntity);
												}
											}

											if (enquiryAttachImgsEList != null && !enquiryAttachImgsEList.isEmpty()) {
												enquiryHdrEntity.setEnquiryAttachImgsList(enquiryAttachImgsEList);
											}

											session.merge(enquiryHdrEntity);
										}
									}
								}
							}
						} else {
							// Enq Source Code not found
							enquiryCreateResponseModel.setMsg("Enquiry Source Not Found.");
							isSuccess = false;
						}
					} else {
						// enquiry Id not found
						enquiryCreateResponseModel.setMsg("Enquiry Not Found.");
						isSuccess = false;
					}
				} else {
					// branch id not found
					enquiryCreateResponseModel.setMsg("Branch Not Found.");
					isSuccess = false;
				}
			} else {
				// user not found
				enquiryCreateResponseModel.setMsg("User Not Found.");
				isSuccess = false;
			}
			if (isSuccess) {
				transaction.commit();
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess && enquiryHdrEntity.getEnquiryHdrId() != null) {
				mapData = fetchEnqNoByEnqHDRId(enquiryHdrEntity.getEnquiryHdrId());
				enquiryCreateResponseModel.setEnquiryNo(enquiryHdrEntity.getEnquiryNo());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					enquiryCreateResponseModel.setEnquiryNo((String) mapData.get("enqNo"));
				}
				enquiryCreateResponseModel.setEnquiryHdrId(enquiryHdrEntity.getEnquiryHdrId());
				enquiryCreateResponseModel.setMsg("Enquiry Updated Successful.");
				enquiryCreateResponseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				if (enquiryCreateResponseModel.getMsg() == null) {
					enquiryCreateResponseModel.setMsg("Error While Updating Enquiry.");
				}
				enquiryCreateResponseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return enquiryCreateResponseModel;
	}

	@SuppressWarnings("deprecation")
	public Map<String, Object> fetchCustomerDTLBtMobileNo(Session session, String mobileNo) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		CustomerHDREntity customerHDREntity = null;
		Query query = null;
		String sqlQuery = "Select TOP 1 * from CM_CUST_HDR (nolock) ch where ch.mobile_no =:mobileNo order by Customer_Id desc";
		try {
			query = session.createNativeQuery(sqlQuery).addEntity(CustomerHDREntity.class);
			query.setParameter("mobileNo", mobileNo);
			customerHDREntity = (CustomerHDREntity) query.uniqueResult();
			mapData.put("customerHDREntity", customerHDREntity);
			mapData.put("SUCCESS", "FETCHED");
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING CUSTOMER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING CUSTOMER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}
	
	
	// Helper method to delete existing records from both tables
	private void deleteExistingExchangeRecords(Session session, BigInteger enquiryId) {
	    try {
	        // Delete from SA_OLD_VEHICLE_INV first (foreign key dependency)
	        String deleteOldVehQuery = "DELETE FROM SA_OLD_VEHICLE_INV WHERE enquiry_id = :enquiryId";
	        Query oldVehQuery = session.createNativeQuery(deleteOldVehQuery);
	        oldVehQuery.setParameter("enquiryId", enquiryId);
	        oldVehQuery.executeUpdate();
	        
	        // Delete from SA_ENQ_EXCHANGE_DTL
	        String deleteExchangeQuery = "DELETE FROM SA_ENQ_EXCHANGE_DTL WHERE enquiry_id = :enquiryId";
	        Query exchangeQuery = session.createNativeQuery(deleteExchangeQuery);
	        exchangeQuery.setParameter("enquiryId", enquiryId);
	        exchangeQuery.executeUpdate();
	        
	    } catch (Exception e) {
	        // Log error and handle appropriately
	        throw new RuntimeException("Error deleting existing exchange records: " + e.getMessage(), e);
	    }
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select user_id from ADM_USER (nolock) u where u.UserCode =:userCode";
		mapData.put("ERROR", "USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger userId = null;
				for (Object object : data) {
					Map row = (Map) object;
					userId = (BigInteger) row.get("user_id");
				}
				mapData.put("userId", userId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> validateForCreatingEnqForExistingCust(Session session, String userCode,
			BigInteger branchId, BigInteger modelId, BigInteger customerId, BigInteger enquiryId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "exec [SP_ENQ_VALIDATIONFOREXISTINGCUST] :userCode, :branchId, :modelId, :customerId, :enquiryId";
		mapData.put("ERROR", "Error While Validating Enquiry Creation For Existing Customer");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("branchId", branchId);
			query.setParameter("modelId", modelId);
			query.setParameter("customerId", customerId);
			query.setParameter("enquiryId", enquiryId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String msg = null;
				String successFlag = null;
				for (Object object : data) {
					Map row = (Map) object;
					msg = (String) row.get("msg");
					successFlag = (String) row.get("successFlag");
				}
				mapData.put("msg", msg == null ? "Error" : msg);
				mapData.put("successFlag", successFlag == null ? "N" : successFlag);
				mapData.put("SUCCESS", "VALIDATED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "Error While Validating Enquiry Creation For Existing Customer");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "Error While Validating Enquiry Creation For Existing Customer");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchSysLookupDTLByCode(Session session, String lookupTypeCode, String lookupVal) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		SystemLookUpEntity systemLookUpEntity = null;
		Query query = null;
		String sqlQuery = "Select * from SYS_LOOKUP (nolock) s where s.LookupTypeCode =:lookupTypeCode and LookupVal =:lookupVal";
		mapData.put("ERROR", "SYS-LOOKUP DTL Not Found.");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("lookupTypeCode", lookupTypeCode);
			query.setParameter("lookupVal", lookupVal);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				systemLookUpEntity = new SystemLookUpEntity();
				for (Object object : data) {
					Map row = (Map) object;
					systemLookUpEntity.setLookUpId((BigInteger) row.get("lookup_id"));
					systemLookUpEntity.setLookTypeText((String) row.get("LookupText"));
					systemLookUpEntity.setLookupVal((String) row.get("LookupVal"));
				}
				mapData.put("systemLookUpEntity", systemLookUpEntity);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING SYSLOOKUP DETAIL");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING SYSLOOKUP DETAIL");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchEnqNoByEnqHDRId(BigInteger enqHDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select enquiry_number from SA_ENQ_HDR (nolock) e where e.enquiry_id =:enqHDRId";
		mapData.put("ERROR", "Enquiry Details Not Found");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("enqHDRId", enqHDRId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String enqNo = null;
				for (Object object : data) {
					Map row = (Map) object;
					enqNo = (String) row.get("enquiry_number");
				}
				mapData.put("enqNo", enqNo);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ENQUIRY DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ENQUIRY DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> saveOldVehilceInventory(Session session, BigInteger userid,
			EnquiryHdrEntity enquiryHdrEntity, EnquiryExchangeDTLEntity enquiryExchangeDTLEntity) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		OldVehicleInvEntity oldVehicleInvEntity = null;
		Query query = null;
		String sqlQuery = "Select * from SA_OLD_VEHICLE_INV (nolock) s where s.Branch_Id =:branchId and s.enquiry_id =:enquiryId and s.brand_id =:brandId";
		mapData.put("ERROR", "Error While Uploading Old Vehicle Inventory.");
		try {
			query = session.createNativeQuery(sqlQuery).addEntity(OldVehicleInvEntity.class);
			query.setParameter("branchId", enquiryHdrEntity.getBranchId());
			query.setParameter("enquiryId", enquiryHdrEntity.getEnquiryHdrId());
			query.setParameter("brandId", enquiryExchangeDTLEntity.getBrandId());
			OldVehicleInvEntity oldVehicleInvDBEntity = (OldVehicleInvEntity) query.uniqueResult();
			//if (oldVehicleInvDBEntity != null) {
			if (false) {
				// update
				// nothing need to do
			} else {
				// insert
				oldVehicleInvEntity = new OldVehicleInvEntity();
				oldVehicleInvEntity.setBranchId(enquiryHdrEntity.getBranchId());
				oldVehicleInvEntity.setBrandId(enquiryExchangeDTLEntity.getBrandId());
				oldVehicleInvEntity.setBrandName("");
				oldVehicleInvEntity.setEnquiryId(enquiryHdrEntity.getEnquiryHdrId());
				oldVehicleInvEntity.setEstimatedExchnagePrice(enquiryExchangeDTLEntity.getEstimatedExchangePrice());
				oldVehicleInvEntity.setInvInDate(new Date());
				oldVehicleInvEntity.setModelName(enquiryExchangeDTLEntity.getModelName());
				oldVehicleInvEntity.setModelYear(enquiryExchangeDTLEntity.getModelYear());
				oldVehicleInvEntity.setStatus(WebConstants.AVAILABLE);
				oldVehicleInvEntity.setCreatedBy(userid);
				oldVehicleInvEntity.setCreatedDate(new Date());
				session.save(oldVehicleInvEntity);
			}
			mapData.put("SUCCESS", "INSERTED");
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "Error While Uploading Old Vehicle Inventory.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "Error While Uploading Old Vehicle Inventory.");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}
}
