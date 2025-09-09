/**
 * 
 */
package com.hitech.dms.web.dao.allotment.create;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.allotment.MachineAllotmentDtlEntity;
import com.hitech.dms.web.entity.allotment.MachineAllotmentEntity;
import com.hitech.dms.web.entity.common.SystemLookUpEntity;
import com.hitech.dms.web.entity.customer.CustomerAddDTLEntity;
import com.hitech.dms.web.entity.customer.CustomerHDREntity;
import com.hitech.dms.web.entity.ledger.SalesMachineInventoryLedgerEntity;
import com.hitech.dms.web.entity.ledger.SalesMachineItemInvLedgerEntity;
import com.hitech.dms.web.model.allot.create.request.MachineAllotCreateRequestModel;
import com.hitech.dms.web.model.allot.create.request.MachineAllotDtlCreateRequestModel;
import com.hitech.dms.web.model.allot.create.request.MachineAllotItemDtlCreateRequestModel;
import com.hitech.dms.web.model.allot.create.response.MachineAllotCreateResponseModel;
import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.service.client.CommonServiceClient;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class MachineAllotCreateDaoImpl implements MachineAllotCreateDao {
	private static final Logger logger = LoggerFactory.getLogger(MachineAllotCreateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private CommonServiceClient commonServiceClient;

	public DealerDTLResponseModel fetchDealerDTLByDealerId(String authorizationHeader, BigInteger dealerId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDealerDTLByDealerId invoked..");
		}
		DealerDTLResponseModel responseModel = null;
		try {
			DealerDTLRequestModel requestModel = new DealerDTLRequestModel();
			requestModel.setDealerId(dealerId);
			requestModel.setIsFor("MACHINEPO");
			HeaderResponse headerResponse = commonServiceClient.fetchDealerDTLByDealerId(authorizationHeader,
					requestModel);
			Object object = headerResponse.getResponseData();
			if (object != null) {
				try {
					com.google.gson.Gson gson = new com.google.gson.Gson();
					String jsonString = gson.toJson(object);
					responseModel = gson.fromJson(jsonString, DealerDTLResponseModel.class);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e);
				}
			}
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return responseModel;
	}

	@SuppressWarnings("deprecation")
	public MachineAllotCreateResponseModel create(String authorizationHeader, String userCode,
			MachineAllotCreateRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("create Allotment invoked.." + userCode);
			
		}
		logger.info(requestModel.toString());
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;

		MachineAllotCreateResponseModel responseModel = new MachineAllotCreateResponseModel();
		MachineAllotmentEntity allotmentEntity = null;
		CustomerHDREntity customerHDREntity = null;
		boolean isSuccess = true;
		String allotNumber = null;
		String sqlQuery = null;
		try {
			List<MachineAllotDtlCreateRequestModel> enqMachineDtlList = requestModel.getEnqMachineDtlList();
			List<MachineAllotItemDtlCreateRequestModel> enqItemDtlList = requestModel.getEnqItemDtlList();

			if (requestModel.isOnlyImplementFlag()
					&& (requestModel.getEnqItemDtlList() == null || requestModel.getEnqItemDtlList().isEmpty())) {
				logger.error(this.getClass().getName(), "At-least One Line Item Is Required");
				responseModel.setMsg("At-least One Line Item Is Required");
				isSuccess = false;
			} else if (!requestModel.isOnlyImplementFlag()
					&& (requestModel.getEnqMachineDtlList() == null || requestModel.getEnqMachineDtlList().isEmpty())) {
				logger.error(this.getClass().getName(), "At-least One Machine Item Is Required");
				responseModel.setMsg("At-least One Machine Item Is Required");
				isSuccess = false;
			}
			if (isSuccess) {
				if (!requestModel.isOnlyImplementFlag() && requestModel.getEnquiryId() != null) {
					enqMachineDtlList = enqMachineDtlList.stream().filter(e -> e.getIsAlloted())
							.collect(Collectors.toList());
					if (enqMachineDtlList == null || enqMachineDtlList.isEmpty()) {
						logger.error(this.getClass().getName(), "At-least One Machine Item Is Required For Allotment.");
						responseModel.setMsg("At-least One Machine Item Is Required For Allotment.");
						isSuccess = false;
					} else {
						requestModel.setEnqMachineDtlList(enqMachineDtlList);
					}
				}

				session = sessionFactory.openSession();
				transaction = session.beginTransaction();
				String productType=requestModel.getProductGroup();
				allotmentEntity = mapper.map(requestModel, MachineAllotmentEntity.class, "MachineAllotMapId");

				List<MachineAllotmentDtlEntity> machineAllotDtlList = allotmentEntity.getEnqMachineDtlList();
				// check for Machine Detail when Implement flag is false and enquiry id is not
				// null
				if (!allotmentEntity.isOnlyImplementFlag() && allotmentEntity.getEnquiryId() != null) {
				} else if (allotmentEntity.isOnlyImplementFlag()) {
					// else for only Implement when flag will be
					if (machineAllotDtlList == null) {
						machineAllotDtlList = new ArrayList<MachineAllotmentDtlEntity>();
						allotmentEntity.setEnqMachineDtlList(machineAllotDtlList);
					}

				} else {
					logger.error(this.getClass().getName(),
							"Machine Implement Flag is False/Null and Also Enquiry Id Is Null.");
					responseModel.setMsg("Please Contact Your System Administrator.");
					isSuccess = false;
				}
				if (isSuccess) {
					BigInteger userId = null;
					Date currDate = new Date();
					SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy");
					String strYear = simpleformat.format(currDate);
					String customerProspectType = null;
					mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
					if (mapData != null && mapData.get("SUCCESS") != null) {
						userId = (BigInteger) mapData.get("userId");
						// fetch Customer Details using mobile number
						mapData = fetchCustomerDTLBtMobileNo(session, requestModel.getMobileNo());
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

							}
							// if customer's prospectType is customer, thn no updation in customer master
							// if customer's prospectType is Prospect thn updation allowed
							// Or create New Customer
							if (((customerProspectType != null
									&& customerProspectType.equalsIgnoreCase(WebConstants.CUST_TYPE_PROSPECT))
									|| isNewCust) && isSuccess) {
								// setting fields value in customerHDREntity from requestModel
								if (requestModel.getAddress1() == null) {
									requestModel.setAddress1("");
								}
								if (requestModel.getCustomerCategoryId() != null) {
									customerHDREntity.setCustomerCategoryId(requestModel.getCustomerCategoryId());
								}
								if (requestModel.getAddress1() != null) {
									customerHDREntity.setAddress1(requestModel.getAddress1());
								}
								if (requestModel.getAddress2() != null) {
									customerHDREntity.setAddress2(requestModel.getAddress2());
								}
								if (requestModel.getAddress3() != null) {
									customerHDREntity.setAddress3(requestModel.getAddress3());
								}
								if (requestModel.getAlternateNo() != null) {
									customerHDREntity.setAlternateNo(requestModel.getAlternateNo());
								}
								if (requestModel.getOrganizationName() != null) {
									customerHDREntity.setOrganizationName(requestModel.getOrganizationName());
								}
								customerHDREntity.setContactTitle(requestModel.getContactTitle() == null ? "Mr."
										: requestModel.getContactTitle());
								customerHDREntity.setProspectType(WebConstants.CUST_TYPE_PROSPECT);
								customerHDREntity.setFirstName(requestModel.getFirstName());
								customerHDREntity.setMiddleName(requestModel.getMiddleName());
								customerHDREntity.setLastName(requestModel.getLastName());
								if (requestModel.getMobileNo() != null) {
									customerHDREntity.setMobileNo(requestModel.getMobileNo());
								}
								if (requestModel.getWhatsAppNo() != null) {
									customerHDREntity.setWhatsAppNo(requestModel.getWhatsAppNo());
								}
								if (requestModel.getPhoneNo() != null) {
									customerHDREntity.setPhoneNo(requestModel.getPhoneNo());
								}
								if (requestModel.getEmailId() != null) {
									customerHDREntity.setEmailId(requestModel.getEmailId());
								}
								if (requestModel.getPinId() != null) {
									customerHDREntity.setPinId(requestModel.getPinId());
								}
								if (requestModel.getDateOfBirth() != null) {
									customerHDREntity.setDateOfBirth(requestModel.getDateOfBirth());
								}
								if (requestModel.getAnniversaryDate() != null) {
									customerHDREntity.setAnniversaryDate(requestModel.getAnniversaryDate());
								}
								if (requestModel.getOccupationID() != null) {
									customerHDREntity.setOccupationID(requestModel.getOccupationID());
								}
								if (requestModel.getLandInAcres() != null) {
									customerHDREntity.setLandInAcres(requestModel.getLandInAcres());
								}

								if (isNewCust) {
									mapData = fetchSysLookupDTLByCode(session, WebConstants.CUST_ADD_TYPE,
											WebConstants.REGISTERED);
									if (mapData != null && mapData.get("SUCCESS") != null) {
										SystemLookUpEntity systemLookUpEntity = (SystemLookUpEntity) mapData
												.get("systemLookUpEntity");
										customerAddDTLEntity.setAddressTypeID(systemLookUpEntity.getLookUpId());
									}
									customerAddDTLEntity.setContactNo(requestModel.getMobileNo());
									customerAddDTLEntity.setCreatedBy(userId);
									customerAddDTLEntity.setCreatedDate(currDate);
									customerAddDTLEntity.setCustAddLine1(requestModel.getAddress1());
									customerAddDTLEntity.setCustAddLine2(requestModel.getAddress2());
									customerAddDTLEntity.setCustAddLine3(requestModel.getAddress3());
									customerAddDTLEntity.setCustomerHdr(customerHDREntity);
									customerAddDTLEntity.setEmail(requestModel.getEmailId());
									customerAddDTLEntity.setGstNo(requestModel.getGstIN());

									customerHDREntity.setCustomerAddDTLList(customerAddDTLList);
								}

								// save customer if not exits
								if (isNewCust) {
									if (customerCode == null) {
										// customer code not generated
										responseModel.setMsg("Error While generating Customer Code.");
										logger.info("Error While generating Customer Code While ALlotment.");
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
												customerHDREntity.setContactTitle(systemLookUpEntity.getLookupVal());
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
										logger.info("Creating Customer while Allotment : ");
										session.save(customerHDREntity);
									}
								} else {
									// merger Customer
									customerHDREntity.setModifiedBy(userId);
									customerHDREntity.setModifiedDate(currDate);
									logger.info("Merging Customer while Allotment : ");
									session.merge(customerHDREntity);
								}

							}

							if (isSuccess) {
								// fetch Dealer Detail
								DealerDTLResponseModel dealerDtl = fetchDealerDTLByDealerId(authorizationHeader,
										requestModel.getDealerId());
								if (dealerDtl != null) {
									// map second list to first list
									if (enqItemDtlList != null && !enqItemDtlList.isEmpty()) {
										for (MachineAllotItemDtlCreateRequestModel dtlCreateRequestModel : enqItemDtlList) {
											if (dtlCreateRequestModel.getMachineInventoryId() != null) {
												MachineAllotmentDtlEntity allotmentDtlEntity = new MachineAllotmentDtlEntity();
												allotmentDtlEntity.setIsAlloted(true);
												
												allotmentDtlEntity.setProductGroup(dtlCreateRequestModel.getProductGroup());
												allotmentDtlEntity.setAllotQnty(dtlCreateRequestModel.getQuantity());
												if(dtlCreateRequestModel.getProductGroup() !=null) {
													if(dtlCreateRequestModel.getProductGroup().equalsIgnoreCase("Accessory") || dtlCreateRequestModel.getProductGroup().equalsIgnoreCase("Implement")) {
														
														allotmentDtlEntity.setMachineInventoryId(dtlCreateRequestModel.getMachineInventoryId());  //dtlCreateRequestModel.getMachineItemId()
														
													}else {
														allotmentDtlEntity.setMachineInventoryId(
																dtlCreateRequestModel.getMachineInventoryId());
													}
												}else {

													allotmentDtlEntity.setMachineInventoryId(
															dtlCreateRequestModel.getMachineInventoryId());
												
												}
												
												
												machineAllotDtlList.add(allotmentDtlEntity);
											}
										}
									}
									//SalesMachineItemInvLedgerEntity
									for (MachineAllotmentDtlEntity dtlEntity : machineAllotDtlList) {
										
										
										if(dtlEntity.getProductGroup() != null && !dtlEntity.getProductGroup().equalsIgnoreCase("null")) {
											if (dtlEntity.getProductGroup().equalsIgnoreCase("Accessory")
													|| dtlEntity.getProductGroup().equalsIgnoreCase("Implement")) {

												sqlQuery = "Select TOP 1 * from SA_MACHINE_ITEM_INV_LEDGER (nolock) where machine_inventory_id =:machineInventory and branch_id =:branchId "
														+ "  order by machine_item_id desc ";
												query = session.createNativeQuery(sqlQuery)
														.addEntity(SalesMachineItemInvLedgerEntity.class);
												query.setParameter("machineInventory", dtlEntity.getMachineInventoryId());
												query.setParameter("branchId", allotmentEntity.getBranchId());
												SalesMachineItemInvLedgerEntity inventoryLedgerEntity = (SalesMachineItemInvLedgerEntity) query
														.uniqueResult();
												if (inventoryLedgerEntity == null) {
													logger.error(this.getClass().getName(),
															"While Allotting InventoryLedgerEntity is null/Empty for "
																	+ dtlEntity.getMachineInventoryId() + " : "
																	+ allotmentEntity.getBranchId());
													responseModel
															.setMsg("Stock Not Found. Kindly Contact Your Admnistrator.");
													isSuccess = false;
													break;
												} else {
													BigInteger allotQnty=dtlEntity.getAllotQnty();
													Integer inward = inventoryLedgerEntity.getInward();
													Integer outward = inventoryLedgerEntity.getOutward();
													BigInteger alreadeyAllotedQuantity = inventoryLedgerEntity.getAllotQuantity();
													
													//Integer available=inward-outward;
													
													BigInteger toalAvail=BigInteger.valueOf(inward).subtract(alreadeyAllotedQuantity);
													
													
													int compareTo = toalAvail.compareTo(allotQnty);
													
													logger.info("allotQnty "+allotQnty+" outward "+outward+" alreadeyAllotedQuantity "+alreadeyAllotedQuantity+" toalAvail "+toalAvail+" compareTo "+compareTo);
													
													if(compareTo==1) {
														inventoryLedgerEntity.setAllotFlag(true);
														inventoryLedgerEntity.setModifiedBy(userId);
														inventoryLedgerEntity.setModifiedDate(currDate);
														inventoryLedgerEntity.setAllotQuantity(allotQnty.add(alreadeyAllotedQuantity));
														session.merge(inventoryLedgerEntity);
													}else if(compareTo==0) {
														inventoryLedgerEntity.setAllotFlag(true);
														inventoryLedgerEntity.setModifiedBy(userId);
														inventoryLedgerEntity.setModifiedDate(currDate);
														inventoryLedgerEntity.setAllotQuantity(allotQnty.add(alreadeyAllotedQuantity));
														session.merge(inventoryLedgerEntity);
													}else if(compareTo==-1) {
														logger.error(this.getClass().getName(),
																"While Allotting InventoryLedgerEntity greater than available stock "
																		+ dtlEntity.getMachineInventoryId() + " : "
																		+ allotmentEntity.getBranchId());
														responseModel.setMsg(
																"Stock Is less than Allotted Quantity. Kindly Contact Your Admnistrator.");
														isSuccess = false;
														break;
													}else {
														System.out.println("some thing error or wrong");
													}
													
												}

											}else {


												sqlQuery = "Select TOP 1 * from SA_MACHINE_INVENTORY_LEDGER (nolock) where machine_inventory_id =:machineInventory and branch_id =:branchId "
														+ " and OUT_DOC_TYPE IS NULL and OUT_DOC_NUMBR IS NULL and OUT_DATE IS NULL and allot_flag = 0 order by machine_inventory_id desc ";
												query = session.createNativeQuery(sqlQuery)
														.addEntity(SalesMachineInventoryLedgerEntity.class);
												query.setParameter("machineInventory", dtlEntity.getMachineInventoryId());
												query.setParameter("branchId", allotmentEntity.getBranchId());
												SalesMachineInventoryLedgerEntity inventoryLedgerEntity = (SalesMachineInventoryLedgerEntity) query
														.uniqueResult();
												if (inventoryLedgerEntity == null) {
													logger.error(this.getClass().getName(),
															"While Allotting InventoryLedgerEntity is null/Empty for "
																	+ dtlEntity.getMachineInventoryId() + " : "
																	+ allotmentEntity.getBranchId());
													responseModel.setMsg("Stock Not Found. Kindly Contact Your Admnistrator.");
													isSuccess = false;
													break;
												} else {
													if (inventoryLedgerEntity.isAllotFlag()) {
														logger.error(this.getClass().getName(),
																"While Allotting InventoryLedgerEntity Is Already Allotted for "
																		+ dtlEntity.getMachineInventoryId() + " : "
																		+ allotmentEntity.getBranchId());
														responseModel.setMsg(
																"Stock Is Already Allotted. Kindly Contact Your Admnistrator.");
														isSuccess = false;
														break;
													}
													inventoryLedgerEntity.setAllotFlag(true);
													inventoryLedgerEntity.setModifiedBy(userId);
													inventoryLedgerEntity.setModifiedDate(currDate);
													session.merge(inventoryLedgerEntity);
											}
											
											
											
											
											}
											
										}else {

											sqlQuery = "Select TOP 1 * from SA_MACHINE_INVENTORY_LEDGER (nolock) where machine_inventory_id =:machineInventory and branch_id =:branchId "
													+ " and OUT_DOC_TYPE IS NULL and OUT_DOC_NUMBR IS NULL and OUT_DATE IS NULL and allot_flag = 0 order by machine_inventory_id desc ";
											query = session.createNativeQuery(sqlQuery)
													.addEntity(SalesMachineInventoryLedgerEntity.class);
											query.setParameter("machineInventory", dtlEntity.getMachineInventoryId());
											query.setParameter("branchId", allotmentEntity.getBranchId());
											SalesMachineInventoryLedgerEntity inventoryLedgerEntity = (SalesMachineInventoryLedgerEntity) query
													.uniqueResult();
											if (inventoryLedgerEntity == null) {
												logger.error(this.getClass().getName(),
														"While Allotting InventoryLedgerEntity is null/Empty for "
																+ dtlEntity.getMachineInventoryId() + " : "
																+ allotmentEntity.getBranchId());
												responseModel.setMsg("Stock Not Found. Kindly Contact Your Admnistrator.");
												isSuccess = false;
												break;
											} else {
												if (inventoryLedgerEntity.isAllotFlag()) {
													logger.error(this.getClass().getName(),
															"While Allotting InventoryLedgerEntity Is Already Allotted for "
																	+ dtlEntity.getMachineInventoryId() + " : "
																	+ allotmentEntity.getBranchId());
													responseModel.setMsg(
															"Stock Is Already Allotted. Kindly Contact Your Admnistrator.");
													isSuccess = false;
													break;
												}
												inventoryLedgerEntity.setAllotFlag(true);
												inventoryLedgerEntity.setModifiedBy(userId);
												inventoryLedgerEntity.setModifiedDate(currDate);
												session.merge(inventoryLedgerEntity);
										}
										
										
										
										}
										
										
										
										dtlEntity.setMachineAllotHdr(allotmentEntity);
									}

									if (isSuccess) {
										mapData = fetchLastMachineALLOTDTLByBranchId(session,
												allotmentEntity.getBranchId(), strYear);
										String lastAllotNumber = null;
										simpleformat = new SimpleDateFormat("yy");
										strYear = simpleformat.format(currDate);
										if (mapData != null && mapData.get("SUCCESS") != null) {
											if (mapData.get("allotNumber") != null) {
												lastAllotNumber = (String) mapData.get("allotNumber");
											//	int lastIndexOf = lastAllotNumber.lastIndexOf(strYear);
												
												int lastIndexOf =dealerDtl.getDealerCode().length()+2;;//lastDCNumber.lastIndexOf(strYear); 
												
												String prefix = lastAllotNumber.substring(0, lastIndexOf);
												allotNumber = lastAllotNumber.substring(lastIndexOf + 4,
														lastAllotNumber.length());
												Integer i = Integer.valueOf(allotNumber);
												allotNumber = prefix + strYear + String.format("%04d", i + 1);
											} else {
												allotNumber = "SA" + dealerDtl.getDealerCode() + strYear + "0001";
											}
											allotmentEntity.setAllotNumber(allotNumber);
											allotmentEntity.setCustomerId(customerHDREntity.getCustomerId());
											allotmentEntity.setAllotStatus(WebConstants.ALLOTTED);
											allotmentEntity.setCreatedBy(userId);
											allotmentEntity.setCreatedDate(currDate);
											allotmentEntity.setBToCFlag(requestModel.getBusinessType().equalsIgnoreCase("B2C")?1:0);
											session.save(allotmentEntity);

											if (allotmentEntity.getEnquiryId() != null) {
												mapData = commonDao.updateEnquiryStatus(session, userCode, userId,
														currDate, allotmentEntity.getEnquiryId(),
														WebConstants.ALLOTTED);
												if (mapData != null && mapData.get("SUCCESS") != null) {
													logger.info("Enquiry Status has been updated to Allotted.");
												} else {
													logger.error(this.getClass().getName(),
															"Error While Updating Enquiry Status.");
													responseModel.setMsg(
															"Error While Updating Enquiry Status. Please Contact Your System Administrator.");
													isSuccess = false;
												}
											}
										} else {
											logger.error(this.getClass().getName(),
													"Error While Validating Last Machine Allotment Number.");
											responseModel.setMsg("Please Contact Your System Administrator.");
											isSuccess = false;
										}
									}
								} else {
									// Dealer Not Found.
									isSuccess = false;
									responseModel.setMsg("Dealer Not Found.");
								}
							}
						} else if (mapData.get("ERROR") != null) {
							// ERROR WHILE FETCHING CUSTOMER DTLs
							responseModel.setMsg("Error While Fetching Customer Details.");
							logger.info("Error While Fetching Customer Details While ALlotment.");
							isSuccess = false;
						}
					} else {
						// User not found
						isSuccess = false;
						responseModel.setMsg("User Not Found.");
					}
					if (isSuccess) {
						transaction.commit();
					}
				}
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
			if (isSuccess) {
				mapData = fetchMachineAllotNoByAllotId(session, allotmentEntity.getMachineAllotmentId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setMachineAllotmentId(allotmentEntity.getMachineAllotmentId());
					responseModel.setAllotNumber((String) mapData.get("allotNumber"));
					responseModel.setMsg("Machine Allotment Number Created Successfully.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				} else {
					responseModel.setMsg("Error While Fetching Machine Allotment Number.");
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Creating Machine Allotment.");
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				if (transaction != null) {
					transaction.rollback();
				}
			}
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
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
	public Map<String, Object> fetchLastMachineALLOTDTLByBranchId(Session session, BigInteger branchId, String fy) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "select top 1 allotment_number"
				+ "	   from SA_MACHINE_ALLOTMENT (nolock) pr where DATEPART(year,allotment_date)=:fy "
				+ "	   and pr.branch_id =:branchId order by machine_allotment_id desc ";
		mapData.put("ERROR", "Machine Last Allot Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("branchId", branchId);
			query.setParameter("fy", fy);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String allotNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					allotNumber = (String) row.get("allotment_number");
				}
				mapData.put("allotNumber", allotNumber);
			}
			mapData.put("SUCCESS", "FETCHED");
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE ALLOT DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE ALLOT DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchMachineAllotNoByAllotId(Session session, BigInteger allotHdrId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select allotment_number from SA_MACHINE_ALLOTMENT (nolock) pr where pr.machine_allotment_id =:allotHdrId";
		mapData.put("ERROR", "Machine Allotment Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("allotHdrId", allotHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String allotNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					allotNumber = (String) row.get("allotment_number");
				}
				mapData.put("allotNumber", allotNumber);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE ALLOTMENT DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE ALLOTMENT DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
}
