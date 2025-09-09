package com.hitech.dms.web.dao.addtionalcriteria;

import com.hitech.dms.web.model.scheme.additionalCriteria.request.AdditinalCriteriaRequestModel;
import com.hitech.dms.web.model.scheme.additionalCriteria.response.SchemeTypeOnchangeResponseModel;

public interface AdditinalCriteriaDao {
	public SchemeTypeOnchangeResponseModel fetchSchemeTypeOnchangeDetail(String userCode,
			AdditinalCriteriaRequestModel requestModel);
}
