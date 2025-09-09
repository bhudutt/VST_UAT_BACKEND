/**
 * 
 */
package com.hitech.dms.web.validators.admin.org.add;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.admin.org.request.OrgLevelHierRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class DealerOrgHierAddValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(DealerOrgHierAddValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {
		return OrgLevelHierRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		OrgLevelHierRequestModel requestModel = (OrgLevelHierRequestModel) target;

		if (requestModel.getDealerId() == null || requestModel.getDealerId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("dealerId", "Dealer is required.");
		}
		
		if (requestModel.getPcId() == null || requestModel.getDealerId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("pcId", "PC is required.");
		}
		
		if (requestModel.getDepartmentId() == null || requestModel.getDepartmentId().compareTo(0) == 0) {
			errors.reject("departmentId", "Department is required.");
		}
		
		if (requestModel.getOrgHierId() == null || requestModel.getOrgHierId().compareTo(BigInteger.ZERO) == 0) {
			errors.reject("orgHierarchyID", "Org. Hierarchy is required.");
		}
	}
}
