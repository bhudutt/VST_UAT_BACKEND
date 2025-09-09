package com.hitech.dms.app.validatior;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class AppBindingResultValidator {
	public BindingResult getBindingResult(Validator validator, Object target, BindingResult bindingResult) {
		ValidationUtils.invokeValidator(validator, target, bindingResult);
		return bindingResult;
	}
}
