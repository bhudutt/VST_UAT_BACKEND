/**
 * 
 */
package com.hitech.dms.web.dao.dc.view;

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

import com.hitech.dms.web.model.dc.view.request.DcViewRequestModel;
import com.hitech.dms.web.model.dc.view.response.DcCancellationApprovalResponseModel;
import com.hitech.dms.web.model.dc.view.response.DcCheckListViewResponseModel;
import com.hitech.dms.web.model.dc.view.response.DcCustViewResponseModel;
import com.hitech.dms.web.model.dc.view.response.DcItemDtlViewResponseModel;
import com.hitech.dms.web.model.dc.view.response.DcMachDtlViewResponseModel;
import com.hitech.dms.web.model.dc.view.response.DcViewResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class DcViewDaoImpl implements DcViewDao {
	private static final Logger logger = LoggerFactory.getLogger(DcViewDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public DcViewResponseModel fetchMachineDcDtl(String userCode, DcViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachineDcDtl invoked.." + requestModel.toString());
		}
		Session session = null;
		DcViewResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchMachineDcDtl(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new DcViewResponseModel();

					responseModel.setDealerId((BigInteger) row.get("dealerId"));
					responseModel.setDealerCode((String) row.get("dealerCode"));
					responseModel.setDealerName((String) row.get("dealerName"));
					responseModel.setBranchId((BigInteger) row.get("branchId"));
					responseModel.setBranchCode((String) row.get("branchCode"));
					responseModel.setBranchName((String) row.get("branchName"));
					responseModel.setPcId((Integer) row.get("PcId"));
					responseModel.setPcDesc((String) row.get("PcDesc"));

					responseModel.setDcId((BigInteger) row.get("DcId"));
					responseModel.setDcNumber((String) row.get("DcNumber"));
					responseModel.setDcDate((String) row.get("DcDate"));
					//responseModel.setDcDate1((Date) row.get("DcDate1"));
					responseModel.setDcStatus((String) row.get("DcStatus"));
					responseModel.setDcTypeId((Integer) row.get("DcTypeId"));
					responseModel.setDcType((String) row.get("DcType"));
					
					responseModel.setDcCancelRequestId((BigInteger) row.get("DcCancelRequestId"));

					responseModel.setAction((String) row.get("action"));

					responseModel.setMachineAllotmentId((BigInteger) row.get("MachineAllotmentId"));
					responseModel.setAllotNumber((String) row.get("AllotNumber"));
					responseModel.setAllotDate((String) row.get("AllotDate"));
					responseModel.setEnquiryHdrId((BigInteger) row.get("EnquiryHdrId"));
					responseModel.setEnquiryNo((String) row.get("EnquiryNo"));
					responseModel.setEnquiryType((String) row.get("EnquiryType"));
					responseModel.setEnquiryDate((String) row.get("EnquiryDate"));
					responseModel.setGatePassNumber((String) row.get("GatePassNumber"));
					responseModel.setGatePassDate((String) row.get("GatePassDate"));

					responseModel.setDcCancelDate((String) row.get("DcCancelDate"));
					responseModel.setDcCancelReasonId((Integer) row.get("DcCancelReasonId"));
					responseModel.setDcCancelReason((String) row.get("DcCancelReason"));
					responseModel.setDcCancelRemark((String) row.get("DcCancelRemark"));
					responseModel.setDcCancellationType((String) row.get("DcCancellationType"));

					responseModel.setInsuranceMasterId((BigInteger) row.get("InsuranceMasterId"));
					responseModel.setPolicyStartDate((String) row.get("PolicyStartDate"));
					responseModel.setPolicyEndDate((String) row.get("PolicyEndDate"));
					responseModel.setDcRemarks((String) row.get("DcRemarks"));
//					responseModel.setVehicleRegistrationNumber((String) row.get("VehicleRegistrationNumber"));
					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setPartyLocation((String) row.get("PartyLocation"));
					responseModel.setPartyAddress1((String) row.get("PartyAddress1"));
					responseModel.setPartyAddress2((String) row.get("PartyAddress2"));
					responseModel.setPartyAddress3((String) row.get("PartyAddress3"));
					responseModel.setMobileNumber((String) row.get("MobileNumber"));
					responseModel.setEmailId((String) row.get("EmailId"));
//					responseModel.setDisplayValue((String) row.get("DisplayValue"));
					responseModel.setPinId((BigInteger) row.get("PinId"));
					responseModel.setPincode((String) row.get("Pincode"));
					responseModel.setVillage((String) row.get("Village"));
					responseModel.setTehsil((String) row.get("Tehsil"));
					responseModel.setDistrict((String) row.get("District"));
					responseModel.setState((String) row.get("State"));
					responseModel.setCountry((String) row.get("Country"));
				}
				requestModel.setFlag(2);
				data = fetchMachineDcDtl(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					DcCustViewResponseModel customerDetail = null;
					for (Object object : data) {
						Map row = (Map) object;
						customerDetail = new DcCustViewResponseModel();
						customerDetail.setMobileNo((String) row.get("Mobile_No"));
						customerDetail.setCustomerCode((String) row.get("CustomerCode"));
						customerDetail.setCustomerType((String) row.get("ProspectCategory"));
						customerDetail.setOrganizationName((String) row.get("CompanyName"));
						customerDetail.setCustomerName((String) row.get("CustomerName"));
						customerDetail.setContactTitle((String) row.get("Title"));
						customerDetail.setFirstName((String) row.get("FirstName"));
						customerDetail.setMiddleName((String) row.get("MiddleName"));
						customerDetail.setLastName((String) row.get("LastName"));
						customerDetail.setWhatsAppNo((String) row.get("WhatsappNo"));
						customerDetail.setAlternateNo((String) row.get("Alternate_No"));
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
						customerDetail.setState((String) row.get("state"));
						customerDetail.setCountry((String) row.get("country"));
//						customerDetail.setCityId((BigInteger) row.get("city_id"));
//						customerDetail.setTehsilId((BigInteger) row.get("tehsil_id"));
//						customerDetail.setDistrictId((BigInteger) row.get("district_id"));
//						customerDetail.setStateId((BigInteger) row.get("state_id"));
//						customerDetail.setCountryId((BigInteger) row.get("country_id"));
					}
					responseModel.setCustomerDetail(customerDetail);
				}
				requestModel.setFlag(3);
				data = fetchMachineDcDtl(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<DcMachDtlViewResponseModel> enqMachDtlList = new ArrayList<DcMachDtlViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						DcMachDtlViewResponseModel dtlForMachineResponseModel = new DcMachDtlViewResponseModel();
						dtlForMachineResponseModel.setMachineDcDtlId((BigInteger) row.get("machineDcDtlId"));
						dtlForMachineResponseModel.setVinId((BigInteger) row.get("VinId"));
						dtlForMachineResponseModel.setMachineInventoryId((BigInteger) row.get("machine_inventory_id"));
						dtlForMachineResponseModel.setItemNo((String) row.get("ItemNo"));
						dtlForMachineResponseModel.setItemDesc((String) row.get("ItemDesc"));
						dtlForMachineResponseModel.setModel((String) row.get("Model"));
						dtlForMachineResponseModel.setChassisNo((String) row.get("ChassisNo"));
						dtlForMachineResponseModel.setVinNo((String) row.get("VinNo"));
						dtlForMachineResponseModel.setEngineNo((String) row.get("EngineNo"));
						dtlForMachineResponseModel.setQty((Integer) row.get("Qty"));

						enqMachDtlList.add(dtlForMachineResponseModel);
					}

					responseModel.setDcDtlList(enqMachDtlList);
				}

				requestModel.setFlag(4);
				data = fetchMachineDcDtl(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<DcItemDtlViewResponseModel> dcItemList = new ArrayList<DcItemDtlViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						DcItemDtlViewResponseModel dtlForMachineResponseModel = new DcItemDtlViewResponseModel();
						dtlForMachineResponseModel.setDcItemId((BigInteger) row.get("dcItemId"));
						dtlForMachineResponseModel.setMachineItemId((BigInteger) row.get("machineItemId"));
						dtlForMachineResponseModel.setItemNo((String) row.get("ItemNo"));
						dtlForMachineResponseModel.setItemDesc((String) row.get("ItemDesc"));
						dtlForMachineResponseModel.setQty((Integer) row.get("Qty"));

						dcItemList.add(dtlForMachineResponseModel);
					}

					responseModel.setDcItemList(dcItemList);
				}

				requestModel.setFlag(5);
				data = fetchMachineDcDtl(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<DcCheckListViewResponseModel> dcCheckList = new ArrayList<DcCheckListViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						DcCheckListViewResponseModel checkResponseModel = new DcCheckListViewResponseModel();
						checkResponseModel.setDcCheckListId((BigInteger) row.get("DcCheckListId"));
						checkResponseModel.setCheckListId((Integer) row.get("CheckListId"));
						checkResponseModel.setPcId((Integer) row.get("PcId"));
						checkResponseModel.setDeliverableChecklist((String) row.get("DeliverableChecklist"));
						boolean isDelivered = (boolean) row.get("isDelivered");
						if (isDelivered) {
							checkResponseModel.setIsDelivered(true);
						} else {
							checkResponseModel.setIsDelivered(false);
						}

						dcCheckList.add(checkResponseModel);
					}

					responseModel.setDcCheckList(dcCheckList);
				}
				requestModel.setFlag(6);
				data = fetchMachineDcDtl(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<DcCancellationApprovalResponseModel> cancellationApprovalList = new ArrayList<DcCancellationApprovalResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						DcCancellationApprovalResponseModel appResponseModel = new DcCancellationApprovalResponseModel();
						appResponseModel.setApprovalStaus((String) row.get("ApprovalStaus"));
						appResponseModel.setApprovedDate((String) row.get("ApprovedDate"));
						appResponseModel.setCancelReasonDesc((String) row.get("CancelReasonDesc"));
						appResponseModel.setCancelRemark((String) row.get("CancelRemark"));
						appResponseModel.setCancelRequestDate((String) row.get("CancelRequestDate"));
						appResponseModel.setDcCancelDate((String) row.get("dcCancelDate"));
						appResponseModel.setDesginationDesc((String) row.get("DesginationDesc"));
						appResponseModel.setEmployeeName((String) row.get("EmployeeName"));

						cancellationApprovalList.add(appResponseModel);
					}

					responseModel.setCancellationApprovalList(cancellationApprovalList);
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
	public List fetchMachineDcDtl(Session session, String userCode, DcViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachineDcDtl invoked...");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_DC_VIEW] :userCode, :dcNo, :dcId, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dcNo", requestModel.getDcNumber());
			query.setParameter("dcId", requestModel.getDcId());
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
