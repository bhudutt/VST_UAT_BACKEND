/**
 * 
 */
package com.hitech.dms.web.dao.enquiry.create;

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
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.config.publisher.PublishModel;
import com.hitech.dms.app.utils.CommonUtils;
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
import com.hitech.dms.web.model.enquiry.create.request.CustomerHDRRequestModel;
import com.hitech.dms.web.model.enquiry.create.request.EnquiryAttachImagesRequestModel;
import com.hitech.dms.web.model.enquiry.create.request.EnquiryCreateRequestModel;
import com.hitech.dms.web.model.enquiry.create.response.EnquiryCreateResponseModel;

import reactor.core.publisher.Mono;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class EnquiryCreateDaoImpl implements EnquiryCreateDao {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryCreateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private FileStorageProperties fileStorageProperties;
//	@Autowired
//	private TopicExchange senderCreateEnqTopicExchange;
	@Autowired
	private CommonUtils commonUtils;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public EnquiryCreateResponseModel createEnquiry(String userCode,
			EnquiryCreateRequestModel enquiryCreateRequestModel,
			List<EnquiryAttachImagesRequestModel> enquiryAttachImgsList, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("createEnquiry invoked.." + userCode);
			logger.debug(enquiryCreateRequestModel.toString());
		}
		System.out.println("Request:::::::::::::::::::"+enquiryCreateRequestModel.toString());
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		EnquiryHdrEntity enquiryHdrEntity = null;
		CustomerHDREntity customerHDREntity = null;
//		List<EnquiryFollowupEntity> enquiryFollowUpList = null;
//		EnquiryFollowupEntity enquiryFollowupEntity = null;
		if(enquiryCreateRequestModel.getActivitySourceID() !=null && enquiryCreateRequestModel.getActivitySourceID() == 0) {
			enquiryCreateRequestModel.setActivitySourceID(null);
		}
		EnquiryCreateResponseModel enquiryCreateResponseModel = new EnquiryCreateResponseModel();
		CustomerHDRRequestModel customerHDRRequestModel = enquiryCreateRequestModel.getCustomerHDRRequestModel();
		List<EnquiryAttachImagesEntity> enquiryAttachImgsEList = null;
		List<EnquiryExchangeDTLEntity> enquiryExchangeDTLList = null;
		List<EnquiryCustSoilTypeEntity> enquiryCustSoilTypeList = null;
		List<EnquiryCustFleetDTLEntity> enquiryCustFleetDTLList = null;
		List<EnquiryCustCropEntity> enquiryCustCropList = null;
		boolean isSuccess = true;
		BigInteger generatedId=null;
		String sqlQuery = "Select BranchCode from ADM_BP_DEALER_BRANCH where branch_id =:branchId";
//		String customerMobileNoValidationUrl = "http://sales-server/sales-api/sales/find/name/by-id?id";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			enquiryHdrEntity = mapper.map(enquiryCreateRequestModel, EnquiryHdrEntity.class, "EnquiryMapId");
			System.out.println(enquiryHdrEntity.toString());

			BigInteger userId = null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				query = session.createNativeQuery(sqlQuery);
				query.setParameter("branchId", enquiryCreateRequestModel.getBranchId());
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
					// set Temp Enq No.
					enquiryHdrEntity.setEnquiryNo("DRA" + branchCode + currDate.getTime());
					// fetch Source Code to validate
					sqlQuery = "Select * from SA_MST_ENQ_SOURCE where Enq_Source_Id =:enqSourceId";
					query = session.createNativeQuery(sqlQuery);
					query.setParameter("enqSourceId", enquiryCreateRequestModel.getEnquirySource());
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
							if (enquiryCreateRequestModel.getActivitySourceID() == null) {
								// must not be null
								enquiryCreateResponseModel.setMsg("Field Activity Source is Required");
								isSuccess = false;
							}
							if (enquiryCreateRequestModel.getActivityDate() == null) {
								// must not be null
								enquiryCreateResponseModel.setMsg("Field Activity Date is Required");
								isSuccess = false;
							} else if (enquiryCreateRequestModel.getActivityDate().compareTo(currDate) > 0) {
								// must not be greater than Current date
								enquiryCreateResponseModel
										.setMsg("Field Activity Date must not be greater than Current Date.");
								isSuccess = false;
							}
							if (enquiryCreateRequestModel.getActivityPlanHDRId() == null || enquiryCreateRequestModel
									.getActivityPlanHDRId().compareTo(BigInteger.ZERO) <= 0) {
								// must not be null
								enquiryCreateResponseModel.setMsg("Field Activity Plan is Required");
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
										System.out.println("successflag "+(String) mapData.get("successFlag"));
										System.out.println("msg "+(String) mapData.get("msg"));
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
								System.out.println("customerProspectType "+customerProspectType);
								System.out.println("isNewCust "+isNewCust);
								System.out.println("isSuccess "+isSuccess);
								if (((customerProspectType != null
										&& customerProspectType.equalsIgnoreCase(WebConstants.CUST_TYPE_PROSPECT))
										|| isNewCust) && isSuccess) {
									// setting fields value in customerHDREntity from customerHDRRequestModel
									if (customerHDRRequestModel.getAddress1() == null) {
										customerHDRRequestModel.setAddress1("");
									}
									customerHDREntity
											.setCustomerCategoryId(customerHDRRequestModel.getCustomerCategoryId());
									customerHDREntity.setAddress1(customerHDRRequestModel.getAddress1());
									customerHDREntity.setAddress2(customerHDRRequestModel.getAddress2());
									customerHDREntity.setAddress3(customerHDRRequestModel.getAddress3());
									customerHDREntity.setAlternateNo(customerHDRRequestModel.getAlternateNo());
									customerHDREntity
											.setOrganizationName(customerHDRRequestModel.getOrganizationName());
									customerHDREntity
											.setContactTitle(customerHDRRequestModel.getContactTitle() == null ? "Mr."
													: customerHDRRequestModel.getContactTitle());
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
									customerHDREntity.setAnniversaryDate(customerHDRRequestModel.getAnniversaryDate());
									customerHDREntity.setOccupationID(customerHDRRequestModel.getOccupationID());
									customerHDREntity.setLandInAcres(customerHDRRequestModel.getLandInAcres());

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
										customerAddDTLEntity.setPinId(customerHDRRequestModel.getPinId());

										customerAddDTLList.add(customerAddDTLEntity);
										customerHDREntity.setCustomerAddDTLList(customerAddDTLList);
									}

									// save customer if not exits
									if (isNewCust) {
										if (customerCode == null) {
											// customer code not generated
											enquiryCreateResponseModel.setMsg("Error While generating Customer Code.");
											isSuccess = false;
										} else {
											// set Customer code
											customerHDREntity.setCustomerCode(customerCode);
											customerHDREntity.setCreatedBy(userId);
											customerHDREntity.setCreatedDate(currDate);
											if (customerHDREntity.getContactTitle() == null) {
												mapData = fetchSysLookupDTLByCode(session, WebConstants.TITLE,
														WebConstants.MR);
												if (mapData != null && mapData.get("SUCCESS") != null) {
													SystemLookUpEntity systemLookUpEntity = (SystemLookUpEntity) mapData
															.get("systemLookUpEntity");
													customerHDREntity
															.setContactTitle(systemLookUpEntity.getLookupVal());
												}
											}
											if (customerHDREntity.getCustomerCategoryId() == null) {
												mapData = fetchSysLookupDTLByCode(session, WebConstants.CUST_CTGRY,
														WebConstants.INDIVIDUAL);
												if (mapData != null && mapData.get("SUCCESS") != null) {
													SystemLookUpEntity systemLookUpEntity = (SystemLookUpEntity) mapData
															.get("systemLookUpEntity");
													customerHDREntity
															.setCustomerCategoryId(systemLookUpEntity.getLookUpId());
												}
											}
											if (customerHDREntity.getCustomerAddDTLList() == null
													|| customerHDREntity.getCustomerAddDTLList().isEmpty()) {
												enquiryCreateResponseModel
														.setMsg("Error While Creating Customer. Address is required.");
												//isSuccess = false;
											}
											if (isSuccess) {
												session.save(customerHDREntity);
											}
										}
									} else {
										// merger Customer
										customerHDREntity.setModifiedBy(userId);
										customerHDREntity.setModifiedDate(currDate);
										if (customerHDREntity.getCustomerAddDTLList() == null
												|| customerHDREntity.getCustomerAddDTLList().isEmpty()) {
											enquiryCreateResponseModel
													.setMsg("Error While Creating Customer. Address is required.");
											isSuccess = false;
										}
										if (isSuccess) {
											session.merge(customerHDREntity);
										}
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

								// create Followup
//								enquiryFollowUpList = new ArrayList<EnquiryFollowupEntity>();
//								enquiryFollowupEntity = new EnquiryFollowupEntity();
//								enquiryFollowupEntity.setCurrentFollowupDate(currDate);
//								enquiryFollowupEntity.setEnquiryStageId(enquiryHdrEntity.getEnquiryStageId());
//								enquiryFollowupEntity.setEnquiryTypeId(enquiryHdrEntity.getEnquiryTypeid());
//								enquiryFollowupEntity
//										.setNextFollowupActivityId(enquiryHdrEntity.getNextFollowupActivityId());
//								enquiryFollowupEntity.setNextFollowupDate(enquiryHdrEntity.getNextFollowupDate());
//								enquiryFollowupEntity.setRemarks(enquiryHdrEntity.getEnqRemark());
//								enquiryFollowupEntity.setExpectedPurchaseDate(currDate);
//								enquiryFollowupEntity.setFollowupTypeId(userId);
//
//								enquiryFollowupEntity.setEnquiryHdr(enquiryHdrEntity);
//								enquiryFollowupEntity.setCreatedBy(userId);
//								enquiryFollowupEntity.setCreatedDate(currDate);
//
//								enquiryFollowUpList.add(enquiryFollowupEntity);
//								enquiryHdrEntity.setEnquiryFollowUpList(enquiryFollowUpList);

								enquiryExchangeDTLList = enquiryHdrEntity.getEnquiryExchangeDTLList();
								
								if(enquiryExchangeDTLList !=null) {
									System.out.println("enquiryExchangeDTLList "+enquiryExchangeDTLList.size());
								}
								if (enquiryExchangeDTLList != null && !enquiryExchangeDTLList.isEmpty()) {
									enquiryExchangeDTLList.removeIf(e -> e.getBrandId() == null
											|| e.getBrandId().compareTo(BigInteger.ZERO) == 0);
									enquiryHdrEntity.setEnquiryExchangeDTLList(
											enquiryExchangeDTLList != null && enquiryExchangeDTLList.isEmpty() ? null
													: enquiryExchangeDTLList);
									
									if(enquiryExchangeDTLList !=null) {
										System.out.println("enquiryExchangeDTLList 2 "+enquiryExchangeDTLList.size());
									}
									if (enquiryExchangeDTLList != null && !enquiryExchangeDTLList.isEmpty()) {
										for (EnquiryExchangeDTLEntity enquiryExchangeDTLEntity : enquiryExchangeDTLList) {
											if (enquiryExchangeDTLEntity.getBrandId() != null) {
												enquiryExchangeDTLEntity.setEnquiryHdr(enquiryHdrEntity);
												enquiryExchangeDTLEntity.setInvInDate(currDate);
												enquiryExchangeDTLEntity.setCreatedBy(userId);
												enquiryExchangeDTLEntity.setCreatedDate(currDate);
											}
										}
									}
								}
								enquiryCustSoilTypeList = enquiryHdrEntity.getEnquiryCustSoilTypeList();
								
								if(enquiryCustSoilTypeList !=null) {
									System.out.println("enquiryCustSoilTypeList 2 "+enquiryCustSoilTypeList.size());
								}
								if (enquiryCustSoilTypeList != null && !enquiryCustSoilTypeList.isEmpty()) {
									for (EnquiryCustSoilTypeEntity enquiryCustSoilTypeEntity : enquiryCustSoilTypeList) {
										enquiryCustSoilTypeEntity.setEnquiryHdr(enquiryHdrEntity);
										enquiryCustSoilTypeEntity.setDeleteFlag(false);
										enquiryCustSoilTypeEntity.setLastupdatedon(currDate);
									}
								}
								
								enquiryCustFleetDTLList = enquiryHdrEntity.getEnquiryCustFleetDTLList();
								
								if(enquiryCustFleetDTLList !=null) {
									System.out.println("enquiryCustFleetDTLList 2 "+enquiryCustFleetDTLList.size());
								}
								if (enquiryCustFleetDTLList != null && !enquiryCustFleetDTLList.isEmpty()) {
									for (EnquiryCustFleetDTLEntity enquiryCustFleetDTLEntity : enquiryCustFleetDTLList) {
										if (enquiryCustFleetDTLEntity.getModelName() == null
												|| enquiryCustFleetDTLEntity.getModelName().equals("")) {
											isSuccess = false;
											enquiryCreateResponseModel.setMsg("Model Name is Required.");
											break;
										}
										if (enquiryCustFleetDTLEntity.getYearOfPurchase() == null
												|| enquiryCustFleetDTLEntity.getYearOfPurchase().compareTo(0) == 0) {
											isSuccess = false;
											enquiryCreateResponseModel.setMsg("Year Of Purchase is Required.");
											break;
										}
										enquiryCustFleetDTLEntity.setDeleteFlag(false);
										enquiryCustFleetDTLEntity.setLastupdatedon(currDate);
										enquiryCustFleetDTLEntity.setEnquiryHdr(enquiryHdrEntity);
									}
								}
								if (isSuccess) {
									enquiryCustCropList = enquiryHdrEntity.getEnquiryCustCropList();
									if(enquiryCustCropList !=null) {
										System.out.println("enquiryCustCropList 2 "+enquiryCustCropList.size());
									}
									
									if (enquiryCustCropList != null && !enquiryCustCropList.isEmpty()) {
										for (EnquiryCustCropEntity custCropEntity : enquiryCustCropList) {
											custCropEntity.setDeleteFlag(false);
											custCropEntity.setLastupdatedon(currDate);
											custCropEntity.setEnquiryHdr(enquiryHdrEntity);
										}
									}
									enquiryHdrEntity.setCreatedBy(userId);
									enquiryHdrEntity.setCreatedDate(currDate);
									// set Enq Status
//									if(enquiryHdrEntity.getAppEnquiryFlag() != null) {
//										enquiryHdrEntity.setAppEnquiryFlag(true);
//									}
									logger.info("Enquiry device.isMobile() : {}", (device != null ? device.isMobile() : device));
									if (device != null) {
										if (device.isMobile()) {
											if (enquiryHdrEntity.getEnquiryStatus() == null) {
												enquiryHdrEntity
														.setEnquiryStatus(WebConstants.PENDING_FOR_VST_VALIDATION);
											}
											enquiryHdrEntity.setAppEnquiryFlag(true);
										} else if (device.isNormal()) {
											enquiryHdrEntity.setEnquiryStatus(WebConstants.PENDING_FOR_VST_VALIDATION);
											if(enquiryHdrEntity.getAppEnquiryFlag() == null) {
												enquiryHdrEntity.setAppEnquiryFlag(false);
											}
										} else if (device.isTablet()) {
											if (enquiryHdrEntity.getEnquiryStatus() == null) {
												enquiryHdrEntity
														.setEnquiryStatus(WebConstants.PENDING_FOR_VST_VALIDATION);
											}
											if(enquiryHdrEntity.getAppEnquiryFlag() == null) {
												enquiryHdrEntity.setAppEnquiryFlag(false);
											}
										}
									}
									// clone & detach Img Obj
									// enquiryAttachImgsEList = enquiryHdrEntity.getEnquiryAttachImgsList();
									// enquiryHdrEntity.setEnquiryAttachImgsList(null);
									//System.out.println("bbb "+enquiryHdrEntity.toString());
									BigInteger saveId = (BigInteger) session.save(enquiryHdrEntity);
									//System.out.println("bbb "+enquiryHdrEntity.toString());
									System.out.println("Save"+saveId);
									generatedId= saveId;
									// upload Enquiry Files
									// enquiryHdrEntity.setEnquiryAttachImgsList(enquiryAttachImgsEList);

									if (enquiryAttachImgsList != null && !enquiryAttachImgsList.isEmpty()) {
										enquiryAttachImgsEList = new ArrayList<EnquiryAttachImagesEntity>();
										String enqFilePath = fileStorageProperties.getUploadDirMain()
												+ messageSource.getMessage("enq.file.upload.dir",
														new Object[] { enquiryHdrEntity.getEnquiryHdrId() },
														LocaleContextHolder.getLocale());
										for (EnquiryAttachImagesRequestModel attachImagesModel : enquiryAttachImgsList) {
											Map<String, String> fileMapData = FileUtils.uploadDOCDTLDOCS(enqFilePath,
													attachImagesModel.getFile());
											if (fileMapData != null
													&& fileMapData.get("MSG").toString().equals(WebConstants.SUCCESS)) {
												EnquiryAttachImagesEntity attachImagesEntity = new EnquiryAttachImagesEntity();

												attachImagesEntity.setFile_name(fileMapData.get("fileName"));
												attachImagesEntity.setEnquiryHdr(enquiryHdrEntity);
												enquiryAttachImgsEList.add(attachImagesEntity);
											}
										}
										enquiryHdrEntity.setEnquiryAttachImgsList(enquiryAttachImgsEList);

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
			System.out.println("DATA:::::::"+enquiryHdrEntity.getEnquiryHdrId());
			System.out.println("isSuccess:::::::"+isSuccess);
			if (isSuccess && enquiryHdrEntity.getEnquiryHdrId() != null) {
				System.out.println("mapData::::::;"+enquiryHdrEntity.getEnquiryHdrId());
				mapData = fetchEnqNoByEnqHDRId(enquiryHdrEntity.getEnquiryHdrId());
				System.out.println("mapData::::::;"+mapData);
				enquiryCreateResponseModel.setEnquiryNo((String) mapData.get("enqNo"));
				if (mapData != null && mapData.get("SUCCESS") != null) {
					enquiryCreateResponseModel.setEnquiryNo((String) mapData.get("enqNo"));
				}
				try {
					updateEnqMail(userCode, WebConstants.CREATE_ENQUIRY, enquiryHdrEntity.getEnquiryHdrId())
							.subscribe(e -> {
								logger.info(e.toString());
							});
				} catch (Exception exp) {
					logger.error(this.getClass().getName(), exp);
				}
				enquiryCreateResponseModel.setEnquiryHdrId(enquiryHdrEntity.getEnquiryHdrId());
				enquiryCreateResponseModel.setMsg("Enquiry Created Successful.");
				enquiryCreateResponseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				if (enquiryCreateResponseModel.getMsg() == null) {
					enquiryCreateResponseModel.setMsg("Error While Creating Enquiry.");
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
		String sqlQuery = "Select * from CM_CUST_HDR (nolock) ch where ch.mobile_no =:mobileNo";
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
	private Mono<Map<String, Object>> updateEnqMail(String userCode, String eventName, BigInteger enqHDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		BigInteger mailItemId = null;
		String sqlQuery = "exec [SP_MAIL_ENQ_CREATE] :userCode, :eventName, :enqHDRId, :isIncludeActive";
		mapData.put("ERROR", "ERROR WHILE INSERTING ENQUIRY MAIL TRIGGERS..");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("eventName", eventName);
			query.setParameter("enqHDRId", enqHDRId);
			query.setParameter("isIncludeActive", "N");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String msg = null;
				for (Object object : data) {
					Map row = (Map) object;
					msg = (String) row.get("msg");
					mailItemId = (BigInteger) row.get("mailItemId");
				}
				mapData.put("msg", msg);
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING ENQUIRY MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING ENQUIRY MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (mailItemId != null && mailItemId.compareTo(BigInteger.ZERO) > 0) {
				PublishModel publishModel = new PublishModel();
				publishModel.setId(mailItemId);
			//	publishModel.setTopic(senderCreateEnqTopicExchange.getName());
				//CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
					// Simulate a long-running Job
					try {
//						rabbitTemplate.convertAndSend(senderCreateEnqTopicExchange.getName(), routingKey,
//								commonUtils.objToJson(publishModel).toString());
//						logger.info("Published message for create enquiry '{}'", publishModel.toString());

					} catch (Exception exp) {
						logger.error(this.getClass().getName(), exp);
					}
					System.out.println("I'll run in a separate thread than the main thread.");
				//});
			}
		}
		return Mono.just(mapData);
	}
}
