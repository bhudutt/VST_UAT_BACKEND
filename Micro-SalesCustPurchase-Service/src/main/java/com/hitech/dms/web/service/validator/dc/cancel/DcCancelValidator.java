/**
 * 
 */
package com.hitech.dms.web.service.validator.dc.cancel;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.dc.cancel.request.DcCancelRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class DcCancelValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(DcCancelValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return DcCancelRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (logger.isDebugEnabled()) {
			logger.debug("Invoking DC Validator form validator");
		}
		DcCancelRequestModel requestModel = (DcCancelRequestModel) target;

		if (requestModel.getPcId() == null || requestModel.getPcId().compareTo(0) == 0) {
			errors.reject("pcId", "Profit Center is required.");
		}
		if (requestModel.getDealerId() == null || requestModel.getDealerId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("dealerId", "Dealer is required.");
		}

		if (requestModel.getBranchId() == null || requestModel.getBranchId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("branchId", "Branch is required.");
		}

		if (requestModel.getDcCancelDate() == null) {
			errors.reject("dcCancelDate", "Dc Cancel Date is required.");
		}

		if (requestModel.getDcCancelReasonId() == null
				|| requestModel.getDcCancelReasonId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("dcCancelReasonId", "Dc Cancel Reason is required.");
		}

		if (requestModel.getDcCancelRemark() == null || requestModel.getDcCancelRemark().equals("")) {
			errors.reject("dcCancelRemark", "Dc Cancel Remarks is required.");
		}
	}
}
