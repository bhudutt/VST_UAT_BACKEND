/**
 * 
 */
package com.hitech.dms.web.validators.admin.dlrvstehsil;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.admin.dlrvstehsil.request.DlrVsTehsilMapRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class DlrVsTehsilMapValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(DlrVsTehsilMapValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return DlrVsTehsilMapRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		DlrVsTehsilMapRequestModel requestModel = (DlrVsTehsilMapRequestModel) target;

		if (requestModel.getDealerId() == null || requestModel.getDealerId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("dealerId", "Please Select Dealer.");
		}

		if (requestModel.getTehsilList() == null || requestModel.getTehsilList().isEmpty()) {
			errors.reject("tehsilList", "Please Select Atleast One Tehsil.");
		}
	}
}
