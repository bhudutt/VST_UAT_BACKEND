/**
 * 
 */
package com.hitech.dms.web.validators.activityplan.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hitech.dms.web.model.activityplan.upload.request.ActivityPlanUploadRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class ActivityPlanUploadValidator implements Validator {
	private static final Logger logger = LoggerFactory.getLogger(ActivityPlanUploadValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return ActivityPlanUploadRequestModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		ActivityPlanUploadRequestModel activityPlanUploadRequestModel = (ActivityPlanUploadRequestModel) target;

		if (activityPlanUploadRequestModel.getPcID() == null
				|| activityPlanUploadRequestModel.getPcID().compareTo(0) == 0) {
			errors.reject("pcId", "Profit Center is required.");
		}
	}
}
