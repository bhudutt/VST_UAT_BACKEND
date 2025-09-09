package com.hitech.dms.web.dao.emp;

import java.util.List;

import com.hitech.dms.web.model.emp.request.EmployeeRequestModel;
import com.hitech.dms.web.model.emp.response.EmployeeResponseModel;

public interface EmployeeDao {
	public List<EmployeeResponseModel> fetchEmployeeList(String userCode, EmployeeRequestModel requestModel);
}
