/**
 * 
 */
package com.hitech.dms.web.dao.inv.dc.list;

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
import com.hitech.dms.web.model.inv.dc.list.request.DcListForInvRequestModel;
import com.hitech.dms.web.model.inv.dc.list.response.DcDtlForCustInvResponseModel;
import com.hitech.dms.web.model.inv.dc.list.response.DcListForInvResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class DcDtlForCustInvDaoImpl implements DcDtlForCustInvDao {
	private static final Logger logger = LoggerFactory.getLogger(DcDtlForCustInvDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public DcDtlForCustInvResponseModel fetchInvoiceDCDtlForCustomer(String userCode,
			DcListForInvRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchInvoiceDCDtlForCustomer invoked.." + requestModel.toString());
		}
		Session session = null;
		DcDtlForCustInvResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			boolean isDcAvail = true;
			requestModel.setFlag(1);
			List data = fetchInvoiceDCDtlForCustomer(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				responseModel = new DcDtlForCustInvResponseModel();
				for (Object object : data) {
					Map row = (Map) object;
					if((String) row.get("msg") != null && !((String) row.get("msg")).equals("")) {
						isDcAvail = false;
						break;
					}
					responseModel.setInsuranceMasterId((BigInteger) row.get("InsuranceMasterId"));
					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setPartyLocation((String) row.get("PartyLocation"));
					responseModel.setPartyAddress1((String) row.get("PartyAddress1"));
					responseModel.setPartyAddress2((String) row.get("PartyAddress2"));
					responseModel.setPartyAddress3((String) row.get("PartyAddress3"));
					responseModel.setMobileNumber((String) row.get("MobileNumber"));
					responseModel.setEmailId((String) row.get("EmailId"));
					responseModel.setPinId((BigInteger) row.get("PinId"));
					responseModel.setPincode((String) row.get("Pincode"));
					responseModel.setVillage((String) row.get("Village"));
					responseModel.setTehsil((String) row.get("Tehsil"));
					responseModel.setDistrict((String) row.get("District"));
					responseModel.setState((String) row.get("State"));
					responseModel.setCountry((String) row.get("Country"));
					responseModel.setPanNo((String) row.get("PAN_NO"));
					responseModel.setGstIN((String) row.get("GSTIN"));
					responseModel.setPolicyStartDate((String) row.get("PolicyStartDate"));
					responseModel.setPolicyEndDate((String) row.get("PolicyEndDate"));
				}
				if(isDcAvail) {
					requestModel.setFlag(2);
					data = fetchInvoiceDCDtlForCustomer(session, userCode, requestModel);
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
							customerDetail.setContactTitle((String) row.get("Title"));
							customerDetail.setCustomerName((String) row.get("CustomerName"));
							customerDetail.setWhatsAppNo((String) row.get("WhatsappNo"));
							customerDetail.setAlternateNo((String) row.get("Alternate_No"));
							customerDetail.setEmailId((String) row.get("Email_id"));
							customerDetail.setAddress1((String) row.get("address1"));
							customerDetail.setAddress2((String) row.get("address2"));
							customerDetail.setAddress3((String) row.get("address3"));
							customerDetail.setPanNo((String) row.get("PAN_NO"));
							customerDetail.setGstIN((String) row.get("GSTIN"));
							customerDetail.setPincode((String) row.get("pincode"));
							customerDetail.setVillage((String) row.get("village"));
							customerDetail.setTehsil((String) row.get("tehsil"));
							customerDetail.setDistrict((String) row.get("district"));
							customerDetail.setState((String) row.get("state"));
							customerDetail.setCountry((String) row.get("country"));
						}
	
						responseModel.setCustomerDetail(customerDetail);
					}
					requestModel.setFlag(3);
					data = fetchInvoiceDCDtlForCustomer(session, userCode, requestModel);
					if (data != null && !data.isEmpty()) {
						List<DcListForInvResponseModel> dcList = new ArrayList<DcListForInvResponseModel>();
						for (Object object : data) {
							Map row = (Map) object;
							DcListForInvResponseModel invResponseModel = new DcListForInvResponseModel();
							invResponseModel.setDcId((BigInteger) row.get("DcId"));
							invResponseModel.setDcNumber((String) row.get("DcNumber"));
							invResponseModel.setDcDate((String) row.get("DcDate"));
							invResponseModel.setDcStatus((String) row.get("DcStatus"));
							invResponseModel.setDcTypeId((Integer) row.get("DcTypeId"));
							invResponseModel.setDcType((String) row.get("DcType"));
							invResponseModel.setMachineAllotmentId((BigInteger) row.get("MachineAllotmentId"));
							invResponseModel.setAllotNumber((String) row.get("AllotNumber"));
							invResponseModel.setAllotDate((String) row.get("AllotDate"));
							invResponseModel.setEnquiryHdrId((BigInteger) row.get("EnquiryId"));
							invResponseModel.setEnquiryNo((String) row.get("EnquiryNumber"));
							invResponseModel.setEnquiryType((String) row.get("EnquiryType"));
							invResponseModel.setEnquiryDate((String) row.get("EnquiryDate"));
	
							dcList.add(invResponseModel);
						}
						responseModel.setDcList(dcList);
					}
				}else {
					responseModel = null;
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
	public List fetchInvoiceDCDtlForCustomer(Session session, String userCode, DcListForInvRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchInvoiceDCDtlForCustomer invoked...");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_INV_DC_LIST_FOR_CUST] :userCode, :dealerId, :branchId, :pcId, :customerId, :invoiceTypeId, :flag, :isFor";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("customerId", requestModel.getCustomerId());
			query.setParameter("invoiceTypeId", requestModel.getInvoiceTypeId());
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
