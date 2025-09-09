/**
 * 
 */
package com.hitech.dms.web.validator.indent;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.indent.create.request.IndentCreateRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class IndentCreateValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return IndentCreateRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		IndentCreateRequestModel requestModel = (IndentCreateRequestModel) target;
		
		
	}
	
}
