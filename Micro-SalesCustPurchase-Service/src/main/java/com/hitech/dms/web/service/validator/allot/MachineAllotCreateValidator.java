/**
 * 
 */
package com.hitech.dms.web.service.validator.allot;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.allot.create.request.MachineAllotCreateRequestModel;
import com.hitech.dms.web.model.allot.create.request.MachineAllotDtlCreateRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class MachineAllotCreateValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(MachineAllotCreateValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return MachineAllotCreateRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (logger.isDebugEnabled()) {
			logger.debug("Invoking Machine Validator form validator");
		}
		MachineAllotCreateRequestModel requestModel = (MachineAllotCreateRequestModel) target;

		if (requestModel.getPcId() == null || requestModel.getPcId().compareTo(0) == 0) {
			errors.reject("pcId", "Profit Center is required.");
		}
		if (requestModel.getDealerId() == null || requestModel.getDealerId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("dealerId", "Dealer is required.");
		}

		if (requestModel.getBranchId() == null || requestModel.getBranchId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("branchId", "Branch is required.");
		}
		if (requestModel.isOnlyImplementFlag()) {
			if (requestModel.getEnqItemDtlList() == null || requestModel.getEnqItemDtlList().isEmpty()) {
				errors.reject("enqItemDtlList", "AtLeast One Implement is required.");
			}
		} else {
			if (requestModel.getEnquiryId() == null) {
				errors.reject("enquiryId", "Enquiry is required.");
			}
			if (requestModel.getEnqMachineDtlList() == null || requestModel.getEnqMachineDtlList().isEmpty()) {
				errors.reject("enqMachineDtlList", "AtLeast One Machine Implement is required For Allotment.");
			} else {
				boolean atLeastOneAlloted = false;
				for (MachineAllotDtlCreateRequestModel dtlCreateRequestModel : requestModel.getEnqMachineDtlList()) {
					if (dtlCreateRequestModel.getIsAlloted()) {
						atLeastOneAlloted = true;
					}
				}
				if (!atLeastOneAlloted) {
					errors.reject("enqMachineDtlList", "Please Check AtLeast One Machine For Allotment Is Required.");
				}
			}
		}
	}
}
