/**
 * 
 */
package com.hitech.dms.web.validators.admin.create;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.admin.create.request.HoUserCreateRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class HoUserCreateValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(HoUserCreateValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return HoUserCreateRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		HoUserCreateRequestModel requestModel = (HoUserCreateRequestModel) target;

		if (requestModel.getEmployeeCode() == null || requestModel.getEmployeeCode().equals("")) {
			errors.reject("employeeCode", "Employee Code is required.");
		}
		
		if (requestModel.getEmpContactNo() == null || requestModel.getEmpContactNo().equals("")) {
			errors.reject("empContactNo", "Employee Contact No. is required.");
		}
	}
}
