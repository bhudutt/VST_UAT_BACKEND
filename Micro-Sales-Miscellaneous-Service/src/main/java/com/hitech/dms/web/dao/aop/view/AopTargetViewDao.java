package com.hitech.dms.web.dao.aop.view;

import com.hitech.dms.web.model.aop.view.request.AopTargetViewRequestModel;
import com.hitech.dms.web.model.aop.view.response.AopTargetViewResponseModel;

public interface AopTargetViewDao {
	public AopTargetViewResponseModel fetchAopTargetView(String userCode, AopTargetViewRequestModel requestModel);
}
