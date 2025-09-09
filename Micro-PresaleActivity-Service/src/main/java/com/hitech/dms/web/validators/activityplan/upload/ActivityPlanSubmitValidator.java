/**
 * 
 */
package com.hitech.dms.web.validators.activityplan.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.activityplan.upload.request.ActivityPlanFormRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class ActivityPlanSubmitValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(ActivityPlanSubmitValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return ActivityPlanFormRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		ActivityPlanFormRequestModel activityPlanUploadRequestModel = (ActivityPlanFormRequestModel) target;

		if (activityPlanUploadRequestModel.getStgActivityPlanHdrId() == null
				|| activityPlanUploadRequestModel.getStgActivityPlanHdrId().equals("")) {
			errors.reject("activityNumber", "Stg Activity Number is required.");
		}
	}
}
