/**
 * 
 */
package com.hitech.dms.web.validator.opex.upload;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.opex.template.upload.request.OpexBudgetUploadRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class OpexBudgetUploadValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(OpexBudgetUploadValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return OpexBudgetUploadRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		OpexBudgetUploadRequestModel requestModel = (OpexBudgetUploadRequestModel) target;

		if (requestModel.getPcId() == null || requestModel.getPcId().compareTo(0) == 0) {
			errors.reject("pcId", "Profit Center is required.");
		}

		if (requestModel.getStateId() == null || requestModel.getStateId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("stateId", "State is required.");
		}
	}
}
