/**
 * 
 */
package com.hitech.dms.web.service.validator.dc.create;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.dc.create.request.DcCreateRequestModel;
import com.hitech.dms.web.service.validator.dc.cancel.DcCancelValidator;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class DcCreateValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(DcCancelValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return DcCreateRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (logger.isDebugEnabled()) {
			logger.debug("Invoking DC Validator form validator");
		}
		DcCreateRequestModel requestModel = (DcCreateRequestModel) target;

		if (requestModel.getPcId() == null || requestModel.getPcId().compareTo(0) == 0) {
			errors.reject("pcId", "Profit Center is required.");
		}
		if (requestModel.getDealerId() == null || requestModel.getDealerId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("dealerId", "Dealer is required.");
		}

		if (requestModel.getBranchId() == null || requestModel.getBranchId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("branchId", "Branch is required.");
		}

		if (requestModel.getDcDtlList() == null || requestModel.getDcDtlList().isEmpty()) {
			errors.reject("dcDtlList", "Dc Detail List is required.");
		}

		if (requestModel.getCustomerDetail().getCustomerId() == null
				|| requestModel.getCustomerDetail().getCustomerId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("customerId", "Customer is required.");
		}
	}
}
