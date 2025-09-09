package com.hitech.dms.web.dao.oldchassis.validate;

import java.math.BigInteger;

import com.hitech.dms.web.model.oldchassis.validate.request.OldChassisValidateRequestModel;
import com.hitech.dms.web.model.oldchassis.validate.response.OldChassisDTLResponseModel;
import com.hitech.dms.web.model.oldchassis.validate.response.OldChassisValidateResponseModel;

public interface OldChassisValidateDao {

	OldChassisDTLResponseModel fetchOldChassisDTLList(String userCode, BigInteger vinId);

	OldChassisValidateResponseModel fetchOldChassisDTLList(String userCode,
			OldChassisValidateRequestModel oldChassisValidateRequestModel);


}
