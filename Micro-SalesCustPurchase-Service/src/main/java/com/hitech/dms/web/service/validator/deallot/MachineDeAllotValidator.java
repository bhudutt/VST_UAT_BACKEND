/**
 * 
 */
package com.hitech.dms.web.service.validator.deallot;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.allot.deallot.request.MachineDeAllotRequestModel;
import com.hitech.dms.web.service.validator.allot.MachineAllotCreateValidator;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class MachineDeAllotValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(MachineAllotCreateValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return MachineDeAllotRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (logger.isDebugEnabled()) {
			logger.debug("Invoking Machine Validator form validator");
		}
		MachineDeAllotRequestModel requestModel = (MachineDeAllotRequestModel) target;

		if (requestModel.getPcId() == null || requestModel.getPcId().compareTo(0) == 0) {
			errors.reject("pcId", "Profit Center is required.");
		}

		if (requestModel.getBranchId() == null || requestModel.getBranchId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("branchId", "Branch is required.");
		}

		if (requestModel.getMachineAllotmentId() == null
				|| requestModel.getMachineAllotmentId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("machineAllotmentId", "Machine Allotment Number is required.");
		}

		if (requestModel.getAllotNumber() == null || requestModel.getAllotNumber().equals("")) {
			errors.reject("allotNumber", "Machine Allotment Number is required.");
		}

		if (requestModel.getDeAllotReason() == null || requestModel.getDeAllotReason().equals("")) {
			errors.reject("deAllotReason", "De-Allot Reason is required.");
		}

		if (requestModel.getDeAllotDate() == null) {
			errors.reject("deAllotDate", "De-Allot Date is required.");
		}
	}
}
