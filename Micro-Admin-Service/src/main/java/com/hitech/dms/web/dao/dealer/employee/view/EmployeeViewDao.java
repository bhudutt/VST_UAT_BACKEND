package com.hitech.dms.web.dao.dealer.employee.view;

import com.hitech.dms.web.model.dealer.employee.view.request.EmployeeViewRequestModel;
import com.hitech.dms.web.model.dealer.employee.view.response.EmployeeViewResponseModel;

public interface EmployeeViewDao {
	public EmployeeViewResponseModel fetchEmployeeDetailById(String userCode, EmployeeViewRequestModel requestModel);
}
