/**
 * 
 */
package com.hitech.dms.web.validators.enquiry.create;

import java.math.BigInteger;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.app.utils.DateUtils;
import com.hitech.dms.web.model.enquiry.create.request.CustomerHDRRequestModel;
import com.hitech.dms.web.model.enquiry.create.request.EnquiryCreateRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class EnquiryCreateValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryCreateValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return EnquiryCreateRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
//		if (errors.getErrorCount() == 0) {
			Date currDate = new Date();
			EnquiryCreateRequestModel enquiryCreateRequestModel = (EnquiryCreateRequestModel) target;
			if (enquiryCreateRequestModel.getBranchId() == null
					|| enquiryCreateRequestModel.getBranchId().compareTo(BigInteger.ZERO) == 0) {
				errors.reject("branchId", "BranchId is required.");
			}
			if (enquiryCreateRequestModel.getPcId() == null
					|| enquiryCreateRequestModel.getPcId().compareTo(BigInteger.ZERO) == 0) {
				errors.reject("pcId", "Profit Center is required.");
			}
			if (enquiryCreateRequestModel.getEnquiryDate() == null) {
				errors.reject("enquiryDate", "Enquiry Date is required.");
			}
			if (enquiryCreateRequestModel.getExpectedPurchaseDate() == null) {
				errors.reject("expectedPurchaseDate", "Enquiry Purchase Date is required.");
			} else if (!DateUtils.validateStartDateIsAfterOrEqual(enquiryCreateRequestModel.getExpectedPurchaseDate(),
					currDate)) {
				errors.reject("expectedPurchaseDate", "Enquiry Purchase Date must be greater/equal than Current Date.");
			}
			if (enquiryCreateRequestModel.getEnquiryTypeid() == null
					|| enquiryCreateRequestModel.getEnquiryTypeid().compareTo(BigInteger.ZERO) == 0) {
				errors.reject("enquiryTypeid", "Enquiry Type is required.");
			}
			if (enquiryCreateRequestModel.getEnquiryStageId() == null
					|| enquiryCreateRequestModel.getEnquiryStageId().compareTo(0) == 0) {
				errors.reject("enquiryStageId", "Enquiry Stage is required.");
			}
			if (enquiryCreateRequestModel.getEnquiryStatus() == null
					|| enquiryCreateRequestModel.getEnquiryStatus().equals("")) {
				errors.reject("enquiryStatus", "Enquiry Status is required.");
			}
			if (enquiryCreateRequestModel.getEnquirySource() == null
					|| enquiryCreateRequestModel.getEnquirySource().compareTo(0) == 0) {
				errors.reject("enquirySource", "Enquiry Source is required.");
			}
			if (enquiryCreateRequestModel.getSalesmanId() == null
					|| enquiryCreateRequestModel.getSalesmanId().compareTo(BigInteger.ZERO) == 0) {
				errors.reject("salesmanId", "Salesman is required.");
			}
			if (enquiryCreateRequestModel.getCustomerHDRRequestModel() != null) {
				CustomerHDRRequestModel customerHDRRequestModel = enquiryCreateRequestModel
						.getCustomerHDRRequestModel();
				if (customerHDRRequestModel.getMobileNo() == null || customerHDRRequestModel.getMobileNo().equals("")) {
					errors.reject("mobileNo", "Customer Mobile Is Required");
				}
//				if (customerHDRRequestModel.getCustomerCategoryId() == null
//						|| customerHDRRequestModel.getCustomerCategoryId().compareTo(BigInteger.ZERO) == 0) {
//					errors.reject("customerCategoryId", "Customer Category Is Required");
//				}
				if (customerHDRRequestModel.getFirstName() == null
						|| customerHDRRequestModel.getFirstName().equals("")) {
					errors.reject("firstName", "First Name Is Required");
				}
//				if (customerHDRRequestModel.getAddress1() == null || customerHDRRequestModel.getAddress1().equals("")) {
//					errors.reject("address1", "Address1 Is Required");
//				}
				if (customerHDRRequestModel.getPinId() == null
						|| customerHDRRequestModel.getPinId().compareTo(BigInteger.ZERO) == 0) {
					errors.reject("pinId", "Pincode Is Required");
				}
			}else {
				errors.reject("customerHDRRequestModel", "Customer Is Required");
			}

			if (enquiryCreateRequestModel.getIsExchangeRequired() == null) {
				errors.reject("isExchangeRequired", "IsExchange is required.");
			}
			if (enquiryCreateRequestModel.getModelId() == null
					|| enquiryCreateRequestModel.getModelId().compareTo(BigInteger.ZERO) == 0) {
				errors.reject("modelId", "Model is required.");
			}
			if (enquiryCreateRequestModel.getCashLoan() == null || enquiryCreateRequestModel.getCashLoan().equals("")) {
				errors.reject("cashLoan", "Please Select Cash/Loan.");
			}
//		}
	}

}
