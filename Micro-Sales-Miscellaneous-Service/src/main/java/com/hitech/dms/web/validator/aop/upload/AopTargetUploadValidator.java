/**
 * 
 */
package com.hitech.dms.web.validator.aop.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.aop.template.upload.request.AopTargetUploadRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class AopTargetUploadValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(AopTargetUploadValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return AopTargetUploadRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		AopTargetUploadRequestModel requestModel = (AopTargetUploadRequestModel) target;

		if (requestModel.getPcId() == null || requestModel.getPcId().compareTo(0) == 0) {
			errors.reject("pcId", "Profit Center is required.");
		}
	}
}
