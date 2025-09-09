/**
 * 
 */
package com.hitech.dms.web.dao.inv.view;

import java.math.BigDecimal;
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

import com.hitech.dms.web.model.inv.customer.response.CustDtlForInvResponseModel;
import com.hitech.dms.web.model.inv.dc.list.response.DcListForInvResponseModel;
import com.hitech.dms.web.model.inv.inv.view.request.InvViewRequestModel;
import com.hitech.dms.web.model.inv.inv.view.response.InvCancelAppViewResponseModel;
import com.hitech.dms.web.model.inv.inv.view.response.InvItemDtlForInvResponseModel;
import com.hitech.dms.web.model.inv.inv.view.response.InvMachDtlForInvResponseModel;
import com.hitech.dms.web.model.inv.inv.view.response.InvViewResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class InvViewDaoImpl implements InvViewDao {
	private static final Logger logger = LoggerFactory.getLogger(InvViewDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public InvViewResponseModel fetchMachineInvDtl(String userCode, InvViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachineInvDtl invoked.." + requestModel.toString());
		}
		Session session = null;
		InvViewResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchMachineInvDtl(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new InvViewResponseModel();

					responseModel.setDealerId((BigInteger) row.get("dealerId"));
					responseModel.setDealerCode((String) row.get("dealerCode"));
					responseModel.setDealerName((String) row.get("dealerName"));
					responseModel.setBranchId((BigInteger) row.get("branchId"));
					responseModel.setBranchCode((String) row.get("branchCode"));
					responseModel.setBranchName((String) row.get("branchName"));
					responseModel.setPcId((Integer) row.get("PcId"));
					responseModel.setPcDesc((String) row.get("PcDesc"));

					responseModel.setSalesInvoiceHdrId((BigInteger) row.get("SalesInvoiceHdrId"));
					responseModel.setInvoiceNumber((String) row.get("InvoiceNumber"));
					responseModel.setInvoiceDate((String) row.get("InvoiceDate"));
					responseModel.setInvoiceStatus((String) row.get("InvoiceStatus"));
					responseModel.setInvoiceTypeId((Integer) row.get("InvoiceTypeId"));
					responseModel.setInvoiceType((String) row.get("InvoiceType"));
					
					responseModel.setCustomerId((BigInteger) row.get("CustomerId"));

					responseModel.setAction((String) row.get("action"));

					responseModel.setToDealerId((BigInteger) row.get("ToDealerId"));
					responseModel.setToDealerCode((String) row.get("ToDealerCode"));
					responseModel.setToDealerName((String) row.get("ToDealerName"));

					responseModel.setInvCancelRequestId((BigInteger) row.get("InvCancelRequestId"));
					responseModel.setInvCancelDate((String) row.get("InvCancelDate"));
					responseModel.setInvCancelReasonId((Integer) row.get("InvCancelReasonId"));
					responseModel.setInvCancelReason((String) row.get("InvCancelReason"));
					responseModel.setInvCancelRemark((String) row.get("InvCancelRemark"));
					responseModel.setInvCancelType((String) row.get("InvCancelType"));

					responseModel.setInsuranceCharges((BigDecimal) row.get("InsuranceCharges"));
					responseModel.setRtoCharges((BigDecimal) row.get("RtoCharges"));
					responseModel.setHsrpCharges((BigDecimal) row.get("HsrpCharges"));
					responseModel.setOtherCharges((BigDecimal) row.get("OtherCharges"));
					responseModel.setTotalBasicAmnt((BigDecimal) row.get("TotalBasicAmnt"));
					responseModel.setTotalGstAmnt((BigDecimal) row.get("TotalGstAmnt"));
					responseModel.setTotalInvoiceAmnt((BigDecimal) row.get("TotalInvoiceAmnt"));
					
					
					responseModel.setToPoHdrId((BigInteger) row.get("ToPoHdrId"));
					responseModel.setPoNumber((String) row.get("PoNumber"));

					responseModel.setFinancerId((BigInteger) row.get("FinancerId"));
					responseModel.setFinancerName((String) row.get("FinancerName"));

					responseModel.setInsuranceMasterId((BigInteger) row.get("InsuranceMasterId"));
					responseModel.setPolicyStartDate((String) row.get("PolicyStartDate"));
					responseModel.setPolicyExpiryDate((String) row.get("PolicyExpiryDate"));
					responseModel.setRemarks((String) row.get("Remarks"));

					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setPartyLocation((String) row.get("PartyLocation"));
					responseModel.setPartyAddress1((String) row.get("PartyAddress1"));
					responseModel.setPartyAddress2((String) row.get("PartyAddress2"));
					responseModel.setPartyAddress3((String) row.get("PartyAddress3"));
					responseModel.setMobileNumber((String) row.get("MobileNumber"));
					responseModel.setEmailId((String) row.get("EmailId"));
					responseModel.setPanNo((String) row.get("PAN_NO"));
					responseModel.setGstIN((String) row.get("GSTIN"));
					responseModel.setPinId((BigInteger) row.get("PinId"));
					responseModel.setPincode((String) row.get("Pincode"));
					responseModel.setVillage((String) row.get("Village"));
					responseModel.setTehsil((String) row.get("Tehsil"));
					responseModel.setDistrict((String) row.get("District"));
					responseModel.setState((String) row.get("State"));
					responseModel.setCountry((String) row.get("Country"));
				}
				requestModel.setFlag(2);
				data = fetchMachineInvDtl(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					CustDtlForInvResponseModel customerDetail = null;
					for (Object object : data) {
						Map row = (Map) object;
						customerDetail = new CustDtlForInvResponseModel();
						customerDetail.setCustomerId((BigInteger) row.get("CustomerId"));
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
				data = fetchMachineInvDtl(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<DcListForInvResponseModel> dcList = new ArrayList<DcListForInvResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						DcListForInvResponseModel model = new DcListForInvResponseModel();
						model.setDcId((BigInteger) row.get("DcId"));
						model.setDcNumber((String) row.get("DcNumber"));
						model.setDcDate((String) row.get("DcDate"));
						model.setDcStatus((String) row.get("DcStatus"));
						model.setDcTypeId((Integer) row.get("DcTypeId"));
						model.setDcType((String) row.get("DcType"));
						model.setMachineAllotmentId((BigInteger) row.get("MachineAllotmentId"));
						model.setAllotNumber((String) row.get("AllotNumber"));
						model.setAllotDate((String) row.get("AllotDate"));
						model.setEnquiryHdrId((BigInteger) row.get("EnquiryId"));
						model.setEnquiryNo((String) row.get("EnquiryNumber"));
						model.setEnquiryType((String) row.get("EnquiryType"));
						model.setEnquiryDate((String) row.get("EnquiryDate"));

						dcList.add(model);
					}
					responseModel.setDcList(dcList);
				}
				requestModel.setFlag(4);
				BigDecimal totalDicountAmnt = BigDecimal.ZERO;
				data = fetchMachineInvDtl(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<InvMachDtlForInvResponseModel> machDtlList = new ArrayList<InvMachDtlForInvResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						InvMachDtlForInvResponseModel dtlForMachineResponseModel = new InvMachDtlForInvResponseModel();
						dtlForMachineResponseModel.setSalesInvoiceDtlId((BigInteger) row.get("SalesInvoiceDtlId"));
						dtlForMachineResponseModel.setDcId((BigInteger) row.get("DcId"));
						dtlForMachineResponseModel.setMachineDcDtlId((BigInteger) row.get("machineDcDtlId"));
						dtlForMachineResponseModel.setVinId((BigInteger) row.get("VinId"));
						dtlForMachineResponseModel.setMachineInventoryId((BigInteger) row.get("machine_inventory_id"));
						dtlForMachineResponseModel.setItemNo((String) row.get("ItemNo"));
						dtlForMachineResponseModel.setItemDesc((String) row.get("ItemDesc"));
						dtlForMachineResponseModel.setModel((String) row.get("Model"));
						dtlForMachineResponseModel.setChassisNo((String) row.get("ChassisNo"));
						dtlForMachineResponseModel.setVinNo((String) row.get("VinNo"));
						dtlForMachineResponseModel.setEngineNo((String) row.get("EngineNo"));
						dtlForMachineResponseModel.setHsnCode((String) row.get("HsnCode"));
						dtlForMachineResponseModel.setQty((Integer) row.get("Qty"));
						dtlForMachineResponseModel.setUnitPrice((BigDecimal) row.get("UnitPrice"));
						dtlForMachineResponseModel.setDiscountAmnt((BigDecimal) row.get("DiscountAmnt"));
						dtlForMachineResponseModel.setIgst_per((BigDecimal) row.get("Igst_per"));
						dtlForMachineResponseModel.setIgst_amount((BigDecimal) row.get("Igst_amount"));
						dtlForMachineResponseModel.setCgst_per((BigDecimal) row.get("Cgst_per"));
						dtlForMachineResponseModel.setCgst_amount((BigDecimal) row.get("Cgst_amount"));
						dtlForMachineResponseModel.setSgst_per((BigDecimal) row.get("Sgst_per"));
						dtlForMachineResponseModel.setSgst_amount((BigDecimal) row.get("Sgst_amount"));
						dtlForMachineResponseModel.setTotal_gst_per((BigDecimal) row.get("Total_gst_per"));
						dtlForMachineResponseModel.setTotal_gst_amount((BigDecimal) row.get("Total_gst_amount"));
						dtlForMachineResponseModel.setAssessableAmnt((BigDecimal) row.get("AssessableAmnt"));
						dtlForMachineResponseModel.setTotalAmnt((BigDecimal) row.get("TotalAmnt"));
						dtlForMachineResponseModel.setRemarks((String) row.get("Remarks"));

						if(dtlForMachineResponseModel.getDiscountAmnt() != null) {
							totalDicountAmnt = totalDicountAmnt.add(dtlForMachineResponseModel.getDiscountAmnt());
						}
						
						machDtlList.add(dtlForMachineResponseModel);
					}

					responseModel.setMachineList(machDtlList);
				}

				requestModel.setFlag(5);
				data = fetchMachineInvDtl(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<InvItemDtlForInvResponseModel> itemList = new ArrayList<InvItemDtlForInvResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						InvItemDtlForInvResponseModel dtlForMachineResponseModel = new InvItemDtlForInvResponseModel();
						dtlForMachineResponseModel.setSalesInvoiceItemId((BigInteger) row.get("SalesInvoiceItemId"));
						dtlForMachineResponseModel.setDcItemId((BigInteger) row.get("dcItemId"));
						dtlForMachineResponseModel.setMachineItemId((BigInteger) row.get("machineItemId"));
						dtlForMachineResponseModel.setItemNo((String) row.get("ItemNo"));
						dtlForMachineResponseModel.setItemDesc((String) row.get("ItemDesc"));
						dtlForMachineResponseModel.setQty((Integer) row.get("Qty"));
						dtlForMachineResponseModel.setHsnCode((String) row.get("HsnCode"));
						dtlForMachineResponseModel.setQty((Integer) row.get("Qty"));
						dtlForMachineResponseModel.setUnitPrice((BigDecimal) row.get("UnitPrice"));
						dtlForMachineResponseModel.setDiscountAmnt((BigDecimal) row.get("DiscountAmnt"));
						dtlForMachineResponseModel.setIgst_per((BigDecimal) row.get("Igst_per"));
						dtlForMachineResponseModel.setIgst_amount((BigDecimal) row.get("Igst_amount"));
						dtlForMachineResponseModel.setCgst_per((BigDecimal) row.get("Cgst_per"));
						dtlForMachineResponseModel.setCgst_amount((BigDecimal) row.get("Cgst_amount"));
						dtlForMachineResponseModel.setSgst_per((BigDecimal) row.get("Sgst_per"));
						dtlForMachineResponseModel.setSgst_amount((BigDecimal) row.get("Sgst_amount"));
						dtlForMachineResponseModel.setTotal_gst_per((BigDecimal) row.get("Total_gst_per"));
						dtlForMachineResponseModel.setTotal_gst_amount((BigDecimal) row.get("Total_gst_amount"));
						dtlForMachineResponseModel.setAssessableAmnt((BigDecimal) row.get("AssessableAmnt"));
						dtlForMachineResponseModel.setTotalAmnt((BigDecimal) row.get("TotalAmnt"));
						dtlForMachineResponseModel.setRemarks((String) row.get("Remarks"));
						
						if(dtlForMachineResponseModel.getDiscountAmnt() != null) {
							totalDicountAmnt = totalDicountAmnt.add(dtlForMachineResponseModel.getDiscountAmnt());
						}

						itemList.add(dtlForMachineResponseModel);
					}
					responseModel.setItemList(itemList);
				}
				
				totalDicountAmnt = totalDicountAmnt.setScale(2);
				responseModel.setTotalDicountAmnt(totalDicountAmnt);
				;
				requestModel.setFlag(6);
				data = fetchMachineInvDtl(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<InvCancelAppViewResponseModel> cancelApprovalList = new ArrayList<InvCancelAppViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						InvCancelAppViewResponseModel appResponseModel = new InvCancelAppViewResponseModel();
						appResponseModel.setApprovalStaus((String) row.get("ApprovalStaus"));
						appResponseModel.setApprovedDate((String) row.get("ApprovedDate"));
						appResponseModel.setCancelReasonDesc((String) row.get("CancelReasonDesc"));
						appResponseModel.setCancelRemark((String) row.get("CancelRemark"));
						appResponseModel.setCancelRequestDate((String) row.get("CancelRequestDate"));
						appResponseModel.setInvCancelDate((String) row.get("InvCancelDate"));
						appResponseModel.setDesginationDesc((String) row.get("DesginationDesc"));
						appResponseModel.setEmployeeName((String) row.get("EmployeeName"));

						cancelApprovalList.add(appResponseModel);
					}

					responseModel.setCancelApprovalList(cancelApprovalList);
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
	public List fetchMachineInvDtl(Session session, String userCode, InvViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMachineInvDtl invoked...");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_INV_VIEW] :userCode, :invId, :invNo, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("invId", requestModel.getSalesInvoiceHdrId());
			query.setParameter("invNo", requestModel.getInvoiceNumber());
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
